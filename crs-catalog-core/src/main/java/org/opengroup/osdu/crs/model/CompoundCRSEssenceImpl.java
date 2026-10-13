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

import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.CompoundCRSEssence;
import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;
import org.opengroup.osdu.crs.model.interfaces.Essence;
import org.opengroup.osdu.crs.model.interfaces.CRSEssence;

/**
 *  CompoundCRSEssenceImpl is a class which encapsulates the essence of a compound
 *  coordinate reference system. It is a child class of the CRSImpl class
 */
public class CompoundCRSEssenceImpl extends CRSEssenceImpl implements CompoundCRSEssence{


	@Expose @SerializedName("horzLateBoundCRS")
	@JsonProperty("horzLateBoundCRS")
	private LateBoundCRSEssenceImpl horizontalLbCRSEssence;
	
	@Expose @SerializedName("vertLateBoundCRS")
	@JsonProperty("vertLateBoundCRS")
	private LateBoundCRSEssenceImpl verticalLbCRSEssence;

	@Expose @SerializedName("horzEarlyBoundCRS")
	@JsonProperty("horzEarlyBoundCRS")
	private EarlyBoundCRSEssenceImpl horizontalEbCRSEssence;

	@Expose @SerializedName("vertEarlyBoundCRS")
	@JsonProperty("vertEarlyBoundCRS")
	private EarlyBoundCRSEssenceImpl verticalEbCRSEssence;

    private CRSImpl horizontalCRS;

    private CRSImpl verticalCRS;

	/**
	 * Constructor
	 */
	public CompoundCRSEssenceImpl() {
		super();
		this.horizontalLbCRSEssence = null;
		this.horizontalEbCRSEssence = null;
		this.verticalEbCRSEssence = null;
		this.verticalLbCRSEssence = null;
	}

	/**
     * set the horizontal coordinate reference system essence.
     * @param horizontalCRS {@link CRSEssenceImpl}
     */
    public void setHorizontalCRS(Essence horizontalCRS) {
        if (horizontalCRS instanceof LateBoundCRSEssenceImpl impl) this.horizontalLbCRSEssence = impl;
        else if (horizontalCRS instanceof EarlyBoundCRSEssenceImpl impl) this.horizontalEbCRSEssence = impl;
    }

    /**
     * set the vertical coordinate reference system essence.
     * @param verticalCRS {@link CRSImpl}
     */
    public void setVerticalCRS(Essence verticalCRS) {
		if (verticalCRS instanceof LateBoundCRSEssenceImpl impl) this.verticalLbCRSEssence = impl;
		else if (verticalCRS instanceof EarlyBoundCRSEssenceImpl impl) this.verticalEbCRSEssence = impl;
    }

	/*
     * get the horizontal coordinate reference system essence
     * @return horizontal coordinate reference system essence
     */
	//@JsonProperty("horzLateBoundCRS")
	@JsonIgnore
    @Override
	public CRSEssence getHorizontalCRSEssence() {
		if (this.horizontalLbCRSEssence != null) return this.horizontalLbCRSEssence;
		else                                     return this.horizontalEbCRSEssence;
	}

	/*
     * get the vertical coordinate reference system essence
     * @return vertical coordinate reference system essence
     */
	//@JsonProperty("vertLateBoundCRS")
	@JsonIgnore
    @Override
	public CRSEssence getVerticalCRSEssence() {
		if (this.verticalLbCRSEssence != null) return this.verticalLbCRSEssence;
		else                                   return this.verticalEbCRSEssence;
	}


	/**
	 * get the horizontal coordinate reference system's {@link AuthorityCode}.
	 * @return the horizontal coordinate reference system's {@link AuthorityCode}
	 */
	@JsonIgnore
    @Override
	public AuthorityCode getHorizontalCRSAuthorityCode() {
        if(getHorizontalCRSEssence() != null) {
        	return getHorizontalCRSEssence().getAuthorityCode();
        }
        return null;
	}

	/**
	 * get the vertical coordinate reference system's {@link AuthorityCode}
	 * @return the vertical coordinate reference system's {@link AuthorityCode}
	 */
	@JsonIgnore
    @Override
	public AuthorityCode getVerticalCRSAuthorityCode() {
        if(getVerticalCRSEssence() != null) {
        	return getVerticalCRSEssence().getAuthorityCode();
        }
        return null;
	}

	/**
	 * get the string representation the CompoundCRSEssenceImpl object
	 * @return the string representation of the CompoundCRSEssenceImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();

		sb.append(super.toString());
		if(horizontalLbCRSEssence != null){
			sb.append(" ");
			sb.append(horizontalLbCRSEssence.toString());
		}
		if(verticalLbCRSEssence != null) {
			sb.append(" ");
			sb.append(verticalLbCRSEssence.toString());
		}
        return sb.toString();
	}

	/**
	 * check for equality of this CompoundCRSEssenceImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if((null == other) || !super.equals(other))
			return false;
			
		if(!(other instanceof CompoundCRSEssenceImpl))
			return false;

		CompoundCRSEssenceImpl otherCRSEssence = (CompoundCRSEssenceImpl)other;
		CRSEssence hCRSEssence = getHorizontalCRSEssence();
		CRSEssence hOtherCRSEssence = otherCRSEssence.getHorizontalCRSEssence();
		if((null != hCRSEssence) && (null == hOtherCRSEssence))
			return false;
		else if((null == hCRSEssence) && (null != hOtherCRSEssence))
			return false;
		else if((null != hCRSEssence) && (null != hOtherCRSEssence)) {
			if (!hCRSEssence.equals(hOtherCRSEssence))
				return false;
		}

		CRSEssence vCRSEssence = getVerticalCRSEssence();
		CRSEssence vOtherCRSEssence = otherCRSEssence.getVerticalCRSEssence();
		if((null != vCRSEssence) && (null == vOtherCRSEssence))
			return false;
		else if((null == vCRSEssence) && (null != vOtherCRSEssence))
			return false;
		else if((null != vCRSEssence) && (null != vOtherCRSEssence)) {
			if (!vCRSEssence.equals(vOtherCRSEssence))
				return false;
		}
        return true;
	}

	/**
	 * get the hash code of this CompoundCRSEssenceImpl object
	 * @return the hash code of this CompoundCRSEssenceImpl object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		try {
			CRSEssence hEssence = getHorizontalCRSEssence();
			hash += 31 * (hEssence == null? 0 : hEssence.hashCode());
			CRSEssence vEssence = getVerticalCRSEssence();
			hash += 37 * (vEssence == null? 0 : vEssence.hashCode());
		}
		catch (Exception ex) {
			Logger log = Logger.getLogger(CompoundCRSEssenceImpl.class.getName());
			log.log(Level.WARNING, ex.toString(), ex);
		}
		return hash;
	}

	/*
     * create an {@link CompoundCRSEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CompoundCRSEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static CompoundCRSEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, CompoundCRSEssenceImpl.class);
	}

}

