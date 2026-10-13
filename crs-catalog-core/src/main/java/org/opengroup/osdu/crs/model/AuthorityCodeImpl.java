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
 * Copyright 2016 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;


/**
 *  AuthorityCodeImpl is a class which encapsulate the a and c properties.
 */
public class AuthorityCodeImpl extends RootImpl implements AuthorityCode {

    @Expose @SerializedName("auth")
    private String authority;
    
    @Expose @SerializedName("code")
    private String code;


    /**
     * Constructor
     */
    public AuthorityCodeImpl(){
        this.authority = "";
        this.code = "";
    }

    /**
     * Constructor
     * @param authority the a identifier
     * @param code the c identifier
     */
    public AuthorityCodeImpl(String authority, String code){
        this.authority = authority;
        this.code = code;
    }

    /**
     * Gets the a property of this {@link AuthorityCodeImpl} object
     * @return the a property of this {@link AuthorityCodeImpl} object
     */
    @JsonProperty("auth")
    public String getAuthority() {
        return authority;
    }

    /**
     * Gets the c property of this {@link AuthorityCodeImpl} object
     * @return the c property of this {@link AuthorityCodeImpl} object
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * Gets the string representation the {@link AuthorityCodeImpl} object
     * @return the string representation of the {@link AuthorityCodeImpl} object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getAuthority());
        sb.append(" ");
        sb.append(getCode());

        return sb.toString();
    }

    /**
     * check for equality of this {@link AuthorityCodeImpl} object with the other object
     * @return true of they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof AuthorityCodeImpl))
            return false;

        AuthorityCodeImpl otherAuthorityCodeImpl = (AuthorityCodeImpl)other;
        return getAuthority().equals(otherAuthorityCodeImpl.getAuthority()) &&
               getCode().equals(otherAuthorityCodeImpl.getCode());
    }

    /**
     * gets the hash c of this {@link AuthorityCodeImpl} object
     * @return the hash c of this {@link AuthorityCodeImpl} object
     */
    @Override
    public int hashCode() {
        int hash = authority == null? 0 : authority.hashCode();
        hash += 13 * (code == null? 0 : code.hashCode());
        return hash;
    }
}

