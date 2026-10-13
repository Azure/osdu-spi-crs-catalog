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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.RepresentationMode;
import org.opengroup.osdu.crs.model.interfaces.SingleCT;
import org.opengroup.osdu.crs.model.interfaces.SingleCTEssence;
import org.opengroup.osdu.crs.model.interfaces.Essence;
import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 * SingleCTImpl is a child of the {@link CTImpl} class. It encapsulates a single
 * cartographic transformation.
 */
public class SingleCTImpl extends CTImpl implements SingleCT {

    @Expose
    @SerializedName("essence")
    private SingleCTEssenceImpl singleCTEssence;

    /**
     * Constructor
     */
    public SingleCTImpl() {
        super();
        singleCTEssence = new SingleCTEssenceImpl();
    }

    /**
     * Copy constructor
     *
     * @param other as {@link SingleCTImpl}
     */
    public SingleCTImpl(SingleCTImpl other) {
        super(other);
        this.singleCTEssence = other.singleCTEssence;
    }

    /**
     * set the essence of the SingleCT object
     *
     * @param essence the {@link SingleCTEssence} object
     */
    public void setEssence(SingleCTEssence essence) {
        singleCTEssence = (SingleCTEssenceImpl) essence;
    }

    /**
     * get the essence of the SingleCT object
     *
     * @return {@link Essence}
     */
    @Override
    public Essence getEssence() {
        return singleCTEssence;
    }

    /**
     * get the essence of the SingleCT object
     *
     * @return {@link SingleCTEssence}
     */
    @JsonIgnore
    @Override
    public SingleCTEssence getSingleCTEssence() {
        return singleCTEssence;
    }

    /**
     * get the string representation the SingleCTImpl object
     *
     * @return the string representation of the SingleCTImpl object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());
        sb.append(" ");
        Essence essence = getEssence();
        if (essence != null) {
            sb.append(essence.toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * check for equality of this SingleCTImpl object with the other object
     *
     * @return true of they are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SingleCTImpl))
            return false;

        SingleCTImpl otherCompoundCT = (SingleCTImpl) other;
        return singleCTEssence.equals(otherCompoundCT.singleCTEssence);
    }

    /**
     * get the hash code of this SingleCTImpl object
     *
     * @return the hash code of this SingleCTImpl object
     */
    @Override
    public int hashCode() {
        if (getEssence() != null) return singleCTEssence.hashCode();
        else return getNamedReference().hashCode();
    }

    /*
     * create an {@link SingleCTImpl from a JSON string
     * @param json the input JSON string
     * @return the fully initialized{@link SingleCTImpl} object
     * @throws Exception which indicates failure to create the object
     */
    public static SingleCTImpl fromJson(String json) throws Exception {
        return Utility.fromJson(json, SingleCTImpl.class);
    }

    /**
     *
     * @param mode The requested {@link RepresentationMode}
     * @return The {@link SingleCT} conforming to mode
     */
    public SingleCT convert(RepresentationMode mode) {
        if (mode == RepresentationMode.ESSENCE) return this;
        SingleCTImpl copy = new SingleCTImpl(this);
        if (mode == RepresentationMode.PERSISTABLE_REFERENCE ||
                mode == RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE) {
            NamedReference namedRef = new NamedReference(this.singleCTEssence.getName(),
                    this.singleCTEssence.toJsonString());
            copy.setNamedReference(namedRef);
        }
        if (mode == RepresentationMode.PERSISTABLE_REFERENCE) {
            copy.setEssence(null);
        }
        return copy;
    }
}

