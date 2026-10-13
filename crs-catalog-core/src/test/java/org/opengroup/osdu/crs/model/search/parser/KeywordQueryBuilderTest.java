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

package org.opengroup.osdu.crs.model.search.parser;

import org.opengroup.osdu.crs.model.search.Indexer;
import org.opengroup.osdu.crs.model.search.SearchCriteria;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class KeywordQueryBuilderTest {

    private static final String FieldDelimiter = ":";
    private static Analyzer analyzer = null;
    private String[] supportedFields = {"id", "content", "type", "reference", "name", "namespace", "source", "symbol",
            "ancestry", "code", "dimensionCode", "quantityCode", "dimensionAnalysis", "state"};;

    @BeforeAll
    public static void setup() {
        try {
            analyzer = CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)
                    .addTokenFilter(ASCIIFoldingFilterFactory.class, "preserveOriginal", "true")
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .build();
        }
        catch(Exception ex) {
            //Ignore
        }
    }

    @Test
    public void createFieldQueryWithoutDataTypeTest() throws Exception {
        assertNotNull(analyzer);

        String value = "a10";
        for (String fieldName: supportedFields) {
            String keyword = fieldName + FieldDelimiter + value;
            SearchCriteria searchInput = new SearchCriteria(keyword);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, Indexer.Content);

            assertNotNull(fieldName);
            String queryText = fieldName + FieldDelimiter + value;
            assertEquals(queryText, query.toString());
        }
    }

    @Test
    public void createFieldQueryWithDataTypeTest() throws Exception {
        assertNotNull(analyzer);

        String dataType = "abc";
        String value = "a10";
        for (String fieldName: supportedFields) {
            String keyword = fieldName + FieldDelimiter + value;
            SearchCriteria searchInput = new SearchCriteria(keyword, dataType);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, Indexer.Content);

            assertNotNull(fieldName);
            String queryText = "+" + fieldName + FieldDelimiter + value + " +" + Indexer.ClassType + FieldDelimiter + dataType;
            assertEquals(queryText, query.toString());
        }
    }

    @Test
    public void createFieldQueryTestWithoutKeywordAndDataTypeTest() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            assertNotNull(analyzer);
            SearchCriteria searchInput = new SearchCriteria(null);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, Indexer.Content);
        });
    }

    @Test
    public void createFieldQueryTestWithInvalidFieldOperatorTest1() throws  Exception
    {
        assertThrows(IllegalArgumentException.class, () -> {
            assertNotNull(analyzer);
            SearchCriteria searchInput = new SearchCriteria("Name: A10");
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, Indexer.Content);
        });
    }

    @Test
    public void createFieldQueryTestWithInvalidFieldOperatorTest2() throws  Exception
    {
        assertThrows(IllegalArgumentException.class, () -> {
            assertNotNull(analyzer);
            SearchCriteria searchInput = new SearchCriteria("Name :A10");
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, Indexer.Content);
        });
    }

}
