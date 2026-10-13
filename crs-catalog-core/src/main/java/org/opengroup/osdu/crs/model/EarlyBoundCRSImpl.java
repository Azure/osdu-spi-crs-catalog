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
 *  EarlyBoundCRSImpl is a child class of the CRSImpl class. It encapsulates
 *  an early bound coordinate reference system, which has both a {@link LateBoundCRSImpl} and
 *  a {@link CTImpl} specified within so that it is ready for a coordinate transformation operation
 */
public class EarlyBoundCRSImpl extends CRSImpl implements EarlyBoundCRS {


    @Expose @SerializedName("essence")
    private EarlyBoundCRSEssenceImpl ebCRSEssence;

	/**
	 * Constructor
	 */
    public EarlyBoundCRSImpl() {
    	super();
		ebCRSEssence = new EarlyBoundCRSEssenceImpl();
    }
	/*
	 * Copy constructor
	 */
	public EarlyBoundCRSImpl(EarlyBoundCRSImpl other) {
		super(other);
		ebCRSEssence = other.ebCRSEssence;
	}

	/**
	 * set the essence of the {@link EarlyBoundCRS}.
	 * @param essence {@link EarlyBoundCRSEssence}.
	 */
	public void setEssence(EarlyBoundCRSEssence essence) {
		this.ebCRSEssence = (EarlyBoundCRSEssenceImpl)essence;
	}

	/**
	 * get the essence of the {@link EarlyBoundCRS}.
	 * @return {@link Essence}.
	 */
	@Override
	public Essence getEssence() {
		return ebCRSEssence;
	}

	/**
	 * get the essence of the {@link EarlyBoundCRS}.
	 * @return {@link EarlyBoundCRSEssence}.
	 */
	@JsonIgnore
	@Override
	public EarlyBoundCRSEssence getEarlyBoundCRSEssence() {
		return ebCRSEssence;
	}

	/**
	 * set the {@link LateBoundCRSImpl}.
	 * @param lbCRS {@link LateBoundCRSEssenceImpl}.
	 */
	public void setLateBoundCRS(LateBoundCRSImpl lbCRS) {
		this.ebCRSEssence.setLateBoundCRSEssence(lbCRS.getLateBoundCRSEssence());
	}

	/**
	 * get the {@link LateBoundCRSImpl}'s {@link AuthorityCode}.
	 * @return {@link LateBoundCRSEssenceImpl}'s {@link AuthorityCode}.
	 */
	@JsonIgnore
	public AuthorityCode getLateBoundCRSAuthorityCode() {
		return this.ebCRSEssence.getLateBoundCRSAuthorityCode();
	}

	/**
	 * set the {@link CTImpl}.
	 * @param trf {@link CTImpl}.
	 */
	public void setCT(CTImpl trf) {
		this.ebCRSEssence.setCT(trf);
	}

	/**
	 * get the {@link CT}'s {@link AuthorityCode}.
	 * @return  {@link CT}'s {@link AuthorityCode}.
	 */
	@JsonIgnore
	public AuthorityCode getCTAuthorityCode() {
		return this.ebCRSEssence.getCTAuthorityCode();
	}

	/**
	 * get the string representation the EarlyBoundCRSImpl object
	 * @return the string representation of the EarlyBoundCRSImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
		sb.append(super.toString());
		Essence	essence = getEssence();
		if(essence != null) {
			sb.append(" ");
			sb.append(essence.toString());
		}
        return sb.toString();
	}

	/**
	 *
	 * @param mode {@link RepresentationMode}
	 * @return a temporary {@link EarlyBoundCRS} copy populated depending on the requested mode
	 */
	public EarlyBoundCRS convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		EarlyBoundCRSImpl copy = new EarlyBoundCRSImpl(this);
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
				mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
			NamedReference namedRef = new NamedReference(this.ebCRSEssence.getName(),
					this.ebCRSEssence.toJsonString());
			copy.setNamedReference(namedRef);
		}
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
			copy.setEssence(null);
		}
		return copy;
	}

	/**
	 * check for equality of this EarlyBoundCRSImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof EarlyBoundCRSImpl))
			return false;

		EarlyBoundCRSImpl otherEarlyBoundCRS = (EarlyBoundCRSImpl)other;
		return ebCRSEssence.equals(otherEarlyBoundCRS.ebCRSEssence);
	}

	/**
	 * get the hash code of this EarlyBoundCRSImpl object
	 * @return the hash code of this EarlyBoundCRSImpl object
	 */
	@Override
	public int hashCode() {
		if (getEssence() != null) return ebCRSEssence.hashCode();
		else return getNamedReference().hashCode();
	}

	/*
     * create an {@link EarlyBoundCRSImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link EarlyBoundCRSImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static EarlyBoundCRSImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, EarlyBoundCRSImpl.class);
	 }
}

