// Copyright © Amazon
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http:#www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchQueryBuilder {
    private List<String> queries;

    SearchQueryBuilder() {
        queries = new ArrayList<>();
    }

    public static String buildPhraseQuery(String recordBodyAttrName, Object attribute) {
        if (attribute == null) return null;
        return "%s: \"%s\"".formatted(recordBodyAttrName, attribute);
    }

    public void addPhraseQuery(String recordBodyAttrName, Object attribute) {
        addQuery(buildPhraseQuery(recordBodyAttrName, attribute));
    }
    public static String buildNegativePhraseQuery(String recordBodyAttrName, Object attribute) {
        if (attribute == null) return null;
        return "NOT %s: \"%s\"".formatted(recordBodyAttrName, attribute);
    }

    public void addNegativePhraseQuery(String recordBodyAttrName, Object attribute) {
        addQuery(buildNegativePhraseQuery(recordBodyAttrName, attribute));
    }

    public static String buildMultiFieldPhraseQuery(Object attribute, String... recordBodyAttrNames) {
        if (attribute == null) return null;
        SearchQueryBuilder builder = new SearchQueryBuilder();
        for (String recordBodyAttrName : recordBodyAttrNames) {
            builder.addPhraseQuery(recordBodyAttrName, attribute);
        }
        return builder.connectQuery("OR");
    }

    void addMultiFieldPhraseQuery(Object attribute, String... recordBodyAttrNames) {
        addQuery(buildMultiFieldPhraseQuery(attribute, recordBodyAttrNames));
    }

    public void addQuery(String query) {
        if (query == null || query.isEmpty()) return;
        queries.add(query);
    }

    public String connectQuery(String connector) {
        return connectMultipleQueries(connector, queries);
    }

    public static String connectMultipleQueries(String connector, String... queriesToConnect) {
        return connectMultipleQueries(connector, Arrays.asList(queriesToConnect));
    }

    public static String connectMultipleQueries(String connector, List<String> queriesToConnect) {
        connector = " %s ".formatted(connector);
        StringBuilder builder = new StringBuilder();
        boolean firstItem = true;
        for (String query : queriesToConnect) {
            if (firstItem) {
                firstItem = false;
            } else {
                builder.append(connector);
            }
            builder.append("(%s)".formatted(query));
        }
        return builder.toString();
    }
}