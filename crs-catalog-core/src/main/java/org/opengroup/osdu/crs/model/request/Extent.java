// Copyright © Amazon
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http:#www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.crs.config.NullableStringDeserializer;

@Data
@Schema(description = "Description of extent. Corresponds to PreferredUsage.Extent.Description in the record. All entities with PreferredUsage.Extent.Description containing the search string will be returned. Wildcard is not supported.")
public class Extent {
	@Schema(type = "string")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = NullableStringDeserializer.class)
	private String name;
	@Schema(type = "string")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = NullableStringDeserializer.class)
	private String description;
}
