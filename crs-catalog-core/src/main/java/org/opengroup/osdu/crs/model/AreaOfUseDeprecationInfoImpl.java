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

import org.opengroup.osdu.crs.model.interfaces.AreaOfUse;
import org.opengroup.osdu.crs.model.interfaces.AreaOfUseDeprecationInfo;

/**
 *  AreaOfUseDeprecationInfoImpl is a class which encapsulate an area of use's deprecation information.
 *  It is a child of the DeprecationInfoImpl class
 */
public class AreaOfUseDeprecationInfoImpl extends DeprecationInfoImpl implements AreaOfUseDeprecationInfo {

    @Expose @SerializedName("supersededByAouId")
    private String supersededByAouId;
    
    private AreaOfUseImpl supersededByArea;

	/**
	 * Constructor
	 */
    public AreaOfUseDeprecationInfoImpl() {
    }

	/**
	 * Constructor
	 * @param deprecationState the deprecation state
	 * @param remarks the deprecation remarks
	 * @param supersededByAouId the area of use ID which supersedes the current area of use
	 */
    public AreaOfUseDeprecationInfoImpl(String deprecationState,
                                        String remarks,
                                        String supersededByAouId) {
		super(deprecationState, remarks);
    	this.supersededByAouId = supersededByAouId;
    }

	/**
	 * get the area of use ID which supersedes the current area of use
	 * @return the area of use ID which supersedes the current area of use
	 */
	@JsonIgnore
	public String getSupersededByAouId() {
		return supersededByAouId;
	}

	/**
	 * get {@link AreaOfUse} which supersedes the current area of use
	 * @return {@link AreaOfUse} which supersedes the current area of use
	 */
	public AreaOfUse getSupersededByArea() {
		return supersededByArea;
	}

	/**
	 * set {@link AreaOfUseImpl} which the supersedes the current area of use
	 * @param supersededByArea the area of use which supersedes the current area of use
	 */
	public void setSupersededByArea(AreaOfUseImpl supersededByArea) {
		this.supersededByArea = supersededByArea;
	}

	/**
	 * Gets the string representation the {@link AreaOfUseDeprecationInfoImpl} object
	 * @return the string representation of the {@link AreaOfUseDeprecationInfoImpl} object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getSupersededByAouId());
        
        return sb.toString();
		
	}
}
