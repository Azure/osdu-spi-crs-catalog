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
 */
package org.opengroup.osdu.crs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.search.NamedReference;


/**
 *  CompoundCRSImpl is a child class of the CRSImpl class. It is a compound
 *  coordinate reference system consisting of both a horizontal coordinate reference system and a vertical
 *  coordinate reference system.
 */
public class CompoundCRSImpl extends CRSImpl implements CompoundCRS {


    @Expose @SerializedName("essence")
    private CompoundCRSEssenceImpl compoundCRSEssence;

    @JsonIgnore
	private CRSImpl horizontalCRS;
	@JsonIgnore
    private CRSImpl verticalCRS;

    /**
     * Constructor
     */
    public CompoundCRSImpl()
    {
    	super();
    	this.horizontalCRS = null;
    	this.verticalCRS = null;
        this.compoundCRSEssence = new CompoundCRSEssenceImpl();
    }

	/**
	 * Copy constructor
	 * @param other {@link CompoundCRSImpl} instance to copy
	 */
    public CompoundCRSImpl(CompoundCRSImpl other){
    	super(other);
    	this.horizontalCRS = other.horizontalCRS;
    	this.verticalCRS = other.verticalCRS;
    	this.compoundCRSEssence = other.compoundCRSEssence;
	}
    /**
     * set the essence of the {@link CompoundCRS}.
     * @param essence {@link CompoundCRSEssence}.
     */
	public void setEssence(CompoundCRSEssence essence) {
		this.compoundCRSEssence = (CompoundCRSEssenceImpl)essence;
	}

    /**
     * get the essence of the {@link CompoundCRS}.
     * @return {@link Essence}.
     */
    @Override
	public Essence getEssence() {
		return this.compoundCRSEssence;
	}

    /**
     * get the essence of the {@link CompoundCRS}.
     * @return {@link CompoundCRSEssence}.
     */
	@JsonIgnore
    @Override
    public CompoundCRSEssence getCompoundCRSEssence() {
        return (CompoundCRSEssence)getEssence();
    }

	/**
	 * get the string representation the CompoundCRSImpl object
	 * @return the string representation of the CompoundCRSImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        if (this.getEssence() == null) sb.append(this.getNamedReference().getPersistableReference());
		else sb.append(getEssence().toString());
        
        return sb.toString();
	}
	/**
	 * Create a copy prepared according to requested representation mode
	 * @param mode The requested {@link RepresentationMode}
	 * @return a temporary {@link CompoundCRS} copy populated depending on the requested mode
	 */
	public CompoundCRS convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		CompoundCRSImpl copy = new CompoundCRSImpl(this);
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
				mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
			NamedReference namedRef = new NamedReference(getCompoundCRSEssence().getName(),
					getCompoundCRSEssence().toJsonString());
			copy.setNamedReference(namedRef);
		}
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
			copy.setEssence(null);
		}
		return copy;
	}

    /**
     * get the horizontal coordinate reference system's {@link AuthorityCode}
     * @return the horizontal coordinate reference system's {@link AuthorityCode}
     */
    @JsonIgnore
	public AuthorityCode getHorizontalCRSAuthorityCode() {
		return getCompoundCRSEssence().getHorizontalCRSAuthorityCode();
	}

    /**
     * set the horizontal coordinate reference system.
     * @param horizontalCRS {@link CRSImpl}
     */
	public void setHorizontalCRS(CRSImpl horizontalCRS) {
		this.horizontalCRS = horizontalCRS;
		if (this.compoundCRSEssence != null) this.compoundCRSEssence.setHorizontalCRS(horizontalCRS.getEssence());
	}

	/**
	 * get the vertical coordinate reference system's {@link AuthorityCode}
	 * @return the vertical coordinate reference system's {@link AuthorityCode}
	 */
	@JsonIgnore
	public AuthorityCode getVerticalCRSAuthorityCode() {
	    return getCompoundCRSEssence().getVerticalCRSAuthorityCode();
	}

    /**
     * set the vertical coordinate reference system.
     * @param verticalCRS {@link CRSImpl}
     */
	public void setVerticalCRS(CRSImpl verticalCRS) {
		this.verticalCRS = verticalCRS;
		if (this.compoundCRSEssence != null) this.compoundCRSEssence.setVerticalCRS(verticalCRS.getEssence());
	}

	/**
	 * check for equality of this CompoundCRSImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof CompoundCRSImpl))
			return false;

		CompoundCRSImpl otherCompoundCRSImpl = (CompoundCRSImpl)other;
		return getCompoundCRSEssence().equals(otherCompoundCRSImpl.getCompoundCRSEssence());
	}

	/**
	 * get the hash code of this CompoundCRSImpl object
	 * @return the hash code of this CompoundCRSImpl object
	 */
	@Override
	public int hashCode() {
		if (getEssence() != null) return getCompoundCRSEssence().hashCode();
		else return getNamedReference().hashCode();
	}

	/*
     * create an {@link CompoundCRSImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CompoundCRSImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static CompoundCRSImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, CompoundCRSImpl.class);
	 }
}

