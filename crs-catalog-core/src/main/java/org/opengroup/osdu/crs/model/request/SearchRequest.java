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
package org.opengroup.osdu.crs.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.opengroup.osdu.crs.config.NullableStringDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Class representing a search query
 */
@Schema(description = "Request containing a Lucene style query string.")
public class SearchRequest {
    @Expose
    @SerializedName("query")
    @JsonDeserialize(using = NullableStringDeserializer.class)
    @Schema(description = "The Lucene style query string with keywords. Omit or set to null for a bounding-box-only search.",
            types = {"string", "null"}, nullable = true)
    private String query;

    /**
     * private empty constructor
     */
    private SearchRequest() {}

    /**
     * Public constructor with query value
     * @param query The Lucene style keyword query
     */
    // Keep this Java convenience constructor from accepting a bare JSON string.
    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public SearchRequest(String query) {
        this.query = query;
    }

    /**
     * Gets the Lucene style keyword query
     * @return The query string
     */
    @JsonProperty("query")
    public String getQuery() {return this.query;}
}
