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

import org.opengroup.osdu.crs.model.interfaces.CTDeprecationInfo;
import org.opengroup.osdu.crs.model.interfaces.CT;

/**
 *  CTDeprecationInfoImpl is a class which encapsulates a cartographic transformation's deprecation information.
 *  It is a child of the DeprecationInfoImpl class
 */
public class CTDeprecationInfoImpl extends DeprecationInfoImpl implements CTDeprecationInfo{

    @Expose @SerializedName("supersededByCTId")
    private String supersededByCTId;
    
    private CTImpl supersededByCT;

	/**
	 * Constructor
	 */
    public CTDeprecationInfoImpl() {
    }

	/**
	 * Constructor
	 * @param deprecationState the deprecation state
	 * @param remarks the deprecation remarks
	 * @param supersededByCTId the cartographic transform ID which the supersedes the current one
	 */
    public CTDeprecationInfoImpl(String deprecationState,
								 String remarks,
								 String supersededByCTId) {
		super(deprecationState, remarks);
    	this.supersededByCTId = supersededByCTId;
   }

	/**
	 * get {@link CTImpl} ID which the supersedes the current one
	 * @return the {@link CTImpl} ID which the supersedes the current one
	 */
	@JsonIgnore
	public String getSupersededByCTId() {
		return supersededByCTId;
	}

	/**
	 * get {@link CTImpl} which the supersedes the current one
	 * @return the {@link CTImpl} which the supersedes the current one
	 */
	public CT getSupersededByCT() {
		return supersededByCT;
	}

	/**
	 * set {@link CTImpl} which the supersedes the current one
	 * @param supersededByCT the cartographic transform which the supersedes the current one
	 */
	public void setSupersededByCT(CTImpl supersededByCT) {
		this.supersededByCT = supersededByCT;
	}

	/**
	 * Gets the string representation the {@link CTDeprecationInfoImpl} object
	 * @return the string representation of the {@link CTDeprecationInfoImpl} object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getSupersededByCTId());
        
        return sb.toString();
	}
}
