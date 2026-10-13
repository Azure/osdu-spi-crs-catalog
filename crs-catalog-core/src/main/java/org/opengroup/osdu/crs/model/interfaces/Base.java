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
package org.opengroup.osdu.crs.model.interfaces;

import org.opengroup.osdu.crs.model.search.NamedReference;

import java.util.List;

/**
 *  Base is a base interface which encapsulates the common features of a derived class
 *  such as an area of use, a coordinate reference system, or a cartographic transform.
 */
public interface Base extends Root{

    /**
     * Get the {@link NamedReference} object; can be null if only {@link Essence} is populated
     * @return the {@link NamedReference} object
     */
    public NamedReference getNamedReference();

    /**
     * Get the {@link Essence} object; can be null if only {@link NamedReference} is populated
     * @return {@link Essence}
     */
    public Essence getEssence();

    /**
     * Get the description of the object
     * @return the description of the object
     */
    public String getDescription();

    /**
     * Get a short string representation of the last modified date in YYYYMMDD format
     * @return the last modified date in YYYYMMDD format
     */
    public String getLastModified();

    /**
     * Get the alias names of the object
     * @return the alias names of the object
     */
    public List<String> getAliasNames();

    /**
     * Get the {@link DeprecationInfo} object
     * @return the {@link DeprecationInfo} object
     */
    public DeprecationInfo getDeprecationInfo();

}

