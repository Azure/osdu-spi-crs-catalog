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
package org.opengroup.osdu.crs.model.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.crs.model.CompoundCTEssenceImpl;

/**
 *  CompoundCTRequest is class which encapsulates the request body for a compound CT object.
 */
@Schema(description = "Request to get one CompoundCT given its 'persistableReference' (serialized essence) or 'essence' structure. Only one, persistableReference or essence must be provided. If both are provided, essence takes precedence.")
public class CompoundCTRequest {
	@JsonProperty("essence")
	@Schema(description = "Compound cartographic transformation")
	private CompoundCTEssenceImpl compoundCTEssence;

	@JsonProperty("persistableReference")
	@Schema(description = "The persistable reference string, either the essence of the CompoundCT serialized into a JSON string or an encoded string (version 1).", type = "string")
	private String persistableReference;

	/**
	 * Constructor
	 */
    public CompoundCTRequest()
    {
    }

	/**
	 * Constructor
	 */
    public CompoundCTRequest(CompoundCTEssenceImpl compoundCTEssence)
    {
		this.compoundCTEssence = compoundCTEssence;
    }

	/**
	 * Constructor
	 * @param compoundCTEssence the {@link CompoundCTEssenceImpl} input
	 * @param persistableReference the persistable reference string
	 */
	public CompoundCTRequest(CompoundCTEssenceImpl compoundCTEssence, String persistableReference){
    	this.compoundCTEssence = compoundCTEssence;
    	this.persistableReference = persistableReference;
	}
	/**
	 * get the CompoundCT essence
	 * @return the CompoundCT essence
	 */
	public CompoundCTEssenceImpl getCompoundCTEssence() {
		return compoundCTEssence;
	}

	/**
	 * Get the persistable reference string as passed through the request
	 * @return the persistable reference string
	 */
	public String getPersistableReference() { return this.persistableReference; }

}

