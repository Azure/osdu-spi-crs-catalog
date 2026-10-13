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


import org.opengroup.osdu.crs.model.SphericalBoundingBoxImpl;

public class SearchCriteria {
    private String keyword;
    private String type;
    private boolean isParentType;
    private SphericalBoundingBoxImpl boundingBox;

    public SearchCriteria(String keyword) {
        this(keyword, null);
    }

    public SearchCriteria(String keyword, String type) {
    	this(keyword, type, false);
    }

    public SearchCriteria(String keyword, String type, boolean isParentType) {
        this.keyword = keyword;
        this.type = type;
        this.isParentType = isParentType;
    }

    public String getKeyword() { return this.keyword; }
    public String getType() { return this.type; }
    public boolean isParentType() { return this.isParentType; }

    /**
     * Gets the bounding box used by search
     * @return bounding box
     */
    public SphericalBoundingBoxImpl getBoundingBox() { return this.boundingBox; }

    /**
     * Sets the bounding box used by search
     * @param boundingBox bounding box
     */
    public void setBoundingBox(SphericalBoundingBoxImpl boundingBox) { this.boundingBox = boundingBox; }

}
