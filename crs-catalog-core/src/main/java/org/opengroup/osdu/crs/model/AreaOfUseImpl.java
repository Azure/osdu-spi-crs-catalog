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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 *  AreaOfUseImpl is a class which encapsulates an area of use for a coordinate reference system
 *  or a cartographic transform. The area of use is specified by an {@link AuthorityCodeImpl} and
 *  a {@link SphericalBoundingBoxImpl} as well as other properties and information.
 */
public class AreaOfUseImpl extends BaseImpl implements AreaOfUse {

    @Expose @SerializedName("name")
    private String name;

    @Expose @SerializedName("essence")
    private AreaOfUseEssenceImpl essence;

    private AreaOfUseDeprecationInfoImpl deprecationInfoImpl;

    /**
     * Constructor
     */
    public AreaOfUseImpl()
    {
        super();
        essence = new AreaOfUseEssenceImpl();
    }

    /**
     *  Copy constructor
     * @param other The instance of {@link AreaOfUseImpl} to copy from
     */
    public AreaOfUseImpl(AreaOfUseImpl other) {
        super(other);
        this.name = other.name;
        this.essence = other.essence;
        this.deprecationInfoImpl = other.deprecationInfoImpl;
    }
    /**
     * Set the {@link AreaOfUseEssence} object
     * @param essence the {@link AreaOfUseEssence} object
     */
    public void setEssence(AreaOfUseEssence essence) {
        this.essence = (AreaOfUseEssenceImpl)essence;
    }

    /**
     * Get the {@link Essence} object
     * @return {@link Essence}
     */
    @JsonIgnore
    @Override
    public Essence getEssence() {
        return essence;
    }

    /**
     * Get the {@link AreaOfUseEssence} object
     * @return {@link AreaOfUseEssence}
     */
    @JsonProperty("essence")
    public AreaOfUseEssence getAreaOfUseEssence() {
        return essence;
    }

    /**
     * Get the name of the {@link AreaOfUseImpl}
     * @return the name of the {@link AreaOfUseImpl}
     */
    public String getName() {
        return name;
    }

    /**
     * Set the {@link AreaOfUseDeprecationInfoImpl} object
     * @param deprecationInfo the {@link AreaOfUseDeprecationInfoImpl} object
     */
    public void setDeprecationInfo(AreaOfUseDeprecationInfoImpl deprecationInfo) {
        this.deprecationInfoImpl = deprecationInfo;
    }

    /**
     * Get the {@link AreaOfUseDeprecationInfo} object
     * @return the {@link AreaOfUseDeprecationInfo} object
     */
    @JsonIgnore
    @Override
    public AreaOfUseDeprecationInfo getAreaOfUseDeprecationInfo() {
        return this.deprecationInfoImpl;
    }

    /**
     * Get the {@link AreaOfUseDeprecationInfo} object
     * @return the {@link AreaOfUseDeprecationInfo} object
     */
    @Override
    public DeprecationInfo getDeprecationInfo() {
        return this.deprecationInfoImpl;
    }

    /**
     *
     * @param mode {@link RepresentationMode}
     * @return a temporary {@link LateBoundCRS} copy populated depending on the requested mode
     */
    public AreaOfUse convert(RepresentationMode mode) {
        if (mode == RepresentationMode.ESSENCE) return this;
        AreaOfUseImpl copy = new AreaOfUseImpl(this);
        if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
                mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
            NamedReference namedRef = new NamedReference(this.getName(),
                    this.essence.toJsonString());
            copy.setNamedReference(namedRef);
        }
        if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
            copy.setEssence(null);
        }
        return copy;
    }
    /**
     * Get the string representation the {@link AreaOfUseImpl} object
     * @return the string representation of the {@link AreaOfUseImpl} object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getName());
        sb.append(" ");
        if (this.essence != null) sb.append(essence.toString());
        else sb.append(this.getNamedReference().getPersistableReference());
        return sb.toString();
    }

    /**
     * check for equality of this {@link AreaOfUseImpl} object with the other object
     * @return true of they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {

        if(!(other instanceof AreaOfUseImpl))
            return false;

        AreaOfUseImpl otherAreaOfUseImpl = (AreaOfUseImpl)other;
        if (this.essence == null) return this.getNamedReference().equals(otherAreaOfUseImpl.getNamedReference());
        else  return essence.equals(otherAreaOfUseImpl.essence);
    }

    /**
     * get the hash code of this {@link AreaOfUseImpl} object
     * @return the hash code of this {@link AreaOfUseImpl} object
     */
    @Override
    public int hashCode() {
        if (this.essence != null) return essence.hashCode();
        else return getNamedReference().hashCode();
    }

	/*
     * create an {@link AreaOfUseImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link AreaOfUseImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static AreaOfUseImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, AreaOfUseImpl.class);
	 }

}

