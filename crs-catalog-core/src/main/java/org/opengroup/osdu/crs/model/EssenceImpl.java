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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.opengroup.osdu.crs.model.interfaces.Essence;
import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;

import java.io.IOException;

/**
 *  EssenceImpl is a base class which implements the Essence interface
 *  to encapsulate the common features of an essence object.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="type", visible=true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = LateBoundCRSEssenceImpl.class, name = "LBC"),
		@JsonSubTypes.Type(value = EarlyBoundCRSEssenceImpl.class, name = "EBC"),
		@JsonSubTypes.Type(value = CompoundCRSEssenceImpl.class, name = "CC"),
		@JsonSubTypes.Type(value = SingleCTEssenceImpl.class, name = "ST"),
		@JsonSubTypes.Type(value = CompoundCTEssenceImpl.class, name = "CT"),
		@JsonSubTypes.Type(value = AreaOfUseEssenceImpl.class, name = "AOU")
})
public class EssenceImpl extends RootImpl implements Essence {

	@Expose @SerializedName("authCode")
	@JsonProperty("authCode")
    private AuthorityCodeImpl authorityCodeImpl;

	@Expose @SerializedName("type")
	@JsonProperty("type")
    private String type;

	/**
	 * Constructor
	 */
	public EssenceImpl()
	{
		super();
		authorityCodeImpl = new AuthorityCodeImpl();
		type = "";
	}

	/**
	 * get the {@link AuthorityCode} property
	 * @return the {@link AuthorityCode} property object
	 */
	@JsonProperty("authCode")
	public AuthorityCode getAuthorityCode() {
		return authorityCodeImpl;
	}

	/**
	 * get the type property
	 * @return the type property
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * Gets the string representation the {@link EssenceImpl} object
	 * @return the string representation of the {@link EssenceImpl} object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(" ");
		sb.append(getType());
		sb.append(" ");
		sb.append(authorityCodeImpl.toString());
		return sb.toString();
	}
	public static EssenceImpl createInstance(String json) {
		if (json != null && !json.isEmpty()) {
			String trimmed = json.trim();
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				JsonNode node = mapper.readTree(trimmed);
				return mapper.treeToValue(node, EssenceImpl.class);
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * check for equality of this {@link EssenceImpl} object with the other object
	 * @return true of they are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof EssenceImpl))
			return false;

		EssenceImpl otherEssenceImpl = (EssenceImpl)other;
		return getType().equals(otherEssenceImpl.getType()) &&
			   getAuthorityCode().equals(otherEssenceImpl.getAuthorityCode());
	}

	/**
	 * get the hash code of this {@link EssenceImpl} object
	 * @return the hash code of this {@link EssenceImpl} object
	 */
	@Override
	public int hashCode() {
		int hash = (getType() == null? 0 : getType().hashCode());
		hash += 13 *(authorityCodeImpl == null? 0 : authorityCodeImpl.hashCode());

		return hash;
	}

}

