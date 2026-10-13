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
 * Copyright 2016 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.Base;
import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 *  BaseImple implements the base interface which encapsulates the common features of a derived class
 *  such as an area of use, a coordinate reference system, or a cartographic transform.
 */
public abstract class BaseImpl extends RootImpl implements Base {

    @Expose @SerializedName("id")
    private String id;

    @Expose @SerializedName("description")
    private String description;

    @Expose @SerializedName("lastModified")
    private String lastModified;

    @Expose @SerializedName("aliasNames")
    private List<String> aliasNames;

    @Expose @SerializedName("namedReference")
    private NamedReference namedReference;

    /**
     * Constructor
     */
    public BaseImpl() {
        super();
        aliasNames = new ArrayList<>();
    }

    /**
     * Copy Constructor
     */
    public BaseImpl(BaseImpl other) {
        super(); // there are no properties
        aliasNames = other.aliasNames;
        lastModified = other.lastModified;
        description = other.description;
        id = other.id;
        namedReference = other.namedReference;
    }


    /**
     * Get the description of the object
     * @return the description of the object
     */
    @Override
    public NamedReference getNamedReference() {
        return namedReference;
    }

    /**
     * Set the namedReference of the object
     * @param namedReference {@link NamedReference}.
     */
    public void setNamedReference(NamedReference namedReference) {
        this.namedReference = namedReference;
    }

    /**
     * Get the description of the object
     * @return the description of the object
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Get a short string representation of the last modified date in YYYYMMDD format
     * @return the last modified date in YYYYMMDD format
     */
    @Override
    public String getLastModified() {
        return lastModified;
    }


    /**
     * Get the internal ID of the object
     * @return the internal ID of the object
     */
    @JsonIgnore
    public String getId() {
        return this.id;
    }

    /**
     * Get the alias names of the object
     * @return the alias names of the object
     */
    @Override
    public List<String> getAliasNames() {
        return aliasNames;
    }

    /**
     * Get the string representation the object
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getId());
        sb.append(" ");
        sb.append(getDescription());
        if(getLastModified() != null) {
            sb.append(getLastModified());
            sb.append(" ");
        }
        List<String> aliasNames = getAliasNames();
        if(aliasNames != null) {
            for(int i = 0; i < aliasNames.size(); i++) {
                sb.append(aliasNames.get(i));
                sb.append(" ");
            }
        }
        if(getDeprecationInfo() != null) {
            sb.append(" ");
            sb.append(getDeprecationInfo().toString());
        }
        return sb.toString();
    }
}

