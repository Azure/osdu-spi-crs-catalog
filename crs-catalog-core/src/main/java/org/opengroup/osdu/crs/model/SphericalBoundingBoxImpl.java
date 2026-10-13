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

import com.fasterxml.jackson.annotation.JsonProperty;

import org.opengroup.osdu.crs.model.interfaces.SphericalBoundingBox;

/**
 *  SphericalBoundingBoxImpl is a class which encapsulates a two dimensional spherical bounding box
 *  consisting of lower left corner and upper right corner to describe an area.
 */
public class SphericalBoundingBoxImpl extends RootImpl implements SphericalBoundingBox {
    @Expose @SerializedName("lonMin")
    private double longitudeLeft;
    
    @Expose @SerializedName("latMin")
    private double latitudeLower;

    @Expose @SerializedName("lonMax")
    private double longitudeRight;
    
    @Expose @SerializedName("latMax")
    private double latitudeUpper;

	/**
	 * Constructor
	 */
    public SphericalBoundingBoxImpl() {
    	this.longitudeLeft = 0.0;
    	this.latitudeLower = 0.0;
    	this.longitudeRight = 0.0;
    	this.latitudeUpper = 0.0;
    }

	/**
	 * Constructor
	 * @param longitudeLeft the left longitude value
	 * @param latitudeLower the lower latitude value
	 * @param longitudeRight the right longitude value
	 * @param latitudeUpper the upper latitude value
	 */
    public SphericalBoundingBoxImpl(double longitudeLeft, double latitudeLower, double longitudeRight, double latitudeUpper) {
    	this.longitudeLeft = longitudeLeft;
    	this.latitudeLower = latitudeLower;
    	this.longitudeRight = longitudeRight;
    	this.latitudeUpper = latitudeUpper;
   }

	/**
	 * Gets the left longitude value
	 * @return left longitude value
	 */
	@JsonProperty("lonMin")
	public double getLongitudeLeft() {
		return longitudeLeft;
	}

	/**
	 * Gets the lower latitude value
	 * @return lower latitude value
	 */
	@JsonProperty("latMin")
	public double getLatitudeLower() {
		return latitudeLower;
	}

	/**
	 * Gets the right longitude value
	 * @return right longitude value
	 */
	@JsonProperty("lonMax")
	public double getLongitudeRight() {
		return longitudeRight;
	}

	/**
	 * Gets the upper latitude value
	 * @return upper latitude value
	 */
	@JsonProperty("latMax")
	public double getLatitudeUpper() {
		return latitudeUpper;
	}

	/**
	 * Gets the string representation the {@link SphericalBoundingBoxImpl} object
	 * @return the string representation of the {@link SphericalBoundingBoxImpl} object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(Double.toString(getLongitudeLeft()));
		sb.append(" ");
		sb.append(Double.toString(getLatitudeLower()));
		sb.append(" ");
		sb.append(Double.toString(getLongitudeRight()));
		sb.append(" ");
		sb.append(Double.toString(getLatitudeUpper()));

		return sb.toString();
	}

	/**
	 * check for equality of this {@link SphericalBoundingBoxImpl} object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
    public boolean equals(Object other) {
		if(!(other instanceof SphericalBoundingBox))
			return false;
		SphericalBoundingBox otherSphericalBoundingBox = (SphericalBoundingBox)other;
		return (Utility.isEqual(getLongitudeLeft(), otherSphericalBoundingBox.getLongitudeLeft()) &&
	    	    Utility.isEqual(getLatitudeLower(), otherSphericalBoundingBox.getLatitudeLower()) &&
	    	    Utility.isEqual(getLongitudeRight(), otherSphericalBoundingBox.getLongitudeRight()) &&
	    	    Utility.isEqual(getLatitudeUpper(), otherSphericalBoundingBox.getLatitudeUpper()));
    }

	/**
	 * gets the hash code of this {@link SphericalBoundingBoxImpl} object
	 * @return the hash code of this {@link SphericalBoundingBoxImpl} object
	 */
	@Override
	public int hashCode() {
		int hash = Double.valueOf(longitudeLeft).hashCode();
		hash += 13 * Double.valueOf(latitudeLower).hashCode();
		hash += 17 * Double.valueOf(longitudeRight).hashCode();
		hash += 19 * Double.valueOf(latitudeUpper).hashCode();
		return hash;
	}

}

