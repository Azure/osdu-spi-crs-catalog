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

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by SYang on 7/11/2016.
 */
public class CompoundCTImplTest {

    @Test
    public void testEmptyCompoundCT() {
        try {
            CompoundCTImpl ct = new CompoundCTImpl();
            assertNotNull(ct);
            List<AuthorityCode> authorityCodes = ct.getCTAuthorityCodes();
            assertTrue(authorityCodes != null);
            assertEquals(0, authorityCodes.size());
            //List<CT> ltrf = ct.getCTs();
//            List<CTImpl> ltrf2 = new ArrayList<>();
//            for (CT trf : ltrf) {
//                ltrf2.add((CTImpl) trf);
//            }
//            ct.setCTs(ltrf2);
            CompoundCTEssenceImpl essence = (CompoundCTEssenceImpl) ct.getEssence();
            assertTrue(essence != null);
            List<CTEssence> lCTEssences = essence.getCTEssences();
            assertTrue((lCTEssences != null) && (lCTEssences.size() == 0));
            assertTrue(ct.toString() != null);
            assertTrue(ct.hashCode() == essence.hashCode());
            assertTrue(ct.equals(ct));
            assertEquals(essence, ct.getEssence());
            assertTrue(essence.equals(essence));
            assertFalse(essence.equals(new Object()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPopulatedCompoundCT() {
        try {
            String json = "{\"authCode\": {\"auth\": \"MY_COMPANY\", \"code\": \"158511693\"}, \"name\": \"Fallback NAD27 to WGS 84 (79)/NAD27 to WGS 84 (33)\", \"policy\": \"Fallback\", \"type\": \"CT\", \"cts\": [{\"authCode\": {\"auth\": \"EPSG\", \"code\": \"15851\"}, \"name\": \"NAD_1927_To_WGS_1984_79_CONUS\", \"type\": \"ST\", \"ver\": \"PE_10_3_1\", \"wkt\": \"GEOGTRAN[\\\"NAD_1927_To_WGS_1984_79_CONUS\\\",GEOGCS[\\\"GCS_North_American_1927\\\",DATUM[\\\"D_North_American_1927\\\",SPHEROID[\\\"Clarke_1866\\\",6378206.4,294.9786982]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"NADCON\\\"],PARAMETER[\\\"Dataset_conus\\\",0.0],AUTHORITY[\\\"EPSG\\\",15851]]\"}, {\"authCode\": {\"auth\": \"EPSG\", \"code\": \"1693\"}, \"name\": \"NAD_1927_To_WGS_1984_33\", \"type\": \"ST\", \"ver\": \"PE_10_3_1\", \"wkt\": \"GEOGTRAN[\\\"NAD_1927_To_WGS_1984_33\\\",GEOGCS[\\\"GCS_North_American_1927\\\",DATUM[\\\"D_North_American_1927\\\",SPHEROID[\\\"Clarke_1866\\\",6378206.4,294.9786982]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"NTv2\\\"],PARAMETER[\\\"Dataset_canada/Ntv2_0\\\",0.0],AUTHORITY[\\\"EPSG\\\",1693]]\"}], \"ver\": \"PE_10_3_1\"}";
            CompoundCTEssenceImpl essence = CompoundCTEssenceImpl.fromJson(json);

            assertEquals("Fallback NAD27 to WGS 84 (79)/NAD27 to WGS 84 (33)", essence.getName());
            assertEquals("MY_COMPANY", essence.getAuthorityCode().getAuthority());
            assertEquals("158511693", essence.getAuthorityCode().getCode());
            assertEquals("15851", essence.getCTEssences().get(0).getAuthorityCode().getCode());
            assertEquals("NAD_1927_To_WGS_1984_79_CONUS", essence.getCTEssences().get(0).getName());
            assertEquals("1693", essence.getCTEssences().get(1).getAuthorityCode().getCode());
            assertEquals("NAD_1927_To_WGS_1984_33", essence.getCTEssences().get(1).getName());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
