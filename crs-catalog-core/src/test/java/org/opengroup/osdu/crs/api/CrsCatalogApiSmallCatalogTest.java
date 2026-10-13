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

import static org.junit.jupiter.api.Assertions.*;

import org.opengroup.osdu.crs.model.CRSEssenceImpl;
import org.opengroup.osdu.crs.model.CTEssenceImpl;
import org.opengroup.osdu.crs.model.CatalogImpl;
import org.opengroup.osdu.crs.model.TestUtils;
import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.request.CRSRequest;
import org.opengroup.osdu.crs.model.request.CTRequest;
import org.opengroup.osdu.crs.model.search.CRSResults;
import org.opengroup.osdu.crs.model.search.CTResults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

public class CrsCatalogApiSmallCatalogTest {
    private static CatalogImpl catalogImpl = null;
    private static CrsCatalogApi crsCatalogApi;

    @BeforeAll
    public static void setUpBeforeClass() {
        try {
            catalogImpl = TestUtils.createTestCatalog("TestCatalog.json");
            if(catalogImpl == null) {
                throw new IOException("Failed to load catalog");
            }
            crsCatalogApi = new CrsCatalogApi(catalogImpl);
            ReflectionTestUtils.setField(crsCatalogApi, "auditLogger", Mockito.mock(AuditLogger.class));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetCRS() {
        assertNotNull(crsCatalogApi);
        try {
            CRSResults allCRSs = crsCatalogApi.getAllCRSes(0, 1000, "persistable_reference_essence");
            for (CRS crs : allCRSs.getCRSes()){
                CRSEssenceImpl anyCRSEssence = (CRSEssenceImpl) crs.getEssence();
                CRSRequest req = new CRSRequest(anyCRSEssence);
                CRS found = crsCatalogApi.getCRS(req, "persistable_reference_essence");
                assertNotNull(found);
                assertEquals(crs, found);
                req = new CRSRequest(null, crs.getNamedReference().getPersistableReference());
                found = crsCatalogApi.getCRS(req, "persistable_reference_essence");
                assertNotNull(found);
                assertEquals(crs, found);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCT() {
        assertNotNull(crsCatalogApi);
        try {
            CTResults allCRSs = crsCatalogApi.getAllCTs(0, 1000, "persistable_reference_essence");
            for (CT ct : allCRSs.getCTs()){
                CTEssenceImpl anyCTEssence = (CTEssenceImpl)ct.getEssence();
                CTRequest req = new CTRequest(anyCTEssence);
                CT found = crsCatalogApi.getCT(req, "persistable_reference_essence");
                assertNotNull(found);
                assertEquals(ct, found);
                req = new CTRequest(null, ct.getNamedReference().getPersistableReference());
                found = crsCatalogApi.getCT(req, "persistable_reference_essence");
                assertNotNull(found);
                assertEquals(ct, found);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }
    @Test
    public void testGetCatalog(){

        assertNotNull(crsCatalogApi);
        try {
            Catalog catalog = crsCatalogApi.getCatalog();
            assertNotNull(catalog);
            assertNotNull(catalog.getAttributes());
            assertNotNull(catalog.getAttributes().getName());
            assertNotNull(catalog.getAttributes().getDescription());
            assertNotNull(catalog.getAttributes().getLastModified());
            assertNotEquals(0, catalog.getLateBoundCRSesCount());
            assertNotEquals(0, catalog.getEarlyBoundCRSesCount());
            assertNotEquals(0, catalog.getCompoundCRSesCount());
            assertNotEquals(0, catalog.getSingleCTsCount());
            assertNotEquals(0, catalog.getCompoundCTsCount());
            assertNotEquals(0, catalog.getAreaOfUseCount());
        } catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }
}
