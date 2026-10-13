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

import org.opengroup.osdu.crs.model.interfaces.LateBoundCRS;
import org.opengroup.osdu.crs.model.interfaces.LateBoundCRSEssence;
import org.opengroup.osdu.crs.model.interfaces.Essence;
import org.opengroup.osdu.crs.model.interfaces.RepresentationMode;
import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 *  LateBoundCRSImpl is a child class of the CRSImpl class
 *  which encapsulate a late bound coordinate reference system. It needs to be associated with
 *  a {@link CTImpl} before a coordinate transformation operation can be performed.
 */
public class LateBoundCRSImpl extends CRSImpl implements LateBoundCRS {

    @Expose @SerializedName("essence")
    private LateBoundCRSEssenceImpl lbCRSEssence;

	/**
	 * Constructor
	 */
    public LateBoundCRSImpl(){
    	super();
    	lbCRSEssence = new LateBoundCRSEssenceImpl();
    }
    /*
     * Copy constructor
     */
    public LateBoundCRSImpl(LateBoundCRSImpl other) {
    	super(other);
    	lbCRSEssence = other.lbCRSEssence;
	}

	/**
	 * set the essence of the {@link LateBoundCRS}.
	 * @param essence {@link LateBoundCRSEssence}.
	 */
	public void setEssence(LateBoundCRSEssence essence) {
		this.lbCRSEssence = (LateBoundCRSEssenceImpl)essence;
	}

	/**
	 * get the essence of the {@link LateBoundCRS}.
	 * @return {@link Essence}.
	 */
	@Override
	public Essence getEssence() {
		return lbCRSEssence;
	}

	/**
	 * get the essence of the {@link LateBoundCRS}.
	 * @return {@link LateBoundCRSEssence}.
	 */
	@JsonIgnore
	@Override
	public LateBoundCRSEssence getLateBoundCRSEssence() {
		return lbCRSEssence;
	}

	/**
	 * get the string representation the LateBoundCRSImpl object
	 * @return the string representation of the LateBoundCRSImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        if (getEssence() == null) sb.append(getNamedReference().getName());
        else sb.append(getEssence().toString());
        
        return sb.toString();
	}

	/**
	 *
	 * @param mode {@link RepresentationMode}
	 * @return a temporary {@link LateBoundCRS} copy populated depending on the requested mode
	 */
	public LateBoundCRS convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		LateBoundCRSImpl copy = new LateBoundCRSImpl(this);
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
				mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
			NamedReference namedRef = new NamedReference(this.lbCRSEssence.getName(),
					this.lbCRSEssence.toJsonString());
			copy.setNamedReference(namedRef);
		}
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
			copy.setEssence(null);
		}
		return copy;
	}
	/**
	 * check for equality of this LateBoundCRSImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof LateBoundCRSImpl))
			return false;

		LateBoundCRSImpl otherLateBoundCRS = (LateBoundCRSImpl)other;
		if (lbCRSEssence == null) return getNamedReference().getPersistableReference().equals(otherLateBoundCRS.getNamedReference().getPersistableReference());
		return lbCRSEssence.equals(otherLateBoundCRS.lbCRSEssence);
	}

	/**
	 * get the hash code of this LateBoundCRSImpl object
	 * @return the hash code of this LateBoundCRSImpl object
	 */
	@Override
	public int hashCode() {
		if (getEssence() != null) return lbCRSEssence.hashCode();
		else return getNamedReference().hashCode();
	}

	/*
     * create an {@link LateBoundCRSImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link LateBoundCRSImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static LateBoundCRSImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, LateBoundCRSImpl.class);
	 }

}

