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

import org.opengroup.osdu.crs.model.interfaces.CRSCTBase;
import org.opengroup.osdu.crs.model.interfaces.AreaOfUse;

/**
 *  An abstract CRSCTBase class which specifies a coordinate reference system or cartographic transform
 */
public abstract class CRSCTBaseImpl extends BaseImpl implements CRSCTBase {

    @Expose @SerializedName("source")
    private String source;

	@Expose @SerializedName("aouId")
    private String areaOfUseId;

    private AreaOfUseImpl areaOfUseImpl;

	/**
	 * Constructor
	 */
    public CRSCTBaseImpl()
    {
    	super();
    }
	/**
	 * Copy Constructor
	 */
	public CRSCTBaseImpl(CRSCTBaseImpl other)
	{
		super(other);
		source = other.source;
		areaOfUseId = other.areaOfUseId;
		areaOfUseImpl = other.areaOfUseImpl;
	}

    /**
     * get the coordinate reference system or cartographic transform source.
     * @return the coordinate reference system or cartographic transform source.
     */
    @Override
    public String getSource() {
        return source;
    }

	/**
	 * Get the {@link AreaOfUse}'s internal ID.
	 * @return the {@link AreaOfUse}'s internal ID.
	 */
	@JsonIgnore
	public String getAreaOfUseId() {
		return areaOfUseId;
	}

	/**
	 * Get the {@link AreaOfUse}.
	 * @return the {@link AreaOfUse}.
	 */
	public AreaOfUse getAreaOfUse() {
		return areaOfUseImpl;
	}

	/**
	 * set the {@link AreaOfUseImpl}.
	 * @param  areaOfUseImpl the {@link AreaOfUseImpl} object.
	 */
	public void setAreaOfUse(AreaOfUseImpl areaOfUseImpl) {
		this.areaOfUseImpl = areaOfUseImpl;
	}

	/**
	 * Gets the string representation the object
	 * @return the string representation of the object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" ");
        sb.append(getSource());
        sb.append(" ");
        sb.append(getAreaOfUseId());
        return sb.toString();
	}
}

