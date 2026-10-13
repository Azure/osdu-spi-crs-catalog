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

import static org.junit.jupiter.api.Assertions.*;

import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;

import org.junit.jupiter.api.Test;

/**
 * Created by SYang on 7/11/2016.
 */
public class LateBoundCRSEssenceImplTest {

    @Test
    public void testEmptyLateBoundCRSEssence() {
        try {
            LateBoundCRSEssenceImpl essence = new LateBoundCRSEssenceImpl();
            assertTrue(essence != null);
            assertTrue(essence.equals(essence));
            String wellKnownText = essence.getWellKnownText();
            assertTrue(essence.toString() != null);
            AuthorityCodeImpl authorityCodeImpl = (AuthorityCodeImpl)essence.getAuthorityCode();
            assertTrue(authorityCodeImpl != null);
            assertTrue(authorityCodeImpl.toString() != null);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLateBoundCRSEssenceFromJson() {
        try {
            String json = "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"4282\"},\"name\":\"GCS_Pointe_Noire\",\"type\":\"LBC\",\"ver\":\"PE_10_3_1\",\"wkt\":\"GEOGCS[\\\"GCS_Pointe_Noire\\\",DATUM[\\\"D_Pointe_Noire\\\",SPHEROID[\\\"Clarke_1880_IGN\\\",6378249.2,293.466021293627]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433],AUTHORITY[\\\"EPSG\\\",4282]]\"}";
            LateBoundCRSEssenceImpl essence = LateBoundCRSEssenceImpl.fromJson(json);
            assertNotNull(essence);
            assertTrue(essence.equals(essence));
            // assertTrue(essence.hashCode() > 0); Why that?
            AuthorityCode authorityCode = essence.getAuthorityCode();
            assertNotNull(authorityCode);
            assertEquals(authorityCode.getAuthority(),"EPSG");
            assertEquals(authorityCode.getCode(),"4282");
        }
        catch (Exception ex) {
            fail();
        }
    }

    // @Test
    // public void testLateBoundCRSEssenceCreateInstance() {
    //     String pr_v2 = "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"4282\"},\"name\":\"GCS_Pointe_Noire\",\"type\":\"LBC\",\"ver\":\"PE_10_3_1\",\"wkt\":\"GEOGCS[\\\"GCS_Pointe_Noire\\\",DATUM[\\\"D_Pointe_Noire\\\",SPHEROID[\\\"Clarke_1880_IGN\\\",6378249.2,293.466021293627]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433],AUTHORITY[\\\"EPSG\\\",4282]]\"}";
    //     String pr_v1 = "%7B%22WKT%22%3A%22GEOGCS%5B%5C%22GCS_Pointe_Noire%5C%22%2CDATUM%5B%5C%22D_Pointe_Noire%5C%22%2CSPHEROID%5B%5C%22Clarke_1880_IGN%5C%22%2C6378249.2%2C293.466021293627%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%2CAUTHORITY%5B%5C%22EPSG%5C%22%2C4282%5D%5D%22%2C%22Type%22%3A%22LBCRS%22%2C%22EngineVersion%22%3A%22PE_10_3_1%22%2C%22Name%22%3A%22GCS_Pointe_Noire%22%2C%22AuthorityCode%22%3A%7B%22Authority%22%3A%22EPSG%22%2C%22Code%22%3A%224282%22%7D%7D";
    //     EssenceImpl essence_v1 = LateBoundCRSEssenceImpl.createInstance(pr_v1);
    //     assertNotNull(essence_v1);
    //     assertTrue(essence_v1 instanceof LateBoundCRSEssenceImpl);
    //     EssenceImpl essence_v2 = LateBoundCRSEssenceImpl.createInstance(pr_v2);
    //     assertNotNull(essence_v2);
    //     assertTrue(essence_v2 instanceof LateBoundCRSEssenceImpl);
    //     assertEquals(essence_v1, essence_v2);
    //     // Failures
    //     String corrupted = pr_v1.replace("LBCRS", "Corrupted");
    //     essence_v1 = LateBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v1.replace("%7B", "");
    //     essence_v1 = LateBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v2.replace("\"authCode", "authCode");
    //     essence_v2 = LateBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v2);
    // }
}