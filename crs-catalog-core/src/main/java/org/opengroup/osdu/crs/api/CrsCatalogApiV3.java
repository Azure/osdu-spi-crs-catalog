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

package org.opengroup.osdu.crs.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.http.AppError;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.CoordinateTransformationsQuery;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchResult;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.service.PointsInAouService;
import org.opengroup.osdu.crs.service.SearchWrapperService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.inject.Inject;

/**
 *  CrsApi is an endpoint class we are exposing so that clients can request or query for
 *  coordinate reference system related catalog data information through its various
 *  exposed API methods
 */

@RestController
@RequestMapping(path= "/v3")
public class CrsCatalogApiV3 {

	@Inject
	private AuditLogger auditLogger;

	@Inject
	private JaxRsDpsLog log;

	@Inject
	private SearchWrapperService searchWrapperService;

	@Inject
	private PointsInAouService pointsInAouService;

	@Operation(summary = "${CrsCatalogApiV3.CoordinateTransformation.summary}", description = "${CrsCatalogApiV3.CoordinateTransformation.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-transformations-api-v3"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SearchResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/coordinate-transformation", produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse getCoordinateTransformation(
			@RequestParam(required = false) String recordId,
			@RequestParam(required = false) String dataId
			) {
		if(recordId != null && recordId.endsWith(":")){
			recordId= RemoveTerminatingColon(recordId);
		}
		return searchWrapperService.getSingleCrsOrCt(recordId, dataId, SearchWrapperService.getCoordinateTransformationKind());
	}


	@Operation(summary = "${CrsCatalogApiV3.CoordinateTransformations.summary}", description = "${CrsCatalogApiV3.CoordinateTransformations.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-transformations-api-v3"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SearchResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/coordinate-transformation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse getCoordinateTransformations(
			@RequestBody(required=false) CoordinateTransformationsQuery coordinateTransformationsQuery
			) {
		if(coordinateTransformationsQuery.getOffset()!=null){
			return searchWrapperService.search(coordinateTransformationsQuery, SearchWrapperService.getCoordinateTransformationKind());

		}
			return searchWrapperService.searchWithCursor(coordinateTransformationsQuery, SearchWrapperService.getCoordinateTransformationKind());
	}


	@Operation(summary = "${CrsCatalogApiV3.CoordinateReferenceSystem.summary}", description = "${CrsCatalogApiV3.CoordinateReferenceSystem.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api-v3"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SearchResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/coordinate-reference-system", produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse getCoordinateReferenceSystem(
			@RequestParam(required = false) String recordId,
			@RequestParam(required = false) String dataId
	) {
		if(recordId != null && recordId.endsWith(":")){
			recordId= RemoveTerminatingColon(recordId);
		}
		return searchWrapperService.getSingleCrsOrCt(recordId, dataId, SearchWrapperService.getCoordinateReferenceSystemKind());
	}

	@Operation(summary = "${CrsCatalogApiV3.CoordinateReferenceSystems.summary}", description = "${CrsCatalogApiV3.CoordinateReferenceSystems.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api-v3"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SearchResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/coordinate-reference-system", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse getCoordinateReferenceSystems(
			@RequestBody(required=false) CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery
	) {
	if(coordinateReferenceSystemsQuery.getOffset()!=null){
		return searchWrapperService.search(coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

	}
		return searchWrapperService.searchWithCursor(coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());
	}

	@Operation(summary = "${CrsCatalogApiV3.CoordinateReferenceSystems.summary}", description = "${CrsCatalogApiV3.CoordinateReferenceSystems.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"area-of-use-api-v3"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = PointsInAouSearchResult.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/points-in-aou", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PointsInAouSearchResult getAouInfo(
			@RequestBody InPolygonQuery inPolygonQuery
	) {
		if (inPolygonQuery.getRecordId() !=null && inPolygonQuery.getRecordId().endsWith(":")) {

			inPolygonQuery.setRecordId(RemoveTerminatingColon(inPolygonQuery.getRecordId()));
		}
		return pointsInAouService.searchPointsInAou(inPolygonQuery);
	}

	public String RemoveTerminatingColon(String record_id) {
		record_id = record_id.substring(0, record_id.length() - 1);
		return record_id;
	}
}
