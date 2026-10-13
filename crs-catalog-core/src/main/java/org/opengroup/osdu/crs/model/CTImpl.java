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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.*;

/**
 *  An abstract base class which specifies a cartographic transformation.
 */
public abstract class CTImpl extends CRSCTBaseImpl implements CT{

    @Expose @SerializedName("fromCRSId")
    private String fromCRSId;

    @Expose @SerializedName("toCRSId")
    private String toCRSId;

    @Expose @SerializedName("deprecationInfo")
    private CTDeprecationInfoImpl deprecationInfoImpl;

	@JsonIgnore
    private CRSImpl fromCRS;

	@JsonIgnore
    private CRSImpl toCRS;


	/**
	 * Constructor
	 */
    public CTImpl()
    {
        super();
    }

	/**
	 * Copy constructor
	 * @param other as {@link CTImpl}
	 */
	public CTImpl(CTImpl other) {
    	super(other);
    	this.deprecationInfoImpl = other.deprecationInfoImpl;
    	this.fromCRS = other.fromCRS;
    	this.fromCRSId = other.fromCRSId;
    	this.toCRS = other.toCRS;
    	this.toCRSId = other.toCRSId;
	}

	/**
	 * Get the from {@link CRSImpl}'s internal ID.
	 * @return the from {@link CRSImpl}'s internal ID.
	 */
	@JsonIgnore
	public String getFromCRSId() {
		return fromCRSId;
	}

	/**
	 * Get the to {@link CRSImpl}'s internal ID.
	 * @return the to {@link CRSImpl}'s internal ID.
	 */
	@JsonIgnore
	public String getToCRSId() {
		return toCRSId;
	}

	/**
	 * set the Transform's Deprecation Information.
	 * @param  deprecationInfo the {@link CTDeprecationInfoImpl} object.
	 */
	public void setDeprecationInfo(CTDeprecationInfoImpl deprecationInfo) {

		this.deprecationInfoImpl = deprecationInfo;
	}

	/**
	 * get the Transform's Deprecation Information.
	 * @return  the {@link DeprecationInfo} object.
	 */
    @Override
	public DeprecationInfo getDeprecationInfo() {
		return deprecationInfoImpl;
	}

	/**
	 * get the Transform's Deprecation Information.
	 * @return  the {@link CTDeprecationInfo} object.
	 */
	@JsonIgnore
	@Override
	public CTDeprecationInfo getCTDeprecationInfo() {
		return deprecationInfoImpl;
	}

	/**
	 * Create a copy prepared according to requested representation mode
	 * @param mode The requested {@link RepresentationMode}
	 * @return a temporary {@link CT} copy populated depending on the requested mode
	 */
	public CT convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		if (this instanceof SingleCTImpl impl) return impl.convert(mode);
		else if (this instanceof CompoundCTImpl impl) return impl.convert(mode);
		return this;
	}

	/**
	 * get the from coordinate reference system's {@link AuthorityCode}
	 * @return  the from coordinate reference system's {@link AuthorityCode}
	 */
	public AuthorityCode getFromCRSAuthorityCode() {
	    if(fromCRS != null) {
	        Essence essence = fromCRS.getEssence();
            if(essence != null) {
                return essence.getAuthorityCode();
            }
        }
		return null;
	}

	/**
	 * set the from coordinate reference system.
	 * @param  fromCRS the from {@link CRSImpl} object.
	 */
	public void setFromCRS(CRSImpl fromCRS) {
		this.fromCRS = fromCRS;
	}

	/**
	 * get the to coordinate reference system's {@link AuthorityCode}.
	 * @return  the to coordinate reference system's {@link AuthorityCode}
	 */
	public AuthorityCode getToCRSAuthorityCode() {
        if(toCRS != null) {
            Essence essence = toCRS.getEssence();
            if(essence != null) {
                return essence.getAuthorityCode();
            }
        }
        return null;
	}

	/**
	 * set the to coordinate reference system.
	 * @param  toCRS the to {@link CRSImpl} object.
	 */
	public void setToCRS(CRSImpl toCRS) {
		this.toCRS = toCRS;
	}

	/**
	 * Gets the string representation the {@link CT} object
	 * @return the string representation of the {@link CT} object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getFromCRSId());
        sb.append(" ");
        sb.append(getToCRSId());
        return sb.toString();
	}
}

