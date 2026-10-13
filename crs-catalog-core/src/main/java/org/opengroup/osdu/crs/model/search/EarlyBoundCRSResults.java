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
import org.opengroup.osdu.crs.model.interfaces.EarlyBoundCRS;

/**
 *  An object to hold the list of {@link EarlyBoundCRS} objects from a search
 */
@Schema(description = "A response containing a list of early-bound CRSes.")
public class EarlyBoundCRSResults extends SearchResults {

    @Schema(description = "An array of EarlyBoundCRS")
    private List<EarlyBoundCRS>	items;

    /**
     * Constructor
     */
    public EarlyBoundCRSResults() {
    	super();
    	items = new ArrayList<>();
    }

    /**
     * Constructor
     * @param items list of {@link EarlyBoundCRS} objects
     */
    public EarlyBoundCRSResults(List<EarlyBoundCRS> items) {
        this(items, 0, items.size());
    }

    /**
     * Constructor
     * @param items list of {@link EarlyBoundCRS} objects
     * @param offset offset index from beginning
     * @param totalCount total number of items in the query
     */
    public EarlyBoundCRSResults(List<EarlyBoundCRS> items, int offset, int totalCount) {
    	super(offset, items.size(), totalCount);
    	this.items = items;
    }

    /**
     * get the list of {@link EarlyBoundCRS} objects
     * @return {@link EarlyBoundCRS} objects
     */
    @Schema(description = "The EarlyBoundCRS class.")
    public List<EarlyBoundCRS> getCRSes() { return items;}
}
