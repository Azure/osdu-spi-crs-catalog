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

package org.opengroup.osdu.crs.model;

import org.opengroup.osdu.crs.model.interfaces.CatalogAttributes;

/**
 * Created by SYang on 8/4/2016.
 */
public class CatalogAttributesImpl implements CatalogAttributes {

    private String name;
    private String description;
    private String lastModified;

    /**
     * Constructor
     *
     * @param name catalog name
     * @param description catalog description
     * @param lastModified catalog lastModified
     */
    public CatalogAttributesImpl(String name, String description, String lastModified) {
        this.name = name;
        this.description = description;
        this.lastModified = lastModified;
    }

    /**
     * Get the catalog name.
     * @return catalog name
     */
    public String getName() { return name; }

    /**
     * Get the catalog description.
     * @return catalog description
     */
    public String getDescription() { return description; }

    /**
     * Get a string representation of the last modified date
     * @return the last modified date in string format
     */
    public String getLastModified() {return lastModified; }

}
