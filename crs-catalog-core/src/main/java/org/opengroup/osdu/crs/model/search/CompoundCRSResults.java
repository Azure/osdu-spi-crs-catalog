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
import org.opengroup.osdu.crs.model.interfaces.CompoundCRS;

/**
 *  An object to hold the list of {@link CompoundCRS} objects from a search
 */
@Schema(description = "A response containing a list of compound CRSes.")
public class CompoundCRSResults extends SearchResults {

    @Schema(description = "An array of CompoundCRS")
    private List<CompoundCRS>	items;

    /**
     * Constructor
     */
    public CompoundCRSResults() {
    	super();
    	items = new ArrayList<>();
    }

    /**
     * Constructor
     * @param items list of {@link CompoundCRS} objects
     */
    public CompoundCRSResults(List<CompoundCRS> items) {
        this(items, 0, items.size());
    }

    /**
     * Constructor
     * @param items list of {@link CompoundCRS} objects
     * @param offset offset index from beginning
     * @param totalCount total number of items in the query
     */
    public CompoundCRSResults(List<CompoundCRS> items, int offset, int totalCount) {
    	super(offset, items.size(), totalCount);
    	this.items = items;
    }

    /**
     * get the list of {@link CompoundCRS} objects
     * @return {@link CompoundCRS} objects
     */
    @Schema(description = "List of CompoundCRS objects.")
    public List<CompoundCRS> getCRSes() { return items;}
}
