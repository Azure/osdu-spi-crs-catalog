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
 */
package org.opengroup.osdu.crs.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.opengroup.osdu.crs.model.CRSEssenceImpl;

/**
 *  CRSRequest is class which encapsulates the request body for a CRS object.
 */
@Schema(description = "Request to get one CRS (any sub-type) given its 'persistableReference' (serialized essence) or 'essence' structure. Only one, persistableReference or essence must be provided. If both are provided, essence takes precedence. If both are provided, essence takes precedence")
public class CRSRequest {
    @Schema(description = "Any coordinate reference system essence carrying all possible properties for LateBoundCRSEssence, EarlyBoundCRSEssence and CompoundCRSEssence")
    @JsonProperty("essence")
    private CRSEssenceImpl crsEssenceJson;

    @Schema(description = "The persistable reference string, either the essence of the CRS serialized into a JSON string or an encoded string (version 1)", type = "string")
    @JsonProperty("persistableReference")
    private String persistableReference;

    /**
     * Constructor
     */
    public CRSRequest() {
    }

    /**
     * Constructor
     */
    public CRSRequest(CRSEssenceImpl crsEssenceJson) {
        this.crsEssenceJson = crsEssenceJson;
    }

    /**
     * Completely initializing constructor
     * @param crsEssenceJson JSON essence string
     * @param persistableReference persistable reference string
     */
    public CRSRequest(CRSEssenceImpl crsEssenceJson, String persistableReference) {
        this.crsEssenceJson = crsEssenceJson;
        this.persistableReference = persistableReference;
    }

    /**
     * get the json representation of the CRS essence
     * @return the json representation of the CRS essence as {@link CRSEssenceImpl}
     */
    @Schema(description = "json representation of the CRS essence")
    public CRSEssenceImpl getCRSEssenceJson() {
        return crsEssenceJson;
    }

    /**
     * Get the persistable reference string as passed through the request
     * @return the persistable reference string
     */
    @Schema(description = "persistable reference string")
    public String getPersistableReference() {
        return this.persistableReference;
    }
}

