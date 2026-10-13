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

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.crs.model.interfaces.CRS;

/**
 *  An object to hold the list of {@link CRS} objects from a search
 */
@Schema(description = "A response containing a list of CRSes.")
public class CRSResults extends SearchResults {

    @Schema(description = "An array of (any sub-type of) CRS")
    private List<CRS>	items;

    /**
     * Constructor
     */
    public CRSResults() {
    	super();
    	items = new ArrayList<>();
    }

    /**
     * Constructor
     * @param items list of {@link CRS} objects
     */
    public CRSResults(List<CRS> items) {
        this(items, 0, items.size());
    }

    /**
     * Constructor
     * @param items list of {@link CRS} objects
     * @param offset offset index from beginning
     * @param totalCount total number of items in the query
     */
    public CRSResults(List<CRS> items, int offset, int totalCount) {
    	super(offset, items.size(), totalCount);
    	this.items = items;
    }

    /**
     * get the list of {@link CRS} objects
     * @return {@link CRS} objects
     */
    @Schema(description = "A response containing a list of CRSes.")
    public List<CRS> getCRSes() { return items;}
}
