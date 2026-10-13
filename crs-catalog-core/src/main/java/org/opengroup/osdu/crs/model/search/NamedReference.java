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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

/*
 * NamedReference is an interface definitions for a simple value container, a name and a persistable reference.
 */
@Schema(description = "Name and Persistable Reference (the essence converted to a JSON string).")
public class NamedReference  {
    @Expose
    @SerializedName("name")
    @Schema(description = "The name of the item.", type = "string")
    private String name;

    @Expose @SerializedName("persistableReference")
    @Schema(description = "The item 'essence' serialized into a JSON string..", type = "string")
    private String persistableReference;

    /**
     * Constructor
     */
    public NamedReference() {
    }
    /**
     * Constructor
     */
    public NamedReference(String name, String persistableReference) {
        this.name = name;
        this.persistableReference = persistableReference;
    }
    /**
     * Get the name of the object
     *
     * @return the name of the object
     */
    public String getName() {
        return name;
    }

    /**
     * Get the persistable reference string of the object
     *
     * @return the persistable reference stringof the object
     */
    public String getPersistableReference() {
        return persistableReference;
    }

    /**
     * check for equality of this NamedReference object with the other object
     * @return true of they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof NamedReference))
            return false;
        NamedReference nr = (NamedReference)other;
        return nr.hashCode() == this.hashCode();
    }

    /**
     * get the hash code of this {@link NamedReference} object
     * @return the hash code of this {@link NamedReference} object
     */
    @Override
    public int hashCode() {
        int hash = 31 * (getName() == null? 0 : this.getName().hashCode());
        hash += 31 * (getPersistableReference() == null? 0: this.getPersistableReference().hashCode());
        return hash;
    }
}
