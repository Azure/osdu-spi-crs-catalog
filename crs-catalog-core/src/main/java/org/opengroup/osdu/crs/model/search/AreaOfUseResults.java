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
import org.opengroup.osdu.crs.model.interfaces.AreaOfUse;

/**
 *  An object to hold the list of {@link AreaOfUse} objects from a search
 */
@Schema(description = "Object to hold the list of `AreaOfUse` objects from a search")
public class AreaOfUseResults extends SearchResults {
    @Schema(description = "A response containing a list of areas of use")
	private List<AreaOfUse>	items;

    /**
     * Constructor
     */
    public AreaOfUseResults() {
    	super();
    	items = new ArrayList<>();
    }

    /**
     * Constructor
     * @param items list of {@link AreaOfUse} objects
     */
    public AreaOfUseResults(List<AreaOfUse> items) {
        this(items, 0, items.size());
    }

    /**
     * Constructor
     * @param items list of {@link AreaOfUse} objects
     * @param offset offset index from beginning
     * @param totalCount total number of items in the query
     */

    public AreaOfUseResults(List<AreaOfUse> items, int offset, int totalCount) {
    	super(offset, items.size(), totalCount);
    	this.items = items;
    }

    /**
     * get the list of {@link AreaOfUse} objects
     * @return {@link AreaOfUse} objects
     */
    public List<AreaOfUse> getAreasOfUse() { return items;}
}
