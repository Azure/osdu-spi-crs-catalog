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

import java.util.ArrayList;
import java.util.List;

import org.opengroup.osdu.crs.model.interfaces.*;

/**
 *  CRSImpl is an abstract base class which encapsulates a coordinate reference system.
 */
public abstract class CRSImpl extends CRSCTBaseImpl implements CRS {

    @Expose @SerializedName("crsType")
    private String crsType;

    @Expose @SerializedName("baseCRSId")
    private String baseCRSId;

    @Expose @SerializedName("numberOfAxes")
    private int numberOfAxes;

    @Expose @SerializedName("axisUnits")
    private List<String> axisUnits;

    @Expose @SerializedName("readyToTransform")
    private boolean isTransformationReady;

    @Expose @SerializedName("deprecationInfo")
    private CRSDeprecationInfoImpl deprecationInfo;

    private CRSImpl baseCRS;

	/**
	 * Constructor
	 */
    public CRSImpl()
    {
    	super();
    	numberOfAxes = 0;
    	axisUnits = new ArrayList<>();
    }
	/**
	 * Copy Constructor
	 */
	public CRSImpl(CRSImpl other)
	{
		super(other);
		crsType = other.crsType;
		baseCRSId = other.baseCRSId;
		baseCRS = other.baseCRS;
		numberOfAxes = other.numberOfAxes;
		axisUnits = other.axisUnits;
		deprecationInfo = other.deprecationInfo;
		isTransformationReady = other.isTransformationReady;
	}

	/**
	 * get the base coordinate reference system's internal ID.
	 * @return the base coordinate reference system's internal ID.
	 */
	@JsonIgnore
	public String getBaseCRSId() {
	return baseCRSId;
	}

	/**
	 * get the base coordinate reference system's authority code.
	 * @return the base coordinate reference system's authority code.
	 */
	public AuthorityCode getBaseCRSAuthorityCode() {
		AuthorityCode authorityCode = null;
		if(baseCRS != null) {
		    Essence essence = baseCRS.getEssence();
            if(essence != null)
			    authorityCode = baseCRS.getEssence().getAuthorityCode();
		}
		return authorityCode;
	}

	/**
	 * set the base coordinate reference system.
	 * @param baseCRS {@link CRSImpl}
	 */
	public void setBaseCRS(CRSImpl baseCRS) {
		this.baseCRS = baseCRS;
	}

	/**
	 * get the number of axes.
	 * @return the number of axes.
	 */
	public int getNumberOfAxes() {
		return numberOfAxes;
	}

	/**
	 * get the axis units as unit persistable references.
	 * @return axis unit persistable references as list of strings.
	 */
	public List<String> getAxisUnits() {
		return axisUnits;
	}

	/**
	 * get the coordinate reference system type.
	 * @return the coordinate reference system type.
	 */
	public String getCRSType() {
		return crsType;
	}

	/**
	 * get the transformation ready status.
	 * @return true if ready for transformation; false otherwise.
	 */
	public boolean isTransformationReady() {
		return isTransformationReady;
	}

	/**
	 * set the deprecation information
	 * @param  deprecationInfo {@link CRSDeprecationInfoImpl}
	 */
    public void setDeprecationInfo(CRSDeprecationInfoImpl deprecationInfo) {
        this.deprecationInfo = deprecationInfo;
    }

	/**
	 * get the deprecation information
	 * @return  {@link DeprecationInfo}
	 */
	@Override
	public DeprecationInfo getDeprecationInfo() {
		return deprecationInfo;
	}

	/**
	 * get the deprecation information
	 * @return  {@link CRSDeprecationInfo}
	 */
	@JsonIgnore
	@Override
	public CRSDeprecationInfo getCRSDeprecationInfo() {
		return deprecationInfo;
	}

	/**
	 * Create a copy prepared according to requested representation mode
	 * @param mode The requested {@link RepresentationMode}
	 * @return a temporary {@link CRS} copy populated depending on the requested mode
	 */
	public CRS convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		if (this instanceof LateBoundCRSImpl impl) return impl.convert(mode);
		else if (this instanceof EarlyBoundCRSImpl impl) return impl.convert(mode);
		else if (this instanceof CompoundCRSImpl impl) return impl.convert(mode);
		return this;
	}
	/**
	 * Gets the string representation the CRSImpl object
	 * @return the string representation of the CRSImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getCRSType());
        sb.append(" ");
        sb.append(getBaseCRSId());
        sb.append(" ");
        sb.append(isTransformationReady());
        sb.append(" ");
        return sb.toString();
	}
}

