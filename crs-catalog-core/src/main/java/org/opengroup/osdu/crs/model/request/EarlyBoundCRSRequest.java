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
import org.opengroup.osdu.crs.model.EarlyBoundCRSEssenceImpl;

/**
 *  EarlyBoundCRSRequest is class which encapsulates the request body for a EarlyBoundCRS object.
 */
@Schema(description = "Request to get one EarlyBoundCRS given its 'persistableReference' (serialized essence) or 'essence' structure. Only one, persistableReference or essence must be provided. If both are provided, essence takes precedence")
public class EarlyBoundCRSRequest {
	@JsonProperty("essence")
	@Schema(description = "Early-bound coordinate reference system, i.e. a CRS with a transformation binding to WGS 84")
	private EarlyBoundCRSEssenceImpl earlyBoundCRSEssence;
	@JsonProperty("persistableReference")
	@Schema(description = "The persistable reference string, either the essence of the EarlyBoundCRS serialized into a JSON string or an encoded string (version 1)")
	private String persistableReference;
	/**
	 * Constructor
	 */
    public EarlyBoundCRSRequest()
    {
    }

	/**
	 * Constructor
	 */
    public EarlyBoundCRSRequest(EarlyBoundCRSEssenceImpl earlyBoundCRSEssence)
    {
		this.earlyBoundCRSEssence = earlyBoundCRSEssence;
    }

	/**
	 * Constructor
	 * @param earlyBoundCRSEssence the {@link EarlyBoundCRSEssenceImpl} (can be null, then persistableReference must be defined)
	 * @param persistableReference the persistable reference (can be null, then earlyBoundCRSEssence must be non-null)
	 */
	public EarlyBoundCRSRequest(EarlyBoundCRSEssenceImpl earlyBoundCRSEssence, String persistableReference) {
		this.earlyBoundCRSEssence = earlyBoundCRSEssence;
		this.persistableReference = persistableReference;
	}
	/**
	 * get the EarlyBoundCRS essence
	 * @return the EarlyBoundCRS essence
	 */
	public EarlyBoundCRSEssenceImpl getEarlyBoundCRSEssence() {
		return this.earlyBoundCRSEssence;
	}

	/**
	 * Get the persistable reference string as passed through the request
	 * @return the persistable reference string
	 */
	public String getPersistableReference() { return this.persistableReference; }
}

