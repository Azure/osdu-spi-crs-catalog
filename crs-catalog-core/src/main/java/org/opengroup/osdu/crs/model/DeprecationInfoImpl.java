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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.DeprecationInfo;

/**
 *  DeprecationInfoImpl is a class which encapsulates a set of deprecation information properties
 */
public class DeprecationInfoImpl extends RootImpl implements DeprecationInfo {

    public final static String UNRESOLVED_STATE = "Unresolved";
    public final static String UNRESOLVED_REMARKS = "The exact definition is not found in the catalog. A temporary object has been created as a substitute.";

    @Expose
    @SerializedName("deprecationState")
    private String deprecationState;

    @Expose @SerializedName("remarks")
    private String remarks;

    /**
     * Constructor
     */
    public DeprecationInfoImpl() {
    }

    /**
     * Constructor
     * @param deprecationState the deprecation state
     * @param remarks the deprecation remarks
     */
    public DeprecationInfoImpl(String deprecationState,
                               String remarks) {
        this.deprecationState = deprecationState;
        this.remarks = remarks;
    }

    /**
     * get the deprecation state
     * @return the deprecation state
     */
    public String getDeprecationState() {
        return deprecationState;
    }

    /**
     * get the deprecation remarks
     * @return the deprecation remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Gets the string representation the DeprecationInfoImpl object
     * @return the string representation of the DeprecationInfoImpl object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getDeprecationState());
        sb.append(" ");
        sb.append(getRemarks());

        return sb.toString();
    }
}
