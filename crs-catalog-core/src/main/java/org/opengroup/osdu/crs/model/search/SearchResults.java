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
package org.opengroup.osdu.crs.model.search;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  An abstract base class object to hold the search results
 */
@Schema(description = "Object to hold the list of `SearchResults` objects from a search")
public abstract class SearchResults {

    @Schema(description = "The offset of the first item in the list of all AreaOfUse. It is optional and is 0 by default.")
    private int offset;

    @Schema(description = "The maximum number of the AreaOfUse returned. It is optional and is 100 by default.")
    private int count;

    @Schema(description = "The total number of items matching the query.")
    private int totalCount;

    /**
     * Constructor
     */
    public SearchResults() {
        this.offset = 0;
        this.count = 0;
        this.totalCount = 0;
    }

    /**
     * Constructor
     * @param offset offset index of all the items in the query
     * @param count number of items in the result
     * @param totalCount total number of items in the query
     */
    public SearchResults(int offset, int count, int totalCount) {
    	this.offset = offset;
        this.count = count;
        this.totalCount = totalCount;
    }

    /**
     * get the total number of items in the query
     * @return total number of items in the query
     */
    public int getTotalCount() { return this.totalCount; }

    /**
     * get the number of items in the result
     * @return number of items in the result
     */
    public int getCount() { return this.count; }

    /**
     * get the offset index of all the items in the query
     * @return offset index of all the items in the query
     */
    public int getOffset() { return this.offset; }
}
