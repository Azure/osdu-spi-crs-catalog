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
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.EarlyBoundCRSEssence;
import org.opengroup.osdu.crs.model.interfaces.LateBoundCRSEssence;
import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;
import org.opengroup.osdu.crs.model.interfaces.Essence;
import org.opengroup.osdu.crs.model.interfaces.CTEssence;

/**
 *  EarlyBoundCRSEssenceImpl is a class which encapsulates the essence of an early
 *  bound coordinate reference system. It is a child class of the CRSImpl class
 */
public class EarlyBoundCRSEssenceImpl extends CRSEssenceImpl implements EarlyBoundCRSEssence {

	@Expose @SerializedName("lateBoundCRS")
	@JsonProperty("lateBoundCRS")
	private LateBoundCRSEssenceImpl lbCRSEssence;
  
	@Expose @SerializedName("singleCT")
	@JsonProperty("singleCT")
	private SingleCTEssenceImpl sctEssence;

	@Expose @SerializedName("compoundCT")
	@JsonProperty("compoundCT")
	private CompoundCTEssenceImpl cctEssence;

	@JsonIgnore
	CTImpl ct;

	/**
	 * Constructor
	 */
    public EarlyBoundCRSEssenceImpl() {
    	super();
    }

	/**
	 * get the late bound coordinate teference system
	 * @return the late bound coordinate reference system as a persistable string
	 */
	@JsonProperty("lateBoundCRS")
	@Override
	public LateBoundCRSEssence getLateBoundCRSEssence() {
		return lbCRSEssence;
	}

	/**
	 * Set the late-bound CRS essence implementation
	 * @param lb_crs The late-bound CRS essence implementation
	 */
	@JsonIgnore
	public void setLateBoundCRSEssence(Essence lb_crs)
	{
		if (lb_crs instanceof LateBoundCRSEssenceImpl impl) this.lbCRSEssence = impl;
	}
	public void setCTEssence(EssenceImpl ct){
		if (ct instanceof SingleCTEssenceImpl impl) this.sctEssence = impl;
		else if (ct instanceof CompoundCTEssenceImpl impl) this.cctEssence = impl;
	}
	/**
	 * get the cartographic transform
	 * @return the cartographic transform as a persistable string
	 */
	@JsonIgnore
	@Override
	public CTEssence getCTEssence() {
		if (sctEssence == null) return cctEssence;
		else return sctEssence;
	}

	/**
	 * get the {@link LateBoundCRSEssenceImpl}'s {@link AuthorityCode}.
	 * @return {@link LateBoundCRSEssenceImpl}'s {@link AuthorityCode}.
	 */
	@JsonIgnore
	@Override
	public AuthorityCode getLateBoundCRSAuthorityCode() {
		if (getLateBoundCRSEssence() != null)
			return getLateBoundCRSEssence().getAuthorityCode();
		return null;
	}

	/**
	 * set the {@link CTImpl}.
	 * @param ct {@link CTImpl}.
	 */
	public void setCT(CTImpl ct) {
		this.ct = ct;
	}

	/**
	 * get the {@link CTImpl}'s {@link AuthorityCode}.
	 * @return  {@link CTImpl}'s {@link AuthorityCode}.
	 */
	@JsonIgnore
	@Override
	public AuthorityCode getCTAuthorityCode() {
		if(ct != null) {
		    Essence essence = ct.getEssence();
            if(essence != null)
                return essence.getAuthorityCode();
        }
        return null;
	}

	/**
	 * get the string representation the EarlyBoundCRSEssenceImpl object
	 * @return the string representation of the EarlyBoundCRSEssenceImpl object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(super.toString());
		if(lbCRSEssence != null) {
			sb.append(" ");
			sb.append(lbCRSEssence.toString());
		}
		if(sctEssence != null) {
			sb.append(" ");
			sb.append(sctEssence.toString());
		}
		if(cctEssence != null) {
			sb.append(" ");
			sb.append(cctEssence.toString());
		}
        return sb.toString();
	}

	/**
	 * check for equality of this EarlyBoundCRSEssenceImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if((null == other) || !super.equals(other))
			return false;

		if(!(other instanceof EarlyBoundCRSEssenceImpl))
			return false;

		EarlyBoundCRSEssenceImpl otherEarlyBoundCRSEssence = (EarlyBoundCRSEssenceImpl)other;
		LateBoundCRSEssence lateBoundCRSEssence = getLateBoundCRSEssence();
		LateBoundCRSEssence otherLateBoundCRSEssence = otherEarlyBoundCRSEssence.getLateBoundCRSEssence();
		if((lateBoundCRSEssence == null) && (otherLateBoundCRSEssence != null))
			return false;

		else if((lateBoundCRSEssence != null) && (otherLateBoundCRSEssence == null))
			return false;

		else if((lateBoundCRSEssence != null) && (otherLateBoundCRSEssence != null)) {
			if (!lateBoundCRSEssence.equals(otherLateBoundCRSEssence))
					return false;
		}

		CTEssence ctEssence = getCTEssence();
		CTEssence otherCTEssence = otherEarlyBoundCRSEssence.getCTEssence();
		if((ctEssence == null) && (otherCTEssence != null))
			return false;
		else if((ctEssence != null) && (otherCTEssence == null))
			return false;
		else if((ctEssence != null) && (otherCTEssence != null)) {
			if (!ctEssence.equals(otherCTEssence))
					return false;
		}

        return true;
	}

	/**
	 * get the hash code of this EarlyBoundCRSEssenceImpl object
	 * @return the hash code of this EarlyBoundCRSEssenceImpl object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		LateBoundCRSEssence crsEssence = getLateBoundCRSEssence();
		hash += 31 * (crsEssence == null ? 0 : crsEssence.hashCode());
		CTEssence ctEssence = getCTEssence();
		hash += 37 * (ctEssence == null ? 0 : ctEssence.hashCode());
		return hash;
	}

	/*
     * create an {@link EarlyBoundCRSEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link EarlyBoundCRSEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static EarlyBoundCRSEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, EarlyBoundCRSEssenceImpl.class);
	}

}

