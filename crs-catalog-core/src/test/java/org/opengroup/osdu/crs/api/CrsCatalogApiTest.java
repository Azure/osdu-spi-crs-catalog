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

package org.opengroup.osdu.crs.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.opengroup.osdu.crs.model.request.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.opengroup.osdu.crs.util.AppException;
import org.opengroup.osdu.crs.model.AreaOfUseEssenceImpl;
import org.opengroup.osdu.crs.model.AreaOfUseImpl;
import org.opengroup.osdu.crs.model.CRSEssenceImpl;
import org.opengroup.osdu.crs.model.CatalogAttributesImpl;
import org.opengroup.osdu.crs.model.CatalogImpl;
import org.opengroup.osdu.crs.model.CompoundCRSEssenceImpl;
import org.opengroup.osdu.crs.model.CompoundCRSImpl;
import org.opengroup.osdu.crs.model.CompoundCTEssenceImpl;
import org.opengroup.osdu.crs.model.CompoundCTImpl;
import org.opengroup.osdu.crs.model.EarlyBoundCRSEssenceImpl;
import org.opengroup.osdu.crs.model.EarlyBoundCRSImpl;
import org.opengroup.osdu.crs.model.LateBoundCRSEssenceImpl;
import org.opengroup.osdu.crs.model.LateBoundCRSImpl;
import org.opengroup.osdu.crs.model.SingleCTEssenceImpl;
import org.opengroup.osdu.crs.model.SingleCTImpl;
import org.opengroup.osdu.crs.model.SphericalBoundingBoxImpl;
import org.opengroup.osdu.crs.model.TestUtils;
import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.search.*;

@ExtendWith(MockitoExtension.class)
public class CrsCatalogApiTest {

	@Mock
	private static Catalog	catalogMock;

	@Mock
	private AuditLogger auditLogger;

    @Mock
    private JaxRsDpsLog jaxRsDpsLog;

	@InjectMocks
    private static CrsCatalogApi crsCatalogApi;

    @BeforeAll
    public static void setUpBeforeClass() {
        crsCatalogApi = new CrsCatalogApi(catalogMock);
    }

    @BeforeEach
    public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testGetCatalogAttributes() {
		assertNotNull(crsCatalogApi);
        try {
            when(catalogMock.getAttributes()).thenReturn(new CatalogAttributesImpl("name", "description", "20180731"));
            CatalogAttributes catalogAttributes = crsCatalogApi.getCatalogAttributes();
            assertNotNull(catalogAttributes);
            assertEquals("name", catalogAttributes.getName());
            assertEquals("description", catalogAttributes.getDescription());
            assertEquals("20180731", catalogAttributes.getLastModified());
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
	}

	@Test
	public void testGetCatalog() {
        assertNotNull(crsCatalogApi);
        try {
            when(catalogMock.getCatalogResponse()).thenReturn(new CatalogImpl());
			Catalog catalog = crsCatalogApi.getCatalog();
        	assertNotNull(catalog);
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
	}

    @Test
    public void testGetAllCRSes() {
        assertNotNull(crsCatalogApi);
        try {
            List<CRS>   allCRSes = new ArrayList<>();
            allCRSes.add(new EarlyBoundCRSImpl());
            when(catalogMock.getAllCRSes()).thenReturn(allCRSes);
	        CRSResults  results = crsCatalogApi.getAllCRSes(0, 100, "essence");
            assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " CRSes");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetCRS() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssence.json");
            LateBoundCRSEssence lateBoundCRSEssence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            CRSEssenceImpl anyCRSEssence = (CRSEssenceImpl) lateBoundCRSEssence;
            LateBoundCRSImpl   lateBoundCRS = new LateBoundCRSImpl();
            lateBoundCRS.setEssence(lateBoundCRSEssence);
            when(catalogMock.getCRS((CRSEssence)lateBoundCRSEssence)).thenReturn((CRS)lateBoundCRS);
            CRSRequest  request = new CRSRequest(anyCRSEssence);
	        CRS  result = crsCatalogApi.getCRS(request, "essence");
            assertNotNull(result);
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetCRSBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            CRSEssenceImpl essence = null;
            try {
                String essenceJson = TestUtils.readFile("LateBoundCRSEssence.json");
                essence = CRSEssenceImpl.fromJson(essenceJson);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                fail();
            }
            when(catalogMock.getCRS(any())).thenThrow(AppException.class);
            CRSRequest  request = new CRSRequest(essence);
            crsCatalogApi.getCRS(request, "essence");
        });
    }

    @Test
    public void testGetLateBoundCRSes() {
        assertNotNull(crsCatalogApi);
        try {
            List<LateBoundCRS>   lateBoundCRSes = new ArrayList<>();
            lateBoundCRSes.add(new LateBoundCRSImpl());
            when(catalogMock.getLateBoundCRSes()).thenReturn(lateBoundCRSes);
	        LateBoundCRSResults results = crsCatalogApi.getLateBoundCRSes(0, 100, "Persistable");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " LateBoundCRSes");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetLateBoundCRS() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssence.json");
            LateBoundCRSEssenceImpl essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            LateBoundCRS crs = new LateBoundCRSImpl();
            when(catalogMock.getLateBoundCRS(essence)).thenReturn(crs);
            LateBoundCRSRequest  request = new LateBoundCRSRequest(essence);
            LateBoundCRS  result = crsCatalogApi.getLateBoundCRS(request, "Essence");
            assertNotNull(result);
            assertEquals(crs, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetLateBoundCRSBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getLateBoundCRS(any())).thenThrow(AppException.class);
            LateBoundCRSRequest  request = new LateBoundCRSRequest(null, null);
            crsCatalogApi.getLateBoundCRS(request, null);
        });
    }


    @Test
    public void testGetEarlyBoundCRSes() {
        assertNotNull(crsCatalogApi);
        try {
            List<EarlyBoundCRS>   earlyBoundCRSes = new ArrayList<>();
            earlyBoundCRSes.add(new EarlyBoundCRSImpl());
            when(catalogMock.getEarlyBoundCRSes()).thenReturn(earlyBoundCRSes);
	        EarlyBoundCRSResults results = crsCatalogApi.getEarlyBoundCRSes(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " EarlyBoundCRSes");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetEarlyBoundCRS() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("EarlyBoundCRSEssence.json");
            EarlyBoundCRSEssenceImpl essence = EarlyBoundCRSEssenceImpl.fromJson(essenceJson);
            EarlyBoundCRS   crs = new EarlyBoundCRSImpl();
            when(catalogMock.getEarlyBoundCRS(essence)).thenReturn(crs);
            EarlyBoundCRSRequest  request = new EarlyBoundCRSRequest(essence);
            EarlyBoundCRS  result = crsCatalogApi.getEarlyBoundCRS(request, "essence");
            assertNotNull(result);
            assertEquals(crs, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetEarlyBoundCRSBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getEarlyBoundCRS(any())).thenThrow(AppException.class);
            EarlyBoundCRSRequest  request = new EarlyBoundCRSRequest(null, null);
            crsCatalogApi.getEarlyBoundCRS(request, "essence");
        });
    }


    @Test
    public void testGetCompoundCRSes() {
        assertNotNull(crsCatalogApi);
        try {
            List<CompoundCRS>   compoundCRSes = new ArrayList<>();
            compoundCRSes.add(new CompoundCRSImpl());
            when(catalogMock.getCompoundCRSes()).thenReturn(compoundCRSes);
	        CompoundCRSResults results = crsCatalogApi.getCompoundCRSes(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " CompoundCRSes");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetCompoundCRS() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("CompoundCRSEssence.json");
            CompoundCRSEssenceImpl essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
            CompoundCRS   crs = new CompoundCRSImpl();
            when(catalogMock.getCompoundCRS(essence)).thenReturn(crs);
            CompoundCRSRequest  request = new CompoundCRSRequest(essence);
            CompoundCRS  result = crsCatalogApi.getCompoundCRS(request, "essence");
            assertNotNull(result);
            assertEquals(crs, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCRSBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getCompoundCRS(any())).thenThrow(AppException.class);
            CompoundCRSRequest  request = new CompoundCRSRequest(null);
            crsCatalogApi.getCompoundCRS(request, "essence");
        });
    }


    @Test
    public void testGetAllCTs() {
        assertNotNull(crsCatalogApi);
        try {
            List<CT>   allCTs = new ArrayList<>();
            allCTs.add(new CompoundCTImpl());
            when(catalogMock.getAllCTs()).thenReturn(allCTs);
	        CTResults results = crsCatalogApi.getAllCTs(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " CTs");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetSingleCTs() {
        assertNotNull(crsCatalogApi);
        try {
            List<SingleCT>   singleCTs = new ArrayList<>();
            singleCTs.add(new SingleCTImpl());
            when(catalogMock.getSingleCTs()).thenReturn(singleCTs);
	        SingleCTResults results = crsCatalogApi.getSingleCTs(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " SingleCTs");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetSingleCT() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("SingleCTEssence.json");
            SingleCTEssenceImpl essence = SingleCTEssenceImpl.fromJson(essenceJson);
            SingleCT   ct = new SingleCTImpl();
            when(catalogMock.getSingleCT(essence)).thenReturn(ct);
            SingleCTRequest  request = new SingleCTRequest(essence);
            SingleCT  result = crsCatalogApi.getSingleCT(request, "essence");
            assertNotNull(result);
            assertEquals(ct, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSingleCTBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getSingleCT(any())).thenThrow(AppException.class);
            SingleCTRequest  request = new SingleCTRequest(null);
            crsCatalogApi.getSingleCT(request, "essence");
        });
    }


    @Test
    public void testGetCompoundCTs() {
        assertNotNull(crsCatalogApi);
        try {
            List<CompoundCT>   compoundCTs = new ArrayList<>();
            compoundCTs.add(new CompoundCTImpl());
            when(catalogMock.getCompoundCTs()).thenReturn(compoundCTs);
	        CompoundCTResults results = crsCatalogApi.getCompoundCTs(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " CompoundCTs");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetCompoundCT() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("CompoundCTEssence.json");
            CompoundCTEssenceImpl essence = CompoundCTEssenceImpl.fromJson(essenceJson);
            CompoundCT   ct = new CompoundCTImpl();
            when(catalogMock.getCompoundCT(essence)).thenReturn(ct);
            CompoundCTRequest  request = new CompoundCTRequest(essence);
            CompoundCT  result = crsCatalogApi.getCompoundCT(request, "essence");
            assertNotNull(result);
            assertEquals(ct, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCTBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getCompoundCT(any())).thenThrow(AppException.class);
            CompoundCTRequest  request = new CompoundCTRequest(null);
            crsCatalogApi.getCompoundCT(request, "essence");
        });
    }


    @Test
    public void testGetAreasOfUse() {
        assertNotNull(crsCatalogApi);
        try {
            List<AreaOfUse>   areasOfUse = new ArrayList<>();
            areasOfUse.add(new AreaOfUseImpl());
            when(catalogMock.getAreasOfUse()).thenReturn(areasOfUse);
	        AreaOfUseResults results = crsCatalogApi.getAreasOfUse(0, 100, "essence");
	        assertNotNull(results);
            assertEquals(1, results.getCount());
	        System.out.println("There are " + results.getCount() + " AreasOfUse");
        }
        catch(Exception ex) {
			ex.printStackTrace();
        	fail();
        }
    }

    @Test
    public void testGetAreaOfUse() {
        assertNotNull(crsCatalogApi);
        try {
            String essenceJson = TestUtils.readFile("AreaOfUseEssence.json");
            AreaOfUseEssenceImpl essence = AreaOfUseEssenceImpl.fromJson(essenceJson);
            AreaOfUse   areaOfUse = new AreaOfUseImpl();
            when(catalogMock.getAreaOfUse(essence)).thenReturn(areaOfUse);
            AreaOfUseRequest  request = new AreaOfUseRequest(essence);
            AreaOfUse  result = crsCatalogApi.getAreaOfUse(request, "essence");
            assertNotNull(result);
            assertEquals(areaOfUse, result);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAreaOfUseBadRequest() {
        assertThrows(AppException.class, () -> {
            assertNotNull(crsCatalogApi);
            when(catalogMock.getAreaOfUse(any())).thenThrow(AppException.class);
            AreaOfUseRequest  request = new AreaOfUseRequest(null);
            crsCatalogApi.getAreaOfUse(request, "essence");
        });
    }

    @Test
    public void testSearchAreasOfUse() {
        assertNotNull(crsCatalogApi);
		try {
            double longitudeLeft = -180;
            double latitudeLower = -90;
            double longitudeRight = 180;
            double latitudeUpper = 0;
            SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
            String  keyword = "description:Europe";
            SearchRequest request = new SearchRequest(keyword);
            int offset = 0;
            int limit = 10;
            int totalCount = 100;
            ArrayList<AreaOfUse> areasOfUse = new ArrayList<>();
            areasOfUse.add(new AreaOfUseImpl());
            AreaOfUseResults areaOfUseResults = new AreaOfUseResults(areasOfUse, offset, totalCount);
    		when(catalogMock.searchAreasOfUse(request, boundingBox, offset, limit, RepresentationMode.ESSENCE)).thenReturn(areaOfUseResults);
	        AreaOfUseResults results;
            results = crsCatalogApi.searchAreasOfUse(request, longitudeLeft, latitudeLower, longitudeRight, latitudeUpper, offset, limit, "essence");
	        assertNotNull(results);
            assertEquals(offset, results.getOffset());
            assertEquals(areasOfUse.size(), results.getCount());
            assertEquals(totalCount, results.getTotalCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    }

    @Test
    public void testSearchCRSes() {
        assertNotNull(crsCatalogApi);
		try {
            double longitudeLeft = -180;
            double latitudeLower = -90;
            double longitudeRight = 180;
            double latitudeUpper = 0;
            SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
            String  keyword = "EPSG";
            SearchRequest request = new SearchRequest(keyword);
            int offset = 0;
            int limit = 10;
            int totalCount = 100;
            ArrayList<CRS> CRSes = new ArrayList<>();
            CRSes.add(new CompoundCRSImpl());
            CRSResults CRSResults = new CRSResults(CRSes, offset, totalCount);
    		when(catalogMock.searchCRSes(request, boundingBox, offset, limit, RepresentationMode.ESSENCE)).thenReturn(CRSResults);
			CRSResults results;
            results = crsCatalogApi.searchCRSes(request, longitudeLeft, latitudeLower, longitudeRight, latitudeUpper, offset, limit, "essence");
            assertEquals(offset, results.getOffset());
            assertEquals(CRSes.size(), results.getCount());
            assertEquals(totalCount, results.getTotalCount());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    }

	@Test
    public void testSearchCTs() {
        assertNotNull(crsCatalogApi);
		try {
            double longitudeLeft = -180;
            double latitudeLower = -90;
            double longitudeRight = 180;
            double latitudeUpper = 0;
            SphericalBoundingBox boundingBox = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
            String  keyword = "EPSG";
            SearchRequest request = new SearchRequest(keyword);
            int offset = 0;
            int limit = 10;
            int totalCount = 100;
            ArrayList<CT> CTs = new ArrayList<>();
            CTs.add(new CompoundCTImpl());
            CTResults CTResults = new CTResults(CTs, offset, totalCount);
    		when(catalogMock.searchCTs(request, boundingBox, offset, limit, RepresentationMode.ESSENCE)).thenReturn(CTResults);
			CTResults results;
            results = crsCatalogApi.searchCTs(request, longitudeLeft, latitudeLower, longitudeRight, latitudeUpper, offset, limit, "essence");
            assertEquals(offset, results.getOffset());
            assertEquals(CTs.size(), results.getCount());
            assertEquals(totalCount, results.getTotalCount());

            request = new SearchRequest(null);
            when(catalogMock.searchCTs(request, boundingBox, offset, limit, RepresentationMode.ESSENCE)).thenReturn(CTResults);
            results = crsCatalogApi.searchCTs(request, longitudeLeft, latitudeLower, longitudeRight, latitudeUpper, offset, limit, "essence");
            assertEquals(offset, results.getOffset());
            assertEquals(CTs.size(), results.getCount());
            assertEquals(totalCount, results.getTotalCount());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
