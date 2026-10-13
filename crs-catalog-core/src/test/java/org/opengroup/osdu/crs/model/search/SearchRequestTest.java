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

package org.opengroup.osdu.crs.model.search;

import org.junit.jupiter.api.Test;

import org.opengroup.osdu.crs.model.Utility;
import org.opengroup.osdu.crs.model.request.SearchRequest;

import static org.junit.jupiter.api.Assertions.*;

public class SearchRequestTest {
    @Test
    public void testSearchRequest() throws Exception{
        String query = "name:to-look-for";
        String json = "{\"query\": \"\"}";
        String empty = "{\"query\": null}";
        String totallyEmpty = "{}";
        SearchRequest request = Utility.fromJson(json, SearchRequest.class);
        assertNotNull(request);
        assertEquals("", request.getQuery());
        SearchRequest emptyRequest = Utility.fromJson(empty, SearchRequest.class);
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getQuery());
        SearchRequest totallyEmptyRequest = Utility.fromJson(totallyEmpty, SearchRequest.class);
        assertNotNull(totallyEmptyRequest);
        assertNull(totallyEmptyRequest.getQuery());
        SearchRequest nullRequest = new SearchRequest(null);
        assertNotNull(nullRequest);
        assertNull(nullRequest.getQuery());
        SearchRequest myRequest = new SearchRequest(query);
        assertNotNull(myRequest);
        assertNotNull(myRequest.getQuery());
        assertEquals(query, myRequest.getQuery());

    }
}
