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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.search.NamedReference;


/**
 *  CompoundCTImpl is a child of the {@link CTImpl} class. It is a compound
 *  cartographic transformation which consists of a list of member {@link SingleCTImpl}s.
 */
public class CompoundCTImpl extends CTImpl implements CompoundCT {

    @Expose @SerializedName("essence")
    private CompoundCTEssenceImpl compoundCTEssence;

    @JsonIgnore
	private ArrayList<SingleCTImpl> memberCTs;

	/**
	 * Constructor
	 */
    public CompoundCTImpl(){
    	super();
        compoundCTEssence = new CompoundCTEssenceImpl();
        this.memberCTs = new ArrayList<>();
    }

	/**
	 * Copy constructor
	 * @param other The instance of {@link CompoundCTImpl} to copy
	 */
	public CompoundCTImpl(CompoundCTImpl other){
    	super(other);
    	this.compoundCTEssence = other.compoundCTEssence;
    	this.memberCTs = other.memberCTs;
	}

	/**
	 * get the child cartographic transforms
	 * @return list of member cartographic transforms
	 */
	@JsonIgnore
	public List<SingleCT> getCTs() {
		List<SingleCT> result = new ArrayList<>();
		result.addAll(this.memberCTs);
		return result;
	}

	/**
	 * Set the list of member CTs
	 * @param memberCTs The list of member CTs
	 */
	protected void setCTs(List<SingleCT> memberCTs){
		this.memberCTs.clear();
		for (SingleCT ct : memberCTs) {
			if (ct == null) {
				this.memberCTs.clear();
				return; // bail out the sequence is corrupted, the essence was ot found to exist in the catalog as individual SingleCT
			}
			this.memberCTs.add((SingleCTImpl) ct);
		}
	}
	/**
	 * set the essence of the CompoundCT object
	 * @param essence the {@link CompoundCTEssence} object
	 */
	public void setEssence(CompoundCTEssence essence) {
		this.compoundCTEssence = (CompoundCTEssenceImpl)essence;
	}

	/**
	 * get the essence of the CompoundCTImpl object
	 * @return {@link CompoundCTEssence}
	 */
	@Override
	public Essence getEssence() {
		return compoundCTEssence;
	}

	/**
	 * get the essence of the CompoundCTImpl object
	 * @return {@link CompoundCTEssence}
	 */
	@JsonIgnore
	@Override
	public CompoundCTEssence getCompoundCTEssence() {
		return compoundCTEssence;
	}

	/**
	 * get the string representation the CompoundCTImpl object
	 * @return the string representation of the CompoundCTImpl object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(" ");
        if (this.getEssence() == null) sb.append(this.getNamedReference().toString());
        else sb.append(getEssence().toString());
        sb.append(" ");
        
        return sb.toString();
	}

	/**
	 * get the child cartographic transforms' {@link AuthorityCode}'s
	 * @return list of child cartographic transforms' {@link AuthorityCode}'s
	 */
	@JsonIgnore
	public List<AuthorityCode> getCTAuthorityCodes() {
		return compoundCTEssence.getCTAuthorityCodes();
	}

	/**
	 * check for equality of this CompoundCTImpl object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof CompoundCTImpl))
			return false;

		CompoundCTImpl otherCompoundCT = (CompoundCTImpl)other;
		return compoundCTEssence.equals(otherCompoundCT.compoundCTEssence);
	}

	/**
	 * get the hash code of this CompoundCTImpl object
	 * @return the hash code of this CompoundCTImpl object
	 */
	@Override
	public int hashCode() {
		if (getEssence() != null) return compoundCTEssence.hashCode();
		else return getNamedReference().hashCode();
	}

	/*
     * create an {@link CompoundCTImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CompoundCTImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	 public static CompoundCTImpl fromJson(String json) throws Exception {
		 return Utility.fromJson(json, CompoundCTImpl.class);
	 }

	/**
	 * @param mode The requested {@link RepresentationMode}
	 * @return The {@link CompoundCT} conforming to mode
	 */
	public CompoundCT convert(RepresentationMode mode) {
		if (mode == RepresentationMode.ESSENCE) return this;
		CompoundCTImpl copy = new CompoundCTImpl(this);
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
				mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
			NamedReference namedRef = new NamedReference(this.compoundCTEssence.getName(),
					this.compoundCTEssence.toJsonString());
			copy.setNamedReference(namedRef);
		}
		if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
			copy.setEssence(null);
		}
		return copy;
	}

}

