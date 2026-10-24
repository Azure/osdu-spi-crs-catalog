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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchQueryBuilderTest {

    @Test
    public void testBuildPhraseQueryNormalRecordId() {
        String recordId = "osdu:reference-data--CoordinateReferenceSystem:EPSG::4326";
        String query = SearchQueryBuilder.buildPhraseQuery("id", recordId);
        Assertions.assertEquals("id: \"osdu:reference-data--CoordinateReferenceSystem:EPSG::4326\"", query);
    }

    @Test
    public void testBuildPhraseQueryWithQuotes() {
        String input = "test\"value";
        String query = SearchQueryBuilder.buildPhraseQuery("data.Name", input);
        Assertions.assertEquals("data.Name: \"test\\\"value\"", query);
    }

    @Test
    public void testBuildPhraseQueryWithBackslashes() {
        String input = "test\\value";
        String query = SearchQueryBuilder.buildPhraseQuery("data.Name", input);
        Assertions.assertEquals("data.Name: \"test\\\\value\"", query);
    }

    @Test
    public void testBuildPhraseQueryWithWildcardAndOrOperators() {
        String input = "test* || other";
        String query = SearchQueryBuilder.buildPhraseQuery("data.Name", input);
        Assertions.assertEquals("data.Name: \"test* || other\"", query);
    }

    @Test
    public void testBuildPhraseQueryEscapesBackslashBeforeQuote() {
        String input = "test\\\"value";
        String query = SearchQueryBuilder.buildPhraseQuery("id", input);
        Assertions.assertEquals("id: \"test\\\\\\\"value\"", query);
    }

    @Test
    public void testBuildNegativePhraseQueryWithSpecialCharacters() {
        String input = "test\"value\\path";
        String query = SearchQueryBuilder.buildNegativePhraseQuery("data.Kind", input);
        Assertions.assertEquals("NOT data.Kind: \"test\\\"value\\\\path\"", query);
    }

    @Test
    public void testBuildMultiFieldPhraseQuery() {
        String input = "test\"value";
        String query = SearchQueryBuilder.buildMultiFieldPhraseQuery(input, "field1", "field2");
        Assertions.assertEquals("(field1: \"test\\\"value\") OR (field2: \"test\\\"value\")", query);
    }

    @Test
    public void testBuildPhraseQueryNull() {
        Assertions.assertNull(SearchQueryBuilder.buildPhraseQuery("id", null));
        Assertions.assertNull(SearchQueryBuilder.buildNegativePhraseQuery("id", null));
    }
}
