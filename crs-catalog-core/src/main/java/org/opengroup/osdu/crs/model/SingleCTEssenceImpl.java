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
 *
 */
package org.opengroup.osdu.crs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.SingleCTEssence;

/**
 *  SingleCTEssenceImpl is a class which encapsulates the essence of a single
 *  cartographic transformation. It is a child class of the CTEssenceImpl class
 */
public class SingleCTEssenceImpl extends CTEssenceImpl implements SingleCTEssence {

	@Expose @SerializedName("wkt")
	@JsonProperty("wkt")
    private String wellKnownText;

	/**
	 * Constructor
	 */
    public SingleCTEssenceImpl() {
    	super();
		wellKnownText = "";
    }

	/**
	 * get the well known text
	 * @return the well known text string
	 */
	@JsonProperty("wkt")
	public String getWellKnownText() {
		return wellKnownText;
	}

	/**
	 * get the string representation the SingleCTEssenceImpl object
	 * @return the string representation of the SingleCTEssenceImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());
        sb.append(" ");
        sb.append(getWellKnownText());
        
        return sb.toString();
	}

	/**
	 * check for equality of this SingleCTEssenceImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!super.equals(other))
			return false;
		if(!(other instanceof SingleCTEssenceImpl))
			return false;
		SingleCTEssenceImpl otherCTEssence = (SingleCTEssenceImpl)other;
        return getWellKnownText().equals(otherCTEssence.getWellKnownText());
	}

	/**
	 * get the hash code of this SingleCTEssenceImpl object
	 * @return the hash code of this SingleCTEssenceImpl object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash += 31 * (getWellKnownText() == null? 0 : getWellKnownText().hashCode());
		return hash;
	}

	/*
     * create an {@link SingleCTEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link SingleCTEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static SingleCTEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, SingleCTEssenceImpl.class);
	}
 
 }

