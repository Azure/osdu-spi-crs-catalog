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
package org.opengroup.osdu.crs.model.interfaces;

import java.util.*;

import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.opengroup.osdu.crs.model.search.*;

/**
 *  Catalog is an interface which encapsulates all the data and their functionality related to coordinate reference systems,
 *  cartographic transformations, and areas of use
 */
public interface Catalog {


	/**
	 * Get the catalog attributes.
	 * @return catalog attributes
	 */
	public CatalogAttributes getAttributes();

	/**
	 * Get all the LateBoundCRSes in the catalog.
	 * @return list of {@link LateBoundCRS}s in the catalog
	 */
	public List<LateBoundCRS> getLateBoundCRSes();

	/**
	 * Get the {@link LateBoundCRS} corresponding to the essence. If a
	 * {@link LateBoundCRS} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link LateBoundCRS}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link LateBoundCRS} identifier
	 * @return {@link LateBoundCRS} if found or created; null otherwise
	 */
	public LateBoundCRS getLateBoundCRS(LateBoundCRSEssence essence);

	/**
	 * Get all the EarlyBoundCRSes in the catalog.
	 * @return list of {@link EarlyBoundCRS}s in the catalog
	 */
	public List<EarlyBoundCRS> getEarlyBoundCRSes();

	/**
	 * Get the {@link EarlyBoundCRS} corresponding to the essence. If a
	 * {@link EarlyBoundCRS} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link EarlyBoundCRS}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link LateBoundCRS} identifier
	 * @return {@link EarlyBoundCRS} if found or created; null otherwise
	 */
	public EarlyBoundCRS getEarlyBoundCRS(EarlyBoundCRSEssence essence);

	/**
	 * Get all the CompoundCRSes in the catalog.
	 * @return list of {@link CompoundCRS}s in the catalog
	 */
	public List<CompoundCRS> getCompoundCRSes();

	/**
	 * Get the {@link CompoundCRS} corresponding to the essence. If a
	 * {@link CompoundCRS} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link CompoundCRS}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link CompoundCRS} identifier
	 * @return {@link CompoundCRS} if found or created; null otherwise
	 */
	public CompoundCRS getCompoundCRS(CompoundCRSEssence essence);

	/**
	 * Get all the SingleCTs in the catalog.
	 * @return list of {@link SingleCT}s in the catalog
	 */
	public List<SingleCT> getSingleCTs();

	/**
	 * Get the {@link SingleCT} corresponding to the essence. If a
	 * {@link SingleCT} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link SingleCT}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link SingleCT} identifier
	 * @return {@link SingleCT} if found or created; null otherwise
	 */
	public SingleCT getSingleCT(SingleCTEssence essence);

	/**
	 * Get all the CompoundCTs in the catalog.
	 * @return list of {@link CompoundCT}s in the catalog
	 */
	public List<CompoundCT> getCompoundCTs();

	/**
	 * Get the {@link CompoundCT} corresponding to the essence. If a
	 * {@link CompoundCT} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link CompoundCT}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link CompoundCT} identifier
	 * @return {@link CompoundCT} if found or created; null otherwise
	 */
	public CompoundCT getCompoundCT(CompoundCTEssence essence);

	/**
	 * Get all the AreasOfUse in the catalog.
	 * @return list of {@link AreaOfUse}s in the catalog
	 */
	public List<AreaOfUse> getAreasOfUse();

	/**
	 * Get the {@link AreaOfUse} corresponding to the essence. If a
	 * {@link AreaOfUse} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link AreaOfUse}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link AreaOfUse} identifier
	 * @return {@link AreaOfUse} if found or created; null otherwise
	 */
	public AreaOfUse getAreaOfUse(AreaOfUseEssence essence);

	/**
	 * Get all the CRSes in the catalog.
	 * @return list of {@link CRS}s in the catalog
	 */
	public List<CRS> getAllCRSes();

	/**
	 * Get the {@link CRS} corresponding to the essence. If a
	 * {@link CRS} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link CRS}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link CRS} identifier
	 * @return {@link CRS} if found or created; null otherwise
	 * @throws IllegalArgumentException indicates the input essence is not recognized
	 */
	public CRS getCRS(CRSEssence essence) throws IllegalArgumentException;
	/**
	 * Get all the CTs in the catalog.
	 * @return list of {@link CT}s in the catalog
	 */
	public List<CT> getAllCTs();

	/**
	 * Get the {@link CT} corresponding to the essence. If a
	 * {@link CT} instance with the same essence is found in
	 * the catalog, that instance is returned.
	 * Otherwise, an attempt is made to create a new instance of {@link CT}
	 * with the input essence. If successful, the new object is marked with proper depreciation
	 * state and remarks information and returned.
	 * If the creation fails, null if returned.
	 * @param essence the stable and unique {@link CT} identifier
	 * @return {@link CT} if found or created; null otherwise
	 * @throws IllegalArgumentException indicates the input essence is not recognized
	 */
	public CT getCT(CTEssence essence) throws IllegalArgumentException;

	/**
	 * Search the AreasOfUse by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
	 * @param offset  The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default.
	 * @param limit   The maximum number of the AreaOfUses returned. It is optional and is 100 by default.
	 * @param mode    The requested {@link RepresentationMode}
	 * @return AreaOfUseResults
	 * @throws Exception indicates something goes wrong
	 */
	public AreaOfUseResults searchAreasOfUse(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode)
            throws Exception;

    /**
     * Search the CRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CRSes returned. It is optional and is 100 by default.
	 * @param mode    The requested {@link RepresentationMode}
     * @return CRSResults
     * @throws Exception indicates something goes wrong
     */
    public CRSResults searchCRSes(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode)
            throws Exception;

    /**
     * Search the LateBoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all LateBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the LateBoundCRSes returned. It is optional and is 100 by default.
     * @return LateBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public LateBoundCRSResults searchLateBoundCRSes(SearchRequest request, int offset, int limit)
            throws Exception;

    /**
     * Search the EarlyBoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all EarlyBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the EarlyBoundCRSes returned. It is optional and is 100 by default.
     * @return EarlyBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public EarlyBoundCRSResults searchEarlyBoundCRSes(SearchRequest request, int offset, int limit)
            throws Exception;

    /**
     * Search the CompoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all CompoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CompoundCRSes returned. It is optional and is 100 by default.
     * @return CompoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public CompoundCRSResults searchCompoundCRSes(SearchRequest request, int offset, int limit)
            throws Exception;
    /**
     * Search the SearchCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the CTs returned. It is optional and is 100 by default.
	 * @param mode    The requested {@link RepresentationMode}
     * @return CTResults
     * @throws Exception indicates something goes wrong
     */
	public CTResults searchCTs(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode)
            throws Exception;

    /**
     * Search the SearchSingleCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all SingleCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the single transformations returned. It is optional and is 100 by default.
     * @return SingleCTResults
     * @throws Exception indicates something goes wrong
     */
    public SingleCTResults searchSingleCTs(SearchRequest request, int offset, int limit)
            throws Exception;
    /**
     * Search the SearchCompoundCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all CompoundCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the compound transformations returned. It is optional and is 100 by default.
     * @return CompoundCTResults
     * @throws Exception indicates something goes wrong
     */
	public CompoundCTResults searchCompoundCTs(SearchRequest request, int offset, int limit)
            throws Exception;

	/**
	 * Get an empty catalog instance for the getCatalog endpoint; no arrays
	 * @return A copy of the catalog without array contents
	 */
	public Catalog getCatalogResponse();
	public int getLateBoundCRSesCount();
	public int getEarlyBoundCRSesCount();
	public int getCompoundCRSesCount();
	public int getSingleCTsCount();
	public int getCompoundCTsCount();
	public int getAreaOfUseCount();
}

