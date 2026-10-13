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
package org.opengroup.osdu.crs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.opengroup.osdu.crs.model.interfaces.AreaOfUseEssence;
import org.opengroup.osdu.crs.model.interfaces.SphericalBoundingBox;


/**
 *  AreaOfUseEssenceImpl is a class which encapsulates the essence of an area of use.
 */
public class AreaOfUseEssenceImpl extends EssenceImpl implements AreaOfUseEssence {

    @Expose @SerializedName("boundBox")
    private SphericalBoundingBoxImpl boundingBox;

    @Expose @SerializedName("name")
	private String name;
	/**
	 * Constructor
	 */
    public AreaOfUseEssenceImpl()
    {
    	super();
    	this.boundingBox = new SphericalBoundingBoxImpl();
    }

	/**
	 * get the {@link SphericalBoundingBox} property
	 * @return the {@link SphericalBoundingBox} property object
	 */
	@JsonProperty("boundBox")
	public SphericalBoundingBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Gets the string representation the {@link AreaOfUseEssenceImpl} object
	 * @return the string representation of the {@link AreaOfUseEssenceImpl} object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(" ");
		sb.append(getBoundingBox().toString());
		return sb.toString();
	}

	public String getName(){
		return this.name;
	}
	public void setName(String name) {
		this.name=name;
	}
	/**
	 * check for equality of this {@link AreaOfUseEssence} object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {

		if(!super.equals(other))
			return false;

		if(!(other instanceof AreaOfUseEssence))
			return false;

		AreaOfUseEssence otherAreaOfUseEssence = (AreaOfUseEssence)other;
		return getBoundingBox().equals(otherAreaOfUseEssence.getBoundingBox());
	}

	/**
	 * get the hash code of this {@link AreaOfUseEssenceImpl} object
	 * @return the hash code of this {@link AreaOfUseEssenceImpl} object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash += 31 * boundingBox.hashCode();
		return hash;
	}

	/*
     * create an {@link AreaOfUseEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link AreaOfUseEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static AreaOfUseEssenceImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, AreaOfUseEssenceImpl.class);
	 }

}

