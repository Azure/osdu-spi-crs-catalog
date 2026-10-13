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
 *
 */
package org.opengroup.osdu.crs.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.CompoundCTEssence;
import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;
import org.opengroup.osdu.crs.model.interfaces.CTEssence;;

/**
 *  CompoundCTEssenceImpl is a class which encapsulates the essence of a compound
 *  cartographic transformation. It is a child class of the CTEssenceImpl class
 */
public class CompoundCTEssenceImpl extends CTEssenceImpl implements CompoundCTEssence{
  	
	@Expose @SerializedName("policy")
	@JsonProperty("policy")
    private String policy;
    
	@Expose @SerializedName("cts")
	@JsonProperty("cts")
    private List<SingleCTEssenceImpl> childCTEssences;

    /**
     * Constructor
     */
    public CompoundCTEssenceImpl() {
    	super();
		policy = "";
		childCTEssences = new ArrayList<>();
    }

	/**
     * get the policy
     * @return the policy string
     */
	@JsonProperty("policy")
	public String getPolicy() {
		return policy;
	}

    /**
     * get the child cartographic transform essences
     * @return list of child cartographic transform essences
     */
	@JsonProperty("cts")
	@Override
	public List<CTEssence> getCTEssences() {
        List<CTEssence> results = new ArrayList<>();
        for (CTEssence essence : childCTEssences) {
            results.add(essence);
        }
        return results;
	}

	/**
	 * get the child cartographic transforms' {@link AuthorityCode}'s
	 * @return list of child cartographic transforms' {@link AuthorityCode}'s
	 */
	@JsonIgnore
	@Override
	public List<AuthorityCode> getCTAuthorityCodes() {
        List<AuthorityCode> results = new ArrayList<>();
        for (CTEssence ct : this.childCTEssences) {
            results.add(ct.getAuthorityCode());
        }
        return results;
	}

	/**
	 * get the string representation the CompoundCTEssenceImpl object
	 * @return the string representation of the CompoundCTEssenceImpl object
	 */
	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());
        sb.append(" ");
        sb.append(getPolicy());
        sb.append(" ");
        for(SingleCTEssenceImpl ct : childCTEssences) {
	        sb.append(ct.toString());
	        sb.append(" ");
        }
        
        return sb.toString();
	}

	/**
	 * check for equality of this CompoundCTEssenceImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!super.equals(other))
			return false;

		if(!(other instanceof CompoundCTEssenceImpl))
			return false;

		CompoundCTEssenceImpl otherCompoundCTEssence = (CompoundCTEssenceImpl)other;
	    if(!getPolicy().equals(otherCompoundCTEssence.getPolicy()))
	    	return false;

	    List<CTEssence> otherCTEssences = otherCompoundCTEssence.getCTEssences();
	    if((childCTEssences == null) || (otherCTEssences == null))
	    	return false;

	    if(childCTEssences.size() != otherCTEssences.size())
	    	return false;
	    
		for(int i = 0; i < childCTEssences.size(); i++) {
			CTEssence ctEssenceThis = childCTEssences.get(i);  // the order matters
			CTEssence ctEssenceOther = otherCTEssences.get(i);
			if (!ctEssenceThis.equals(ctEssenceOther)) return false;
		}
        return true;
	}

	/**
	 * get the hash code of this CompoundCTEssenceImpl object
	 * @return the hash code of this CompoundCTEssenceImpl object
	 */
	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash += 31 * (policy == null? 0 : policy.hashCode());
		final int primes[] = {41, 43};
		try {
			for(int i = 0; i < childCTEssences.size() && i < primes.length; i++) {
				hash += primes[i] * childCTEssences.hashCode();
			}
		}
		catch (Exception ex) {
		}
		return hash;
	}

	/*
     * create an {@link CompoundCTEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CompoundCTEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static CompoundCTEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, CompoundCTEssenceImpl.class);
	}
	
}

