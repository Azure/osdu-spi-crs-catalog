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
 * Copyright 2016 Schlumberger. All Rights Reserved.
 *
 */

package org.opengroup.osdu.crs.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.http.AppError;
import org.opengroup.osdu.crs.constants.CrsCatalogRole;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.opengroup.osdu.crs.model.*;
import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.request.*;
import org.opengroup.osdu.crs.model.search.*;
import org.opengroup.osdu.crs.util.AppException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *  CrsApi is an endpoint class we are exposing so that clients can request or query for
 *  coordinate reference system related catalog data information through its various
 *  exposed API methods
 */

@RestController
@RequestMapping(path= "/v2")
public class CrsCatalogApi {

	private Catalog catalog = null;

	@Inject
	private AuditLogger auditLogger;
	@Inject
	private JaxRsDpsLog log;

	public CrsCatalogApi(Catalog catalog) {
		this.catalog = catalog;
	}

    /**
     * get the entire catalog content
      * @return Catalog
     */

	@Operation(summary = "${CrsCatalogApi.Catalog.summary}", description = "${CrsCatalogApi.Catalog.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"crs-catalog-api"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = Catalog.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
	public Catalog getCatalog() {
   		return catalog.getCatalogResponse();
	}

    /**
     * get the catalog attributes
     * @return catalog attributes
     */
	@Operation(summary = "${CrsCatalogApi.CatalogAttributes.summary}", description = "${CrsCatalogApi.CatalogAttributes.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"crs-catalog-api"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CatalogAttributes.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/catalog/attributes",produces = MediaType.APPLICATION_JSON_VALUE)
	public CatalogAttributes getCatalogAttributes() {
   		return catalog.getAttributes();
	}

    /**
     * get a list of AreasOfUses in the catalog
     * @param offset  Offset of the first item in the AreasOfUses. It is optional and is 0 by default.
     * @param limit   Maximum number of the AreasOfUse items returned. It is optional and is 100 by default.
     * @return AreasOfUseResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */

	@Operation(summary = "${CrsCatalogApi.AllArea.summary}", description = "${CrsCatalogApi.AllArea.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"areas-of-use-api"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = AreaOfUseResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
	public AreaOfUseResults getAreasOfUse(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		AreaOfUseResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<AreaOfUse> list = catalog.getAreasOfUse();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<AreaOfUse> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results = new AreaOfUseResults(list2, offset, list.size());
				}
			}
		}
		catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readAreaSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		} else {
			this.auditLogger.readAreaSuccess(
				Collections.singletonList("AreaOfUseResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		}
		return results;
	}

    /**
     * get the AreaOfUse by essence
     * @param request AreaOfUseRequest
     * @return AreaOfUse
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneArea.summary}", description = "${CrsCatalogApi.OneArea.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"areas-of-use-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = AreaOfUse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/area", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AreaOfUse getAreaOfUse(@RequestBody AreaOfUseRequest request,
								  @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		AreaOfUseEssence essence = null;
		AreaOfUse aou = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getAreaOfUseEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (AreaOfUseEssenceImpl)ei;
			}
			aou = catalog.getAreaOfUse(essence);
			if(null != aou){
				aou = aou.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(aou)) {
			this.auditLogger.readAreaByEssenceSuccess(
				Collections.singletonList(aou.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		} else {
			this.auditLogger.readAreaByEssenceSuccess(
				Collections.singletonList("AreaOfUse is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		}
		return aou;
	}

    /**
     * get a list of CRSes in the catalog
     * @param offset  Offset of the first item in the crs. It is optional and is 0 by default.
     * @param limit   Maximum number of the crs items returned. It is optional and is 100 by default.
     * @return CRSResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.CoordinateReferenceSystems.summary}", description = "${CrsCatalogApi.CoordinateReferenceSystems.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CRSResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/crs", produces = MediaType.APPLICATION_JSON_VALUE)
	public CRSResults getAllCRSes(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		CRSResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<CRS> list = catalog.getAllCRSes();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<CRS> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add((CRS)list.get(i).convert(mode));
					}
					results = new CRSResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readCrsSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		} else {
			this.auditLogger.readCrsSuccess(
				Collections.singletonList("CRSResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		}
		return results;
	}

    /**
     * get the CRS by essence
     * @param request The CRSRequest object for the CRS
     * @return CRS
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.CoordinateReferenceSystemsOne.summary}", description = "${CrsCatalogApi.CoordinateReferenceSystemsOne.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CRS.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/crs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public 	CRS getCRS(@RequestBody CRSRequest request,
					  @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		CRS	crs = null;

		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			CRSEssence essence = request.getCRSEssenceJson();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (CRSEssence) ei;
				if (essence == null) throw new IllegalArgumentException("Malformed persistableReference");
			}
			crs = catalog.getCRS(essence);
			if(null != crs){
				crs = (CRS)crs.convert(mode);
			}
		}
		catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(crs)) {
			this.auditLogger.readCrsByEssenceSuccess(
				Collections.singletonList(crs.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		} else {
			this.auditLogger.readCrsByEssenceSuccess(
				Collections.singletonList("CRS is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER));
		}
		return crs;
	}

    /**
     * get a list of LateBoundCRSes in the catalog
     * @param offset  Offset of the first item in the LateBoundCRSes. It is optional and is 0 by default.
     * @param limit   Maximum number of the LateBoundCRSes items returned. It is optional and is 100 by default.
     * @return        LateBoundCRSResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.LateBoundCRS.summary}", description = "${CrsCatalogApi.LateBoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = LateBoundCRSResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/lateboundcrs", produces = MediaType.APPLICATION_JSON_VALUE)
	public LateBoundCRSResults getLateBoundCRSes(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		LateBoundCRSResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<LateBoundCRS> list = catalog.getLateBoundCRSes();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<LateBoundCRS> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results = new LateBoundCRSResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readLateBoundCrsSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readLateBoundCrsSuccess(
				Collections.singletonList("LateBoundCRSResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

    /**
     * get the LateBoundCRS by essence
     * @param request LateBoundCRSRequest
     * @return LateBoundCRS
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneLateBoundCRS.summary}", description = "${CrsCatalogApi.OneLateBoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = LateBoundCRS.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/lateboundcrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public LateBoundCRS getLateBoundCRS(
		    @RequestBody LateBoundCRSRequest request,
			@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		LateBoundCRSEssence essence = null;
		LateBoundCRS crs  = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getLateBoundCRSEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (LateBoundCRSEssenceImpl)ei;
			}
			crs = catalog.getLateBoundCRS(essence);
			if(crs != null) {
				crs = crs.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(crs)) {
			this.auditLogger.readLateBoundCrsByEssenceSuccess(
				Collections.singletonList(crs.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readLateBoundCrsByEssenceSuccess(
				Collections.singletonList("LateBoundCRS is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return crs;
	}

    /**
     * get a list of the EarlyBoundCRSes in the catalog
     * @param offset  Offset of the first item in the EarlyBoundCRSes. It is optional and is 0 by default.
     * @param limit   Maximum number of the EarlyBoundCRSes items returned. It is optional and is 100 by default.
     * @return EarlyBoundCRSResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.EarlyBoundCRS.summary}", description = "${CrsCatalogApi.EarlyBoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = EarlyBoundCRSResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/earlyboundcrs", produces = MediaType.APPLICATION_JSON_VALUE)
	public EarlyBoundCRSResults getEarlyBoundCRSes(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		EarlyBoundCRSResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<EarlyBoundCRS> list = catalog.getEarlyBoundCRSes();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<EarlyBoundCRS> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results =  new EarlyBoundCRSResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readEarlyBoundCrsSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readEarlyBoundCrsSuccess(
				Collections.singletonList("EarlyBoundCRSResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

    /**
     * get the EarlyBoundCRS by essence
     * @param request EarlyBoundCRSRequest
     * @return EarlyBoundCRS
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneEarlyBoundCRS.summary}", description = "${CrsCatalogApi.OneEarlyBoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = EarlyBoundCRS.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/earlyboundcrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EarlyBoundCRS getEarlyBoundCRS(@RequestBody EarlyBoundCRSRequest request,
										  @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		EarlyBoundCRSEssence essence = null;
		EarlyBoundCRS crs = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getEarlyBoundCRSEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (EarlyBoundCRSEssenceImpl)ei;
			}
			crs = catalog.getEarlyBoundCRS(essence);
			if(crs != null) {
				crs = crs.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(crs)) {
			this.auditLogger.readEarlyBoundCrsByEssenceSuccess(
				Collections.singletonList(crs.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readEarlyBoundCrsByEssenceSuccess(
				Collections.singletonList("EarlyBoundCRS is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return crs;
	}

    /**
     * get a list of the CompoundCRSes in the catalog
     * @param offset  Offset of the first item in the CompoundCRSes. It is optional and is 0 by default.
     * @param limit   Maximum number of the CompoundCRSes items returned. It is optional and is 100 by default.
     * @return CompoundCRSResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.CompoundCRS.summary}", description = "${CrsCatalogApi.CompoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CompoundCRSResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/compoundcrs",produces = MediaType.APPLICATION_JSON_VALUE)
	public CompoundCRSResults getCompoundCRSes(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		CompoundCRSResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<CompoundCRS> list = catalog.getCompoundCRSes();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<CompoundCRS> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results = new CompoundCRSResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readCompoundCrsSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCompoundCrsSuccess(
				Collections.singletonList("CompoundCRSResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

    /**
     * get the CompoundCRS by essence
     * @param request CompoundCRSRequest
     * @return CompoundCRS
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneCompoundCRS.summary}", description = "${CrsCatalogApi.OneCompoundCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CompoundCRS.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/compoundcrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompoundCRS getCompoundCRS(@RequestBody CompoundCRSRequest request,
									  @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		CompoundCRSEssence essence = null;
		CompoundCRS crs = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getCompoundCRSEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (CompoundCRSEssenceImpl)ei;
			}
			crs = catalog.getCompoundCRS(essence);
			if(crs != null){
				crs = crs.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(crs)) {
			this.auditLogger.readCompoundCrsByEssenceSuccess(
				Collections.singletonList(crs.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCompoundCrsByEssenceSuccess(
				Collections.singletonList("CompoundCRS is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return crs;
	}

    /**
     * get a list of cartographic transforms (CTs) in the catalog
     * @param offset  Offset of the first item in the cartographic transforms. It is optional and is 0 by default.
     * @param limit   Maximum number of the cartographic transforms items returned. It is optional and is 100 by default.
     * @return CTResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.CT.summary}", description = "${CrsCatalogApi.CT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CTResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/ct", produces = MediaType.APPLICATION_JSON_VALUE)
	public CTResults getAllCTs(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		CTResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<CT> list = catalog.getAllCTs();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<CT> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add((CT)list.get(i).convert(mode));
					}
					results = new CTResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readCtSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCtSuccess(
				Collections.singletonList("CTResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

    /**
     * get the CT by essence
     * @param request CTRequest
     * @return CT
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneCT.summary}", description = "${CrsCatalogApi.OneCT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CT.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/ct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CT getCT(@RequestBody CTRequest request,
					@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		CT ct = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			CTEssence essence = request.getCTEssenceJson();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (CTEssence)ei;
				if (essence == null) throw new IllegalArgumentException("Malformed persistableReference");
			}

			ct = catalog.getCT(essence);
			if(ct != null){
				ct = (CT)ct.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(ct)) {
			this.auditLogger.readCtByEssenceSuccess(
				Collections.singletonList(ct.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCtByEssenceSuccess(
				Collections.singletonList("CTResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return ct;
	}

    /**
     * get a list of SingleCTs in the catalog
     * @param offset  Offset of the first item in the SingleCTs. It is optional and is 0 by default.
     * @param limit   Maximum number of the SingleCTs items returned. It is optional and is 100 by default.
     * @return SingleCTResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.SingleCT.summary}", description = "${CrsCatalogApi.SingleCT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SingleCTResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/singlect",produces = MediaType.APPLICATION_JSON_VALUE)
	public SingleCTResults getSingleCTs(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		SingleCTResults	results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<SingleCT> list = catalog.getSingleCTs();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<SingleCT> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results = new SingleCTResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readSingleCtSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readSingleCtSuccess(
				Collections.singletonList("SingleCTResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

    /**
     * get the SingleCT by essence
     * @param request SingleCTRequest
     * @return SingleCT
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.OneSingleCT.summary}", description = "${CrsCatalogApi.OneSingleCT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = SingleCT.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/singlect", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SingleCT getSingleCT(@RequestBody SingleCTRequest request,
								@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		SingleCTEssence essence = null;
		SingleCT	ct = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getSingleCTEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (SingleCTEssence) ei;
			}
			ct = catalog.getSingleCT(essence);
			if(null != ct){
				ct = ct.convert(mode);
			}
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(ct)) {
			this.auditLogger.readSingleCtByEssenceSuccess(
				Collections.singletonList(ct.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readSingleCtByEssenceSuccess(
				Collections.singletonList("SingleCT is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return ct;
	}

    /**
     * get a list of CompoundCTs in the catalog
     * @param offset  Offset of the first item in the CompoundCTs. It is optional and is 0 by default.
     * @param limit   Maximum number of the CompoundCTs items returned. It is optional and is 100 by default.
     * @return CompoundCTResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.CompoundCT.summary}", description = "${CrsCatalogApi.CompoundCT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CompoundCTResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@GetMapping(value = "/compoundct", produces = MediaType.APPLICATION_JSON_VALUE)
	public CompoundCTResults getCompoundCTs(
		@RequestParam(required = false, defaultValue = "0") int offset,
	    @RequestParam(required = false, defaultValue = "100") int limit,
		@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		CompoundCTResults results = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			List<CompoundCT> list = catalog.getCompoundCTs();
			if(list != null) {
				if ((offset < 0) || ((list.size() > 0) && (offset >= list.size())))
					throw new IndexOutOfBoundsException("The start index is out of the range");

				if ((limit < 0) || (limit > list.size() - offset))
					limit = list.size() - offset;

				if (limit >= 0) {
					ArrayList<CompoundCT> list2 = new ArrayList<>();
					for (int i = offset; i < offset + limit; i++) {
						list2.add(list.get(i).convert(mode));
					}
					results = new CompoundCTResults(list2, offset, list.size());
				}
			}
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(results)) {
			this.auditLogger.readCompoundCtSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCompoundCtSuccess(
				Collections.singletonList("CompoundCTResults is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return results;
	}

   /**
    * get the CompoundCT by essence
    * @param request CompoundCTRequest
    * @return CompoundCT
	* @throws AppException with BadRequest status to indicate a client request is not recognized
	*/
   @Operation(summary = "${CrsCatalogApi.OneCompoundCT.summary}", description = "${CrsCatalogApi.OneCompoundCT.description}",
		   security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
		   requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
   @ApiResponses(value = {
		   @ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CompoundCT.class)) }),
		   @ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
		   @ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
   })
   @PostMapping(value = "/compoundct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CompoundCT getCompoundCT(@RequestBody CompoundCTRequest request,
									@RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		CompoundCTEssence essence = null;
		CompoundCT ct = null;
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			essence = request.getCompoundCTEssence();
			if (essence == null) {
				EssenceImpl ei = EssenceImpl.createInstance(request.getPersistableReference());
				essence = (CompoundCTEssenceImpl)ei;
			}
			ct = catalog.getCompoundCT(essence);
			if(ct != null)
				ct = ct.convert(mode);
		}
		catch (Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
		if (Objects.nonNull(ct)) {
			this.auditLogger.readCompoundCtByEssenceSuccess(
				Collections.singletonList(ct.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		} else {
			this.auditLogger.readCompoundCtByEssenceSuccess(
				Collections.singletonList("CompoundCT is null"),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
		}
		return ct;
	}

    /********************************************
     Search related API
     *********************************************/

    /**
	 * Search the AreasOfUse by keyword or bounding box and return the results in given range.
	 * @param request The {@link SearchRequest} containing the search keyword. It could be empty if the bounding box is set.
	 * @param longitudeLeft longitude value of the lower left point of the bounding box. It is option and is -180 degree by default.
	 * @param latitudeLower latitude value of the lower left point of the bounding box. It is option and is -90 degree by default.
	 * @param longitudeRight longitude value of the upper right point of the bounding box. It is option and is 180 degree by default.
	 * @param latitudeUpper latitude value of the upper right point of the bounding box. It is option and is 90 degree by default.
	 * @param offset The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default.
	 * @param limit The maximum number of the AreasOfUse returned. It is optional and is 100 by default.
	 * @return AreaOfUseResults
     * @throws AppException with BadRequest status to indicate a client request is not recognized
     */

	@Operation(summary = "${CrsCatalogApi.SearchAreaOfUse.summary}", description = "${CrsCatalogApi.SearchAreaOfUse.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"areas-of-use-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = AreaOfUseResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/search/area", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AreaOfUseResults searchAreasOfUse(@RequestBody SearchRequest request,
											 @RequestParam(required = false, defaultValue = "-180") double longitudeLeft,
											 @RequestParam(required = false, defaultValue = "-90") double latitudeLower,
											 @RequestParam(required = false, defaultValue = "180")double longitudeRight,
											 @RequestParam(required = false, defaultValue = "90") double latitudeUpper,
											 @RequestParam(required = false, defaultValue = "0") int offset,
											 @RequestParam(required = false, defaultValue = "100") int limit,
											 @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		try{
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
			AreaOfUseResults results = catalog.searchAreasOfUse(request, boundingBox, offset, limit, mode);
			this.auditLogger.searchAreaSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
			return results;
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
    }

    /**
     * Search the CRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the search keyword. It could be empty if the bounding box is set.
	 * @param longitudeLeft longitude value of the lower left point of the bounding box. It is option and is -180 degree by default.
	 * @param latitudeLower latitude value of the lower left point of the bounding box. It is option and is -90 degree by default.
	 * @param longitudeRight longitude value of the upper right point of the bounding box. It is option and is 180 degree by default.
	 * @param latitudeUpper latitude value of the upper right point of the bounding box. It is option and is 90 degree by default.
     * @param offset  The offset of the first item in the list of all CRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CRSes returned. It is optional and is 100 by default.
     * @return CRSResults
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.SearchCRS.summary}", description = "${CrsCatalogApi.SearchCRS.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"coordinate-reference-systems-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CRSResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/search/crs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CRSResults searchCRSes(@RequestBody SearchRequest request,
								  @RequestParam(required = false, defaultValue = "-180") double longitudeLeft,
								  @RequestParam(required = false, defaultValue = "-90") double latitudeLower,
								  @RequestParam(required = false, defaultValue = "180") double longitudeRight,
								  @RequestParam(required = false, defaultValue = "90") double latitudeUpper,
								  @RequestParam(required = false, defaultValue = "0") int offset,
								  @RequestParam(required = false, defaultValue = "100") int limit,
								  @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
			CRSResults results = catalog.searchCRSes(request, boundingBox, offset, limit, mode);
			this.auditLogger.searchCrsSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
			return results;
		}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
    }

	/**
     * Search the SearchCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the search keyword. It could be empty if the bounding box is set.
	 * @param longitudeLeft longitude value of the lower left point of the bounding box. It is option and is -180 degree by default.
	 * @param latitudeLower latitude value of the lower left point of the bounding box. It is option and is -90 degree by default.
	 * @param longitudeRight longitude value of the upper right point of the bounding box. It is option and is 180 degree by default.
	 * @param latitudeUpper latitude value of the upper right point of the bounding box. It is option and is 90 degree by default.
     * @param offset  The offset of the first item in the list of all CTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the CTs returned. It is optional and is 100 by default.
     * @return CTResults
	 * @throws AppException with BadRequest status to indicate a client request is not recognized
     */
	@Operation(summary = "${CrsCatalogApi.SearchCT.summary}", description = "${CrsCatalogApi.SearchCT.description}",
			security = {@SecurityRequirement(name = "Authorization")}, tags = {"cartographic-transformations-api"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "A successful response", content = { @Content(schema = @Schema(implementation = CTResults.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad input format",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "403", description = "User not authorized to perform the action.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "404", description = "Not Found",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "409", description = "A LegalTag with the given name already exists.",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "502", description = "Bad Gateway",  content = {@Content(schema = @Schema(implementation = AppError.class ))}),
			@ApiResponse(responseCode = "503", description = "Service Unavailable",  content = {@Content(schema = @Schema(implementation = AppError.class ))})
	})
	@PostMapping(value = "/search/ct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CTResults searchCTs(@RequestBody SearchRequest request,
							   @RequestParam(required = false, defaultValue = "-180") double longitudeLeft,
							   @RequestParam(required = false, defaultValue = "-90") double latitudeLower,
							   @RequestParam(required = false, defaultValue = "180") double longitudeRight,
							   @RequestParam(required = false, defaultValue = "90") double latitudeUpper,
							   @RequestParam(required = false, defaultValue = "0") int offset,
							   @RequestParam(required = false, defaultValue = "100") int limit,
							   @RequestParam(name = "mode", required = false, defaultValue = "persistableReference") String reprMode) {
		assertRange(offset, limit);
		try {
			RepresentationMode mode = RepresentationMode.getRepresentationMode(reprMode);
			SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
			CTResults results = catalog.searchCTs(request, boundingBox, offset, limit, mode);
			this.auditLogger.searchCtSuccess(
				Collections.singletonList(results.toString()),
				Collections.singletonList(CrsCatalogRole.CRS_CATALOG_AUTHENTICATED_USER)
			);
			return results;
    	}
        catch(Exception ex) {
			log.warning(ex.toString(), ex);
            throw AppException.createBadRequest(ex.getMessage());
		}
    }

    private void assertRange(int offset, int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        if(offset < 0)
            stringBuilder.append("Offset can not be negative. ");

        if(limit < -1)
            stringBuilder.append("'-1' is the only valid negative value and means all. Other negative values for limit is invalid.");

        if(stringBuilder.length() > 0)
            throw AppException.createBadRequest(stringBuilder.toString());
    }
}
