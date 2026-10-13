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

/*
 * Copyright 2018 Schlumberger. All Rights Reserved.
 */
package org.opengroup.osdu.crs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.CRSCTEssence;

/**
 *  EssenceImpl is a base class which encapsulates the essence of a coordinate reference system
 *  or cartographic transform
 */
public abstract class CRSCTEssenceImpl extends EssenceImpl implements CRSCTEssence {

	@Expose @SerializedName("ver")
	@JsonProperty("ver")
    private String engineVersion;

	@Expose @SerializedName("name")
	@JsonProperty("name")
    private String name;

	/**
	 * Constructor
	 */
    public CRSCTEssenceImpl() {
        super();
		engineVersion = "";
		name = "";
    }

	/**
	 * get the name.
	 * @return the name.
	 */
	@JsonProperty("name")	
	public String getName() {
		return name;
	}

	/**
	 * get the engine version.
	 * @return the engine version.
	 */
	@JsonProperty("ver")	
	public String getEngineVersion() {
		return engineVersion;
	}

	/**
	 * Gets the string representation the EssenceImpl object
	 * @return the string representation of the EssenceImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getName());
        sb.append(" ");
        sb.append(getEngineVersion());
        sb.append(" ");
        return sb.toString();
	}

	/**
	 * check for equality of this EssenceImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {

        if(!super.equals(other))
            return false;

		if(!(other instanceof CRSCTEssenceImpl))
			return false;

		CRSCTEssenceImpl otherEssenceImpl = (CRSCTEssenceImpl)other;
        return getName().equals(otherEssenceImpl.getName()) &&
        	   getEngineVersion().equals(otherEssenceImpl.getEngineVersion());
	}

	/**
	 * gets the hash code of this EssenceImpl object
	 * @return the hash code of this EssenceImpl object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash += 13 * (getName() == null? 0 : getName().hashCode());
		hash += 17 * (getEngineVersion() == null? 0 : getEngineVersion().hashCode());
		return hash;
	}
}

