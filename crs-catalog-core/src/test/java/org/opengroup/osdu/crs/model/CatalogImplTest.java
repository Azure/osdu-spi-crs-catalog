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

package org.opengroup.osdu.crs.model;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.opengroup.osdu.crs.model.search.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogImplTest {

    private static final int EXPECTED_EB_CRS = 15;
    private static final int EXPECTED_LB_CRS = 15;
    private static final int EXPECTED_C_CRS = 1;
    private static final int EXPECTED_S_CT = 12;
    private static final int EXPECTED_C_CT = 2;

    private static CatalogImpl catalogImpl = null;

    @BeforeAll
    public static void setUpBeforeClass() {
        try {
            catalogImpl = TestUtils.createTestCatalog("TestCatalog.json");
            if(catalogImpl == null) {
                throw new IOException("Failed to load catalog");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetCatalogName() {
        assertNotNull(catalogImpl);
        assertEquals("Minimal test catalog", catalogImpl.getAttributes().getName());
    }

    @Test
    public void testGetCatalogDescription() {
        final String description = "Catalog containing selected ESRI's standard EPSG coordinate reference systems and cartographic transforms, and limited early-bound coordinate reference systems where the CRS and TRF area-of-use intersect";
        assertNotNull(catalogImpl);
        assertEquals(description, catalogImpl.getAttributes().getDescription());
    }

    @Test
    public void testGetCatalogLastModified() {
        assertNotNull(catalogImpl);
        assertEquals("2018-08-06T11:12:32.9535505Z", catalogImpl.getAttributes().getLastModified());
    }
    @Test
    public void testToStringAndHash(){
        assertNotNull(catalogImpl);
        for (CRS crs: catalogImpl.getAllCRSes()){
            assertNotEquals(0, crs.hashCode());
            String s = crs.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            crs = (CRS)crs.convert(RepresentationMode.PERSISTABLE_REFERENCE);
            s = crs.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            assertNotEquals(0, crs.hashCode());
        }
        for (CT ct: catalogImpl.getAllCTs()){
            assertNotEquals(0, ct.hashCode());
            String s = ct.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            ct = (CT)ct.convert(RepresentationMode.PERSISTABLE_REFERENCE);
            s = ct.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            assertNotEquals(0, ct.hashCode());
        }
        for (AreaOfUse aou: catalogImpl.getAreasOfUse()){
            assertNotEquals(0, aou.hashCode());
            String s = aou.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            aou = aou.convert(RepresentationMode.PERSISTABLE_REFERENCE);
            s = aou.toString();
            assertNotNull(s);
            assertTrue(s.length() > 0);
            assertNotEquals(0, aou.hashCode());
        }
    }
    @Test
    public void testGetAllCRSes() {
        assertNotNull(catalogImpl);
        try {
            List<CRS> crss = catalogImpl.getAllCRSes();
            assertNotNull(crss);
            assertEquals(31, crss.size());
            for (CRS crs : crss){
                assertNotNull(crs.getEssence());
                assertNull(crs.getNamedReference());
                CRS copy = (CRS)crs.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = (CRS)crs.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = (CRS)crs.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetEarlyBoundCRSwithCompoundCT(){
        assertNotNull(catalogImpl);
        try {
            List<EarlyBoundCRS> crs = catalogImpl.getEarlyBoundCRSes();
            assertNotNull(crs);
            for (EarlyBoundCRS eb_crs : crs) {
                assertNotNull(eb_crs);
                if (eb_crs.getEarlyBoundCRSEssence().getCTEssence() instanceof CompoundCTEssenceImpl) {
                    String pr = eb_crs.getEarlyBoundCRSEssence().toJsonString();
                    assertNotNull(pr);
                }
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssence.json");
            CRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            CRS  result = catalogImpl.getCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCRSWithNullEssence() {
        assertNotNull(catalogImpl);
        CRS result = catalogImpl.getCRS(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssenceDeprecated.json");
            LateBoundCRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            CRS  result = catalogImpl.getCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    @Test
    public void testGetLateBoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            List<LateBoundCRS> crss = catalogImpl.getLateBoundCRSes();
            assertNotNull(crss);
            assertEquals(EXPECTED_LB_CRS, crss.size());
            for (LateBoundCRS crs: crss) {
                assertNotNull(crs.getEssence());
                assertNull(crs.getNamedReference());
                LateBoundCRS copy = crs.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = crs.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = crs.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetLateBoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssence.json");
            LateBoundCRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            LateBoundCRS  result = catalogImpl.getLateBoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetLateBoundCRSWithNullEssence() {
        assertNotNull(catalogImpl);
        LateBoundCRS result = catalogImpl.getLateBoundCRS(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedLateBoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("LateBoundCRSEssenceDeprecated.json");
            LateBoundCRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
            LateBoundCRS  result = catalogImpl.getLateBoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    @Test
    public void testGetEarlyBoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            List<EarlyBoundCRS> crss = catalogImpl.getEarlyBoundCRSes();
            assertNotNull(crss);
            assertEquals(EXPECTED_EB_CRS, crss.size());
            for (EarlyBoundCRS crs: crss) {
                assertNotNull(crs.getEssence());
                assertNull(crs.getNamedReference());
                EarlyBoundCRS copy = crs.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = crs.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = crs.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetEarlyBoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("EarlyBoundCRSEssence.json");
            EarlyBoundCRSEssence essence = EarlyBoundCRSEssenceImpl.fromJson(essenceJson);
            EarlyBoundCRS  result = catalogImpl.getEarlyBoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetEarlyBoundCRSWithNullEssence() {
        assertNotNull(catalogImpl);
        EarlyBoundCRS result = catalogImpl.getEarlyBoundCRS(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedEarlyBoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("EarlyBoundCRSEssenceDeprecated.json");
            EarlyBoundCRSEssence essence = EarlyBoundCRSEssenceImpl.fromJson(essenceJson);
            EarlyBoundCRS  result = catalogImpl.getEarlyBoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    @Test
    public void testGetCompoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            List<CompoundCRS> crss = catalogImpl.getCompoundCRSes();
            assertNotNull(crss);
            assertEquals(EXPECTED_C_CRS, crss.size());
            for (CompoundCRS crs: crss) {
                assertNotNull(crs.getEssence());
                assertNull(crs.getNamedReference());
                CompoundCRS copy = crs.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = crs.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = crs.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                String s = copy.toString();
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCRSEssence.json");
            CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
            CompoundCRS  result = catalogImpl.getCompoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCRSwith1EbCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCRSEssenceWith1EbCRS.json");
            CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
            CompoundCRS  result = catalogImpl.getCompoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }
    @Test
    public void testGetCompoundCRSwith2EbCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCRSEssenceWith2EbCRS.json");
            CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
            CompoundCRS  result = catalogImpl.getCompoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCRSWithNullEssence() {
        assertNotNull(catalogImpl);
        CompoundCRS result = catalogImpl.getCompoundCRS(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedCompoundCRS() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCRSEssenceDeprecated.json");
            CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
            CompoundCRS  result = catalogImpl.getCompoundCRS(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAllCTs() {
        assertNotNull(catalogImpl);
        try {
            List<CT> cts = catalogImpl.getAllCTs();
            assertNotNull(cts);
            assertEquals(14, cts.size());
            for (CT ct : cts) {
                assertNotNull(ct.getEssence());
                assertNull(ct.getNamedReference());
                CT copy = (CT)ct.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = (CT)ct.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = (CT)ct.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("SingleCTEssence.json");
            SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
            CT  result = catalogImpl.getCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCTWithNullEssence() {
        assertNotNull(catalogImpl);
        CT result = catalogImpl.getCT(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("SingleCTEssenceDeprecated.json");
            SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
            CT  result = catalogImpl.getCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSingleCTs() {
        assertNotNull(catalogImpl);
        try {
            List<SingleCT> cts = catalogImpl.getSingleCTs();
            assertNotNull(cts);
            assertEquals(12, cts.size());
            assertEquals(EXPECTED_S_CT, cts.size());
            for (SingleCT ct: cts) {
                assertNotNull(ct.getEssence());
                assertNull(ct.getNamedReference());
                SingleCT copy = ct.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = ct.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = ct.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSingleCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("SingleCTEssence.json");
            SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
            SingleCT  result = catalogImpl.getSingleCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSingleCTWithNullEssence() {
        assertNotNull(catalogImpl);
        SingleCT result = catalogImpl.getSingleCT(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedSingleCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("SingleCTEssenceDeprecated.json");
            SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
            SingleCT  result = catalogImpl.getSingleCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    @Test
    public void testGetCompoundCTs() {
        assertNotNull(catalogImpl);
        try {
            List<CompoundCT> cts = catalogImpl.getCompoundCTs();
            assertNotNull(cts);
            assertEquals(EXPECTED_C_CT, cts.size());
            for (CompoundCT ct : cts){
                assertNotNull(ct.getEssence());
                assertNull(ct.getNamedReference());
                assertEquals(ct.getCTs().size(), ct.getCompoundCTEssence().getCTEssences().size());
                for (int i=0; i<ct.getCTs().size(); i++) {
                    assertEquals(ct.getCTs().get(i).getSingleCTEssence(), ct.getCompoundCTEssence().getCTEssences().get(i));
                }
                CompoundCT copy = ct.convert(RepresentationMode.ESSENCE_AND_PERSISTABLE_REFERENCE);
                assertNotNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
                copy = ct.convert(RepresentationMode.ESSENCE);
                assertNotNull(copy.getEssence());
                assertNull(copy.getNamedReference());
                copy = ct.convert(RepresentationMode.PERSISTABLE_REFERENCE);
                assertNull(copy.getEssence());
                assertNotNull(copy.getNamedReference());
                assertTrue(copy.getNamedReference().getName().length() > 0);
                assertTrue(copy.getNamedReference().getPersistableReference().length() > 0);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCTEssence.json");
            CompoundCTEssence essence = CompoundCTEssenceImpl.fromJson(essenceJson);
            CompoundCT  result = catalogImpl.getCompoundCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCompoundCTWithNullEssence() {
        assertNotNull(catalogImpl);
        CompoundCT result = catalogImpl.getCompoundCT(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedCompoundCT() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("CompoundCTEssenceDeprecated.json");
            CompoundCTEssence essence = CompoundCTEssenceImpl.fromJson(essenceJson);
            CompoundCT  result = catalogImpl.getCompoundCT(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    @Test
    public void testGetAreasOfUse() {
        assertNotNull(catalogImpl);
        try {
            List<AreaOfUse> au = catalogImpl.getAreasOfUse();
            assertNotNull(au);
            assertEquals(28, au.size());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAreaOfUse() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("AreaOfUseEssence.json");
            AreaOfUseEssence essence = AreaOfUseEssenceImpl.fromJson(essenceJson);
            AreaOfUse  result = catalogImpl.getAreaOfUse(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAreaOfUseWithNullEssence() {
        assertNotNull(catalogImpl);
        AreaOfUse result = catalogImpl.getAreaOfUse(null);
        assertNull(result);
    }

    @Test
    public void testGetDeprecatedAreaOfUse() {
        assertNotNull(catalogImpl);
        try {
            String essenceJson = TestUtils.readFile("AreaOfUseEssenceDeprecated.json");
            AreaOfUseEssence essence = AreaOfUseEssenceImpl.fromJson(essenceJson);
            AreaOfUse  result = catalogImpl.getAreaOfUse(essence);
            assertNotNull(result);
            assertEquals(essence, result.getEssence());
            assertNotNull(result.getDeprecationInfo());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchAreasOfUse() {
        assertNotNull(catalogImpl);
        try {
            AreaOfUseResults results;
            SphericalBoundingBoxImpl boundingBox = new SphericalBoundingBoxImpl(-180, -90, 180, 90);
            results = catalogImpl.searchAreasOfUse(new SearchRequest("EPSG"), boundingBox, 0, 10, RepresentationMode.ESSENCE);
            if(results != null)
                System.out.println("Found " + results.getAreasOfUse().size() + " Areas of use");
            else
                System.out.println("Found 0 Area of use");
            assertNotNull(results);
            assertNotNull(results.getAreasOfUse());
            assertEquals(10,results.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchCRSes() {
        assertNotNull(catalogImpl);
        try {
            CRSResults results;
            SphericalBoundingBoxImpl boundingBox = new SphericalBoundingBoxImpl(-180, -90, 180, 90);
            results = catalogImpl.searchCRSes(new SearchRequest("EPSG"), boundingBox, 0, 10, RepresentationMode.ESSENCE);
            if(results != null)
                System.out.println("Found " + results.getCRSes().size() + " CRSes");
            else
                System.out.println("Found 0 CRSes");
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(10,results.getCount());
            // search for a part of the late-bound CRS name in the name query
            results = catalogImpl.searchCRSes(new SearchRequest("name:*ED_1950_UTM* transformationReady:True"), boundingBox, 0, 10, RepresentationMode.ESSENCE);
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(4, results.getCount());
            // search for a part of the wellKnownText of the early-bound member WKTs
            results = catalogImpl.searchCRSes(new SearchRequest("wellKnownText:*Position_Vector* type:EBC"), boundingBox, 0, 10, RepresentationMode.ESSENCE);
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(4, results.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchLateBoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            LateBoundCRSResults results;
            results = catalogImpl.searchLateBoundCRSes(new SearchRequest("EPSG"), 0, 10);
            if(results != null)
                System.out.println("Found " + results.getCRSes().size() + " LateBoundCRSes");
            else
                System.out.println("Found 0 LateBoundCRSes");
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(10,results.getCount());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchEarlyBoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            EarlyBoundCRSResults results;
            results = catalogImpl.searchEarlyBoundCRSes(new SearchRequest("MY_COMPANY"), 0, 10);
            if(results != null)
                System.out.println("Found " + results.getCRSes().size() + " EarlyBoundCRSes");
            else
                System.out.println("Found 0 EarlyBoundCRSes");
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(10,results.getCount());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchCompoundCRSes() {
        assertNotNull(catalogImpl);
        try {
            CompoundCRSResults results;
            results = catalogImpl.searchCompoundCRSes(new SearchRequest("MY_COMPANY"), 0, 10);
            if(results != null)
                System.out.println("Found " + results.getCRSes().size() + " CompoundCRSes");
            else
                System.out.println("Found 0 CompoundCRSes");
            assertNotNull(results);
            assertNotNull(results.getCRSes());
            assertEquals(1,results.getCount());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchCTs() {
        assertNotNull(catalogImpl);
        try {
            CTResults results;
            SphericalBoundingBoxImpl boundingBox = new SphericalBoundingBoxImpl(-180, -90, 180, 90);
            results = catalogImpl.searchCTs(new SearchRequest(null), boundingBox, 0, 10, RepresentationMode.ESSENCE);
            if(results != null)
                System.out.println("Found " + results.getCTs().size() + " CTs");
            else
                System.out.println("Found 0 CTs");
            assertNotNull(results);
            assertNotNull(results.getCTs());
            assertEquals(10,results.getCount());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchSingleCTs() {
        assertNotNull(catalogImpl);
        try {
            SingleCTResults results;
            results = catalogImpl.searchSingleCTs(new SearchRequest(null), 0, 10);
            if(results != null)
                System.out.println("Found " + results.getCTs().size() + " SingleCTs");
            else
                System.out.println("Found 0 SingleCTs");
            assertNotNull(results);
            assertNotNull(results.getCTs());
            assertEquals(10,results.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSearchCompoundCTs() {
        assertNotNull(catalogImpl);
        try {
            CompoundCTResults results;
            results = catalogImpl.searchCompoundCTs(new SearchRequest("EPSG"), 0, 10);
            if(results != null)
                System.out.println("Found " + results.getCTs().size() + " CompoundCTs");
            else
                System.out.println("Found 0 CompoundCTs");
            assertNotNull(results);
            assertNotNull(results.getCTs());
            assertEquals(2,results.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
