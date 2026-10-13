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
package org.opengroup.osdu.crs.model;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.opengroup.osdu.crs.model.search.*;
import org.opengroup.osdu.crs.model.search.parser.SpatialUtility;

/**
 *  CatalogImpl is a class which encapsulates all the data related to coordinate reference systems,
 *  cartographic transformations, and area of uses
 */
public class CatalogImpl implements Catalog {

    @Expose @SerializedName("name")
    private String name;
    
    @Expose @SerializedName("lastModified")
	private String lastModified;

    @Expose @SerializedName("description")
    private String description;
    
    @Expose @SerializedName("nextId")
    int nextId;	
    
    @Expose @SerializedName("listOfLateBoundCRSs")
    private List<LateBoundCRSImpl> lateBoundCRSes;

	public int getLateBoundCRSesCount() {
		return lateBoundCRSesCount;
	}

	@Expose @SerializedName("lateBoundCRSesCount")
	private int lateBoundCRSesCount;


	@Expose @SerializedName("listOfEarlyBoundCRSs")
    private List<EarlyBoundCRSImpl> earlyBoundCRSes;

	public int getEarlyBoundCRSesCount() {
		return this.earlyBoundCRSesCount ;
	}

	@Expose @SerializedName("earlyBoundCRSesCount")
	private int earlyBoundCRSesCount;


	@Expose @SerializedName("listOfCompoundCRSs")
    private List<CompoundCRSImpl> compoundCRSImpls;

	public int getCompoundCRSesCount() {
		return compoundCRSesCount;
	}

	@Expose @SerializedName("compoundCRSesCount")
	private int compoundCRSesCount;


	@Expose @SerializedName("listOfSingleCTs")
    private List<SingleCTImpl> singleCTs;

	public int getSingleCTsCount() {
		return singleCTsCount;
	}

	@Expose @SerializedName("singleCTsCount")
	private int singleCTsCount;


	@Expose @SerializedName("listOfCompoundCTs")
    private List<CompoundCTImpl> compoundCTs;

	public int getCompoundCTsCount() {
		return compoundCTsCount;
	}

	@Expose @SerializedName("compoundCTsCount")
	private int compoundCTsCount;


    @Expose @SerializedName("listOfAreaOfUses")
    private List<AreaOfUseImpl> areaOfUseImpls;

	public int getAreaOfUseCount() {
		return areaOfUseCount;
	}

	@Expose @SerializedName("areaOfUseCount")
	private int areaOfUseCount;


	private Map<String, AreaOfUseImpl> allAreasOfUse;
    private Map<String, CRSImpl> allCRSes;
    private Map<String, CTImpl> allCTs;

	private Indexer		indexer = null;
    private Searcher	searcher = null; 
    
    private static final Logger log = Logger.getLogger(CatalogImpl.class.getName());

	/**
	 * Constructor
	 */
    public CatalogImpl()
    {
    	lateBoundCRSes = new ArrayList<>();    	
    	earlyBoundCRSes = new ArrayList<>();    	
    	compoundCRSImpls = new ArrayList<>();
    	
    	singleCTs = new ArrayList<>();    	
    	compoundCTs = new ArrayList<>();    
    	
    	areaOfUseImpls = new ArrayList<>();
 
    	searcher = null;
    }

	@Override
	@JsonIgnore
	public Catalog getCatalogResponse() {
    	CatalogImpl response = new CatalogImpl();
    	response.name = this.name;
    	response.description = this.description;
    	response.lastModified = this.lastModified;
		response.lateBoundCRSesCount = this.lateBoundCRSes.size();
		response.earlyBoundCRSesCount = this.earlyBoundCRSes.size();
		response.compoundCRSesCount = this.compoundCRSImpls.size();
		response.singleCTsCount = this.singleCTs.size();
		response.compoundCTsCount = this.compoundCTs.size();
		response.areaOfUseCount = this.areaOfUseImpls.size();
    	return response;
	}

	/**
	 * Called by createCatalog() initialize the contents of the catalog by building up the various internal
	 * data structures and linking them up properly. It then builds up the indexes to support searching
	 * @throws Exception indicates failure to initialize the catalog
	 */
    private void initCatalog() throws Exception {
    	
    	allAreasOfUse = new HashMap<>();
     	for(AreaOfUseImpl aou : areaOfUseImpls) {
     		allAreasOfUse.put(aou.getId(), aou);
    	}

    	allCRSes = new HashMap<>();
     	for(LateBoundCRSImpl crs : lateBoundCRSes) {
     		allCRSes.put(crs.getId(), crs);
    	}
     	for(EarlyBoundCRSImpl crs : earlyBoundCRSes) {
     		allCRSes.put(crs.getId(), crs);
    	}
     	for(CompoundCRSImpl crs : compoundCRSImpls) {
     		allCRSes.put(crs.getId(), crs);
    	}

        // fix the vertical and horizon CRSes in CompoundCRSes  
     	for(CompoundCRSImpl crs : compoundCRSImpls) {
     		
     		CompoundCRSEssenceImpl essence = (CompoundCRSEssenceImpl)crs.getEssence();
     		
     		CRSImpl hCRS = (CRSImpl)getCRS(essence.getHorizontalCRSEssence());
     		crs.setHorizontalCRS(hCRS);
     		
     		CRSImpl vCRS = (CRSImpl)getCRS(essence.getVerticalCRSEssence());
     		crs.setVerticalCRS(vCRS);
    	}
     	
        // fix the BaseCoordinateReference relationships after all elements are restored in the allCoodrinateReferenceSystems
        // fix any DeprecationInfoImpl superseded CRS relationships after all elements are restored in the allCoodrinateReferenceSystems
        // look-up failures will throw
		Iterator<Entry<String, CRSImpl>> crsIt = allCRSes.entrySet().iterator();
		while(crsIt.hasNext())
		{
			CRSImpl crs = crsIt.next().getValue();
	        if(crs.getBaseCRSId() != null)
	            crs.setBaseCRS(allCRSes.get(crs.getBaseCRSId()));
	        CRSDeprecationInfoImpl deprecationInfo = (CRSDeprecationInfoImpl)crs.getDeprecationInfo();
	        if(deprecationInfo != null && deprecationInfo.getSupersededByCRSId() != null)
	            deprecationInfo.setSupersededByCRS(allCRSes.get(deprecationInfo.getSupersededByCRSId()));
            if(crs.getAreaOfUseId() != null) 
        		crs.setAreaOfUse(allAreasOfUse.get(crs.getAreaOfUseId()));
        }

    	allCTs = new HashMap<>(); 
     	for(SingleCTImpl ct : singleCTs) {
     		allCTs.put(ct.getId(), ct);
    	}
     	for(CompoundCTImpl ct : compoundCTs) {
     		allCTs.put(ct.getId(), ct);
    	}
     	
        // fix component CTs in CompoundCTs
     	for(CompoundCTImpl cct : compoundCTs) {
     		CompoundCTEssenceImpl essence = (CompoundCTEssenceImpl)cct.getEssence();
     		List<CTEssence> prs = essence.getCTEssences();
     		List<SingleCT> cts = new ArrayList<>();
     		for(CTEssence ctEssence : prs) {
				SingleCT ct = (SingleCT)getCT(ctEssence);
				cts.add(ct);
     		}
     		cct.setCTs(cts);
    	}
     	
        // fix to and from CRS 
        // fix deprecation 
		Iterator<Entry<String, CTImpl>> ctIt = allCTs.entrySet().iterator();
		while(ctIt.hasNext())
		{
			CTImpl ct = ctIt.next().getValue();
            ct.setFromCRS(allCRSes.get(ct.getFromCRSId()));
            ct.setToCRS(allCRSes.get(ct.getToCRSId()));
            
	        CTDeprecationInfoImpl deprecationInfo = (CTDeprecationInfoImpl)ct.getDeprecationInfo();
	        if(deprecationInfo != null && deprecationInfo.getSupersededByCTId() != null)
	            deprecationInfo.setSupersededByCT(allCTs.get(deprecationInfo.getSupersededByCTId()));
            
            if(ct.getAreaOfUseId() != null) 
        		ct.setAreaOfUse(allAreasOfUse.get(ct.getAreaOfUseId()));
        }

        // fix EarlyBoundCRSImpl's LateBoundCRSImpl and CTImpl
     	for(EarlyBoundCRSImpl crs : earlyBoundCRSes) {
     		EarlyBoundCRSEssenceImpl essence = (EarlyBoundCRSEssenceImpl)crs.getEssence();
     		LateBoundCRSImpl lbCRS = (LateBoundCRSImpl)getLateBoundCRS(essence.getLateBoundCRSEssence());
     		crs.setLateBoundCRS(lbCRS);
     		CTImpl ct = (CTImpl)getCT(essence.getCTEssence());
     		crs.setCT(ct);
    	}
    }

	/**
	 * Create, initialize, and return the catalog instance from given reader.
     * @param reader input source reader
	 * @return {@link CatalogImpl}
	 * @throws Exception indicates failure to create the catalog
	 */
    public static CatalogImpl createCatalog(Reader reader) throws Exception {
		if(reader == null) {
			throw new IOException("Null reader");
		}
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();
        CatalogImpl catalogImpl = gson.fromJson(reader, CatalogImpl.class);
        catalogImpl.initCatalog();
		catalogImpl.refreshIndex();
        return catalogImpl;
    }

	/**
	 * Get the catalog attributes.
	 * @return catalog attributes
	 */
	public CatalogAttributes getAttributes() {
		CatalogAttributes attributes = new CatalogAttributesImpl(name, description, lastModified);
		return attributes;
	}

	/**
	 * Get all the LateBoundCRSes in the catalog.
	 * @return list of {@link LateBoundCRSImpl}s in the catalog
	 */
	public List<LateBoundCRS> getLateBoundCRSes() {
		List<LateBoundCRS> results = new ArrayList<>();
		for (LateBoundCRSImpl crs: lateBoundCRSes) {
			results.add(crs);
		}
		return results;
	}

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
	public LateBoundCRS getLateBoundCRS(LateBoundCRSEssence essence) {
		if(essence == null) {
			return null;
		}
		for(LateBoundCRSImpl crs : lateBoundCRSes) {
			if(essence.equals(crs.getEssence())) 
				return crs;
		}
		CRSDeprecationInfoImpl deprecationInfo = new CRSDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
				DeprecationInfoImpl.UNRESOLVED_REMARKS,
				"");
		LateBoundCRSImpl crs = new LateBoundCRSImpl();
		crs.setEssence(essence);
		crs.setDeprecationInfo(deprecationInfo);
		return crs;
	}

	/**
	 * Get all the EarlyBoundCRSes in the catalog.
	 * @return list of {@link EarlyBoundCRS}s in the catalog
	 */
	public List<EarlyBoundCRS> getEarlyBoundCRSes() {
		List<EarlyBoundCRS> results = new ArrayList<>();
		for (EarlyBoundCRSImpl crs: earlyBoundCRSes) {
			results.add(crs);
		}
		return results;
	}

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
	public EarlyBoundCRS getEarlyBoundCRS(EarlyBoundCRSEssence essence) {
		if(essence == null) {
			return null;
		}
		for(EarlyBoundCRSImpl crs : earlyBoundCRSes) {
			if(essence.equals(crs.getEssence())) 
				return crs;
		}
		CRSDeprecationInfoImpl deprecationInfo = new CRSDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
				DeprecationInfoImpl.UNRESOLVED_REMARKS,
				"");
		EarlyBoundCRSImpl crs = new EarlyBoundCRSImpl();
		crs.setEssence(essence);
		crs.setDeprecationInfo(deprecationInfo);
		return crs;
	}

	/**
	 * Get all the CompoundCRSes in the catalog.
	 * @return list of {@link CompoundCRS}s in the catalog
	 */
	public List<CompoundCRS> getCompoundCRSes() {
		List<CompoundCRS> results = new ArrayList<>();
		for (CompoundCRSImpl crs: compoundCRSImpls) {
			results.add(crs);
		}
		return results;
	}

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
	public CompoundCRS getCompoundCRS(CompoundCRSEssence essence) {
		if(essence == null) {
			return null;
		}
		for(CompoundCRSImpl crs : compoundCRSImpls) {
			if(essence.equals(crs.getEssence())) 
				return crs;
		}
		CRSDeprecationInfoImpl deprecationInfo = new CRSDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
				DeprecationInfoImpl.UNRESOLVED_REMARKS,
				"");
		CompoundCRSImpl crs = new CompoundCRSImpl();
		crs.setEssence(essence);
		crs.setDeprecationInfo(deprecationInfo);
		return crs;
	}

	/**
	 * Get all the SingleCTs in the catalog.
	 * @return list of {@link SingleCT}s in the catalog
	 */
	public List<SingleCT> getSingleCTs() {
		List<SingleCT> results = new ArrayList<>();
		for (SingleCTImpl CT: singleCTs) {
			results.add(CT);
		}
		return results;
	}

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
	public SingleCT getSingleCT(SingleCTEssence essence) {
		if(essence == null) {
			return null;
		}
		for(SingleCTImpl ct : singleCTs) {
			if(essence.equals(ct.getEssence())) 
				return ct;
		}
		CTDeprecationInfoImpl deprecationInfo = new CTDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
				DeprecationInfoImpl.UNRESOLVED_REMARKS,
				"");
		SingleCTImpl ct = new SingleCTImpl();
		ct.setEssence(essence);
		ct.setDeprecationInfo(deprecationInfo);
		return ct;
	}

	/**
	 * Get all the CompoundCTs in the catalog.
	 * @return list of {@link CompoundCT}s in the catalog
	 */
	public List<CompoundCT> getCompoundCTs() {
		List<CompoundCT> results = new ArrayList<>();
		for (CompoundCTImpl CT : compoundCTs) {
			results.add(CT);
		}
		return results;
	}

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
	public CompoundCT getCompoundCT(CompoundCTEssence essence) {
		if(essence == null) {
			return null;
		}
		for(CompoundCTImpl ct : compoundCTs)
			{
			if(essence.equals(ct.getEssence())) 
				return ct;
		}
		CTDeprecationInfoImpl deprecationInfo = new CTDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
				DeprecationInfoImpl.UNRESOLVED_REMARKS,
				"");
		CompoundCTImpl ct = new CompoundCTImpl();
		ct.setEssence(essence);
		ct.setDeprecationInfo(deprecationInfo);
		return ct;
	}

	/**
	 * Get all the AreasOfUse in the catalog.
	 * @return list of {@link AreaOfUse}s in the catalog
	 */
	public List<AreaOfUse> getAreasOfUse() {
		List<AreaOfUse> results = new ArrayList<>();
		for (AreaOfUseImpl area: areaOfUseImpls) {
			results.add(area);
		}
		return results;
	}

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
	public AreaOfUse getAreaOfUse(AreaOfUseEssence essence) {
		if(essence == null) {
			return null;
		}
		for(AreaOfUseImpl au : areaOfUseImpls)
		{
		    if(essence.equals(au.getEssence())) {
				log.info("Catalog area of use essence " + au.getEssence().toJsonString() + " matched");
			    return au;
			}
		}
		log.info("No match found for requested area of use essence " + essence.toJsonString() + " in the Catalog");
		AreaOfUseDeprecationInfoImpl deprecationInfo;
		deprecationInfo = new AreaOfUseDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
														   DeprecationInfoImpl.UNRESOLVED_REMARKS,
														   "");
		AreaOfUseImpl au = new AreaOfUseImpl();
		au.setEssence(essence);
		au.setDeprecationInfo(deprecationInfo);
		return au;
	}

	/**
	 * Get all the CRSes in the catalog.
	 * @return list of {@link CRS}s in the catalog
	 */
	@JsonIgnore
	public List<CRS> getAllCRSes() {
		List<CRS> results = new ArrayList<>();
		for (CRSImpl crs: allCRSes.values()) {
			results.add(crs);
		}
		return results;
	}

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
	public CRS getCRS(CRSEssence essence) throws IllegalArgumentException {
		if(essence == null) {
			return null;
		}
		Iterator<Entry<String, CRSImpl>> it = allCRSes.entrySet().iterator();
		while(it.hasNext())
		{
			CRSImpl crs = it.next().getValue();
			Essence essence2 = crs.getEssence();
			if(essence.equals(essence2))
				return crs;
		}

		CRSImpl crs = null;
		if(essence.getType().equals(CRSEssenceImpl.EBCRS)) {
			EarlyBoundCRSImpl ebcrs = new EarlyBoundCRSImpl();
			ebcrs.setEssence((EarlyBoundCRSEssenceImpl)essence);
			crs = ebcrs;
		}
		else if(essence.getType().equals(CRSEssenceImpl.LBCRS)) {
			LateBoundCRSImpl lbcrs = new LateBoundCRSImpl();
			lbcrs.setEssence((LateBoundCRSEssenceImpl)essence);
			crs = lbcrs;
		}
		else if(essence.getType().equals(CRSEssenceImpl.CCRS)) {
			CompoundCRSImpl ccrs = new CompoundCRSImpl();
			ccrs.setEssence((CompoundCRSEssenceImpl)essence);
			crs = ccrs;
		}
		else
			throw new IllegalArgumentException("Malformed essence");

		CRSDeprecationInfoImpl deprecationInfo; 
		deprecationInfo = new CRSDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
														DeprecationInfoImpl.UNRESOLVED_REMARKS,
														"");
		crs.setDeprecationInfo(deprecationInfo);
		return crs;
	}

	/**
	 * Get all the CTs in the catalog.
	 * @return list of {@link CT}s in the catalog
	 */
	@JsonIgnore
	public List<CT> getAllCTs() {
		List<CT> results = new ArrayList<>();
		for (CTImpl CT: allCTs.values()) {
			results.add(CT);
		}
		return results;
	}

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
	public CT getCT(CTEssence essence) throws IllegalArgumentException {
		if(essence == null) {
			return null;
		}
		Iterator<Entry<String, CTImpl>> it = allCTs.entrySet().iterator();
		while(it.hasNext())
		{
			CTImpl ct = it.next().getValue();
			Essence essence2 = ct.getEssence();
			if(essence.equals(essence2))
					return ct;
		}

		CTImpl ct = null;
		if(essence.getType().equals(CTEssenceImpl.SCT)) {
			SingleCTImpl sCT = new SingleCTImpl();
			sCT.setEssence((SingleCTEssenceImpl)essence);
			ct = sCT;
		}
		else if(essence.getType().equals(CTEssenceImpl.CCT)) {
			CompoundCTImpl cCT = new CompoundCTImpl();
			cCT.setEssence((CompoundCTEssenceImpl)essence);
			ct = cCT;
		}
		else
			throw new IllegalArgumentException("Malformed CT essence");

		CTDeprecationInfoImpl deprecationInfo;
		deprecationInfo = new CTDeprecationInfoImpl(DeprecationInfoImpl.UNRESOLVED_STATE,
													DeprecationInfoImpl.UNRESOLVED_REMARKS,
													"");
		ct.setDeprecationInfo(deprecationInfo);
		return ct;
	}

    /**
	 * Perform indexing on the catalog data to support searching. First an {@link Indexer} object is created.
	 * Then all {@link AreaOfUseImpl}s, {@link CRSImpl}s, and {@link CTImpl}s
	 * are indexed. Finally, a {@link Searcher} object is instanciated to support client searching requests
	 * @throws Exception indicates failure to refresh the index
	 */
	private void refreshIndex() throws Exception {

		// index only on the first search call to create the searcher object
		if(searcher == null) {
			indexer = new Indexer();
			indexAllAreasOfUse(indexer);
			indexAllCRSes(indexer);
			indexAllCTs(indexer);
			indexer.commit();
			indexer.closeIndexWriter();
			searcher = new Searcher(indexer, this);
		}
	}

    /**
     * Perform indexing on {@link CRSImpl}s in the catalog
     * @param indexer the {@link Indexer} object
	 * @throws Exception indicates failure to index the coordinate reference systems
     */
	private void indexAllCRSes(Indexer indexer) throws Exception {

        for(CRSImpl crs : allCRSes.values())
        	indexer.indexCRS(crs);
	}

    /**
     * Perform indexing on {@link CTImpl}s in the catalog
     * @param indexer the {@link Indexer} object
	 * @throws Exception indicates failure to index the cartographic transforms
     */
	private void indexAllCTs(Indexer indexer) throws  Exception {

		for(CTImpl CT : allCTs.values())
	       	indexer.indexCT(CT);
	}

    /**
     * Perform indexing on {@link AreaOfUseImpl}s in the catalog
     * @param indexer the {@link Indexer} object
	 * @throws Exception indicates failure to index the areas of uses
     */
	private void indexAllAreasOfUse(Indexer indexer) throws Exception {

        for(AreaOfUseImpl aou : areaOfUseImpls)
        	indexer.indexAreaOfUse(aou);
	}

	/**
	 * Search the AreasOfUse by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
	 * @param offset  The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default.
	 * @param limit   The maximum number of the AreasOfUse returned. It is optional and is 100 by default.
	 * @param mode    The requested {@link RepresentationMode}
	 * @return AreaOfUseResults
	 * @throws Exception indicates something goes wrong
	 */
	public AreaOfUseResults searchAreasOfUse(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode)
            throws Exception {

		refreshIndex();
		SpatialUtility.assertSphericalBoundingBox(boundingBox);
        return searcher.searchAreasOfUse(request.getQuery(), (SphericalBoundingBoxImpl)boundingBox, offset, limit, mode);
	}

    /**
     * Search the CRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CRSes returned. It is optional and is 100 by default.
     * @return CRSResults
     * @throws Exception indicates something goes wrong
     */
    public CRSResults searchCRSes(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode) throws Exception {

		refreshIndex();
		SpatialUtility.assertSphericalBoundingBox(boundingBox);
        return searcher.searchCRSes(request.getQuery(), (SphericalBoundingBoxImpl)boundingBox, offset, limit, mode);
    }

    /**
     * Search the LateBoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all LateBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the LateBoundCRSes returned. It is optional and is 100 by default.
     * @return LateBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public LateBoundCRSResults searchLateBoundCRSes(SearchRequest request, int offset, int limit) throws Exception {

		refreshIndex();

        return searcher.searchLateBoundCRSes(request.getQuery(), offset, limit);
    }

    /**
     * Search the EarlyBoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all EarlyBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the EarlyBoundCRSes returned. It is optional and is 100 by default.
     * @return EarlyBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public EarlyBoundCRSResults searchEarlyBoundCRSes(SearchRequest request, int offset, int limit) throws Exception {

		refreshIndex();

        return searcher.searchEarlyBoundCRSes(request.getQuery(), offset, limit);
    }

    /**
     * Search the CompoundCRSes by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all CompoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CompoundCRSes returned. It is optional and is 100 by default.
     * @return CompoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public CompoundCRSResults searchCompoundCRSes(SearchRequest request, int offset, int limit) throws Exception {

		refreshIndex();

        return searcher.searchCompoundCRSes(request.getQuery(), offset, limit);
    }

    /**
     * Search the SearchCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
	 * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the CTs returned. It is optional and is 100 by default.
     * @return CTResults
     * @throws Exception indicates something goes wrong
     */
	public CTResults searchCTs(SearchRequest request, SphericalBoundingBox boundingBox, int offset, int limit, RepresentationMode mode) throws Exception {

		refreshIndex();
		SpatialUtility.assertSphericalBoundingBox(boundingBox);
        return searcher.searchCTs(request.getQuery(), (SphericalBoundingBoxImpl)boundingBox, offset, limit, mode);
	}

    /**
     * Search the SearchSingleCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all SingleCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the single transformations returned. It is optional and is 100 by default.
     * @return SingleCTResults
     * @throws Exception indicates something goes wrong
     */
    public SingleCTResults searchSingleCTs(SearchRequest request, int offset, int limit) throws Exception {

		refreshIndex();

        return searcher.searchSingleCTs(request.getQuery(), offset, limit);
    }

    /**
     * Search the SearchCompoundCTs by keyword and return the results in given range
	 * @param request The {@link SearchRequest} containing the keyword query
     * @param offset  The offset of the first item in the list of all CompoundCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the compound transformations returned. It is optional and is 100 by default.
     * @return CompoundCTResults
     * @throws Exception indicates something goes wrong
     */
	public CompoundCTResults searchCompoundCTs(SearchRequest request, int offset, int limit) throws Exception {

		refreshIndex();

        return searcher.searchCompoundCTs(request.getQuery(), offset, limit);
	}

}

