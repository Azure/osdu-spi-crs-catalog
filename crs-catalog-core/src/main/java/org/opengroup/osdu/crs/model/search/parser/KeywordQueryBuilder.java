// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model.search.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengroup.osdu.crs.model.Utility;
import org.opengroup.osdu.crs.model.search.Indexer;
import org.opengroup.osdu.crs.model.search.SearchCriteria;

/**
 * A keyword query build to create a lucene search query based on the search input.
 */
public class KeywordQueryBuilder {

    private static final String WildCard = "___MULTICHAR_WILDCARD___";

    /**
     * Create a lucene search query based on the search input
     * @param searchInput search input
     * @param analyzer analyzer of the query
     * @param defaultFieldName default field for item without field item.
     * @return lucene query
     * @throws IllegalArgumentException an exception will be thrown
     * if both keyword and data type in search input are null or empty
     * @throws ParseException an exception will be thrown if the syntax of the keyword is invalid.
     */
    public static Query createQuery(SearchCriteria searchInput, Analyzer analyzer, String defaultFieldName)
            throws IllegalArgumentException, ParseException
    {
        String keyword = searchInput.getKeyword();
        if(Utility.isNullOrEmpty(keyword) && Utility.isNullOrEmpty(searchInput.getType()))
            throw new IllegalArgumentException("Keyword can not be null");

        // Should we treat the data query special?
        Query dataTypeQuery = null;
        // Add data type search constraint
        if(!Utility.isNullOrEmpty(searchInput.getType())) {
            String fieldName;
            if(searchInput.isParentType()) {
                fieldName = Indexer.ParentClassType;
            }
            else {
                fieldName = Indexer.ClassType;
            }
            String keywordWord = searchInput.getType();
            dataTypeQuery = createKeywordQuery(fieldName, keywordWord, analyzer);
        }

        KeywordParser keywordParser = new KeywordParser();
        List<List<Item>> items;
        try {
            items = keywordParser.parse(keyword);
        }
        catch(UnexpectedParameterException ex) {
            throw new IllegalArgumentException("Invalid syntax in the keyword");
        }
        catch (MissingFieldValueException ex) {
            throw new IllegalArgumentException("Invalid syntax in the keyword");
        }

        List<Query> queries = new ArrayList<Query>();
        for (List<Item> itemList : items) {
            Query query = createQuery(itemList, analyzer,  defaultFieldName);
            if(query != null)
                queries.add(query);
        }

        Query keywordQuery = null;
        if(queries.size() > 0) {
            if (queries.size() == 1)
                keywordQuery = queries.get(0);
            else {
                BooleanQuery.Builder bq = new BooleanQuery.Builder();
                for (Query query : queries) {
                    bq.add(query, BooleanClause.Occur.SHOULD);
                }
                keywordQuery = bq.build();
            }
        }
        if(keywordQuery == null && dataTypeQuery == null)
            throw new IllegalArgumentException("Keyword can not be null");
        if(keywordQuery != null && dataTypeQuery == null)
            return keywordQuery;
        if(keywordQuery == null && dataTypeQuery != null)
            return dataTypeQuery;

        BooleanQuery.Builder bq = new BooleanQuery.Builder();
        bq.add(keywordQuery, BooleanClause.Occur.MUST);
        bq.add(dataTypeQuery, BooleanClause.Occur.MUST);
        return bq.build();
        // return keywordQuery;
    }

    private static Query createQuery(List<Item> itemList, Analyzer analyzer, String defaultFieldName) throws ParseException {
        if (itemList == null || itemList.size() == 0) return null;

        Map<Query, BooleanClause.Occur> queries = new HashMap<>();
        boolean allExcluded = true;
        for(Item item : itemList) {
            ItemValue field = item.getField();
            String fieldName = Indexer.Content;
            if(field != null && !Utility.isNullOrEmpty(field.getValue()))
                fieldName = field.getValue();
            if(!item.isExcluded())
                allExcluded = false;

            ItemValue content = item.getValue();
            if(content == null || Utility.isNullOrEmpty(content.getValue()))
                continue; //It should not reach here
            String keyword = content.isQuoted()
                    ? "\"" + QueryParser.escape(content.getValue()) + "\""
                    : escapeNonPhraseKeyword(content.getValue());
            Query query = createKeywordQuery(fieldName, keyword, analyzer);

            /*TODO
            // for SMR 2603216
                // if this is not a field specific query, go ahead and add it to Name as an OR to boost it
                // We ONLY do this to boost hits which matches on name. We want to help any name hits to the top of
                // the list. We could potentially do this with other fields too - to boost runtime based on field.
                if (item.Field == null && !item.Excluded)
                {
                    var bquery = new BooleanQuery();
                    var nameBoost = createKeywordQuery(analyzer, WellKnownAttributeInfo.NameInfo, keyword);
                    bquery.Add(query, BooleanClause.Occur.MUST);
                    bquery.Add(nameBoost, BooleanClause.Occur.SHOULD);
                    query = bquery;
                }
             */

            // Add query if we have not already added it. I.e. a query where one includes the same keyword twice should not fail (i.e. "boutros boutros ghali")
            if (!queries.containsKey(query))
                queries.put(query, item.isExcluded() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.MUST);
        }

        if (queries.size() == 0)
            return null;

        if (queries.size() == 1)
        {
            for (Map.Entry<Query, BooleanClause.Occur> entry: queries.entrySet())
            {
                Query query = entry.getKey();
                if (allExcluded)
                {
                    //Excluded item must combine with at least one non-excluded item
                    Query wildcardQuery = createKeywordQuery(defaultFieldName, "*", analyzer);

                    BooleanQuery.Builder bq = new BooleanQuery.Builder();
                    bq.add(query, BooleanClause.Occur.MUST_NOT);
                    bq.add(wildcardQuery, BooleanClause.Occur.SHOULD);
                    query = bq.build();
                }
                return query;
            }
        }

        BooleanQuery.Builder bq = new BooleanQuery.Builder();
        for (Map.Entry<Query, BooleanClause.Occur> entry: queries.entrySet()) {
            //The relationship of the words in ItemAndList is AND
            bq.add(entry.getKey(), entry.getValue());
        }
        if(allExcluded) {
            //Excluded items must combine with at least one non-excluded item
            Query wildcardQuery = createKeywordQuery(defaultFieldName, "*", analyzer);
            bq.add(wildcardQuery, BooleanClause.Occur.SHOULD);
        }
        return bq.build();
    }

    private static Query createKeywordQuery(String fieldName, String keyword, Analyzer analyzer) throws ParseException {
        if(Indexer.Id.equals(fieldName)) {
            //Remove the quote if exist for term query
            keyword = removeKeywordQuotes(keyword);
            return new TermQuery(new Term(fieldName, keyword));
        }

        QueryParser parser = new QueryParser(fieldName, analyzer);
        // This can be slow, but at least the query will work if it starts with a wildcard
        // Otherwise it makes no difference
        parser.setAllowLeadingWildcard(true);
        return parser.parse(keyword);
    }

    private static String removeKeywordQuotes(String keyword)
    {
        if (keyword.length() > 2 && keyword.startsWith("\"") && keyword.endsWith("\""))
            keyword = keyword.substring(1, keyword.length() - 2);
        return keyword;
    }


    private static String escapeNonPhraseKeyword(String keyword)
    {
        if (keyword.contains("*"))
        {
            keyword = keyword.replace("*", WildCard);
            QueryParser.escape(keyword);
            keyword = keyword.replace(WildCard, "*");
        }
        else
        {
            keyword = QueryParser.escape(keyword);
        }

        return keyword;
    }

}
