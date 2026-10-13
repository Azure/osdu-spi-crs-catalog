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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.CRSDeprecationInfo;
import org.opengroup.osdu.crs.model.interfaces.CRS;

/**
 *  CRSDeprecationInfoImpl is a class which encapsulates a coordinate reference system's deprecation information.
 *  It is a child of the DeprecationInfoImpl class
 */
public class CRSDeprecationInfoImpl extends DeprecationInfoImpl implements CRSDeprecationInfo{

    @Expose @SerializedName("supersededByCRSId")
    private String supersededByCRSId;
    
    private CRSImpl supersededByCRS;

	/**
	 * Constructor
	 */
    public CRSDeprecationInfoImpl() {
    }

	/**
	 * Constructor
	 * @param deprecationState the deprecation state
	 * @param remarks the deprecation remarks
	 * @param supersededByCRSId the coordinate reference system ID which supersedes the current one
	 */
    public CRSDeprecationInfoImpl(String deprecationState,
                                  String remarks,
                                  String supersededByCRSId) {
		super(deprecationState, remarks);
    	this.supersededByCRSId = supersededByCRSId;
    }

	/**
	 * get {@link CRSImpl} ID which the supersedes the current one
	 * @return the {@link CRSImpl} ID which the supersedes the current one
	 */
	@JsonIgnore
	public String getSupersededByCRSId() {
		return supersededByCRSId;
	}

	/**
	 * get {@link CRSImpl} which the supersedes the current one
	 * @return the {@link CRSImpl} which the supersedes the current one
	 */
	public CRS getSupersededByCRS() {
		return supersededByCRS;
	}

	/**
	 * set {@link CRSImpl} which the supersedes the current one
	 * @param supersededByCRS the coordinate reference system which supersedes the current one
	 */
	public void setSupersededByCRS(CRSImpl supersededByCRS) {
		this.supersededByCRS = supersededByCRS;
	}

	/**
	 * Get the string representation the {@link CRSDeprecationInfoImpl} object
	 * @return the string representation of the {@link CRSDeprecationInfoImpl} object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getSupersededByCRSId());
        
        return sb.toString();
	}
}
