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
public class SingleCTEssenceImplTest {

    @Test
    public void testEmptySingleCTEssence() {
        try {
            SingleCTEssenceImpl essence = new SingleCTEssenceImpl();
            assertTrue(essence != null);
            assertTrue(essence.equals(essence));
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
    public void testSingleCTEssenceFromJson() {
        try {
            String json = "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"15846\"},\"name\":\"Egypt_Gulf_of_Suez_S-650_TL_To_WGS_1984_2\",\"type\":\"ST\",\"ver\":\"PE_10_3_1\",\"wkt\":\"GEOGTRAN[\\\"Egypt_Gulf_of_Suez_S-650_TL_To_WGS_1984_2\\\",GEOGCS[\\\"GCS_Egypt_Gulf_of_Suez_S-650_TL\\\",DATUM[\\\"D_Egypt_Gulf_of_Suez_S-650_TL\\\",SPHEROID[\\\"Helmert_1906\\\",6378200.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"Geocentric_Translation\\\"],PARAMETER[\\\"X_Axis_Translation\\\",-146.21],PARAMETER[\\\"Y_Axis_Translation\\\",112.63],PARAMETER[\\\"Z_Axis_Translation\\\",4.05],AUTHORITY[\\\"EPSG\\\",15846]]\"}";
            CompoundCTEssenceImpl essence = CompoundCTEssenceImpl.fromJson(json);
            assertNotNull(essence);
            assertTrue(essence.equals(essence));
            //TODO: assertTrue(essence.hashCode() > 0); 
            AuthorityCode authorityCode = essence.getAuthorityCode();
            assertNotNull(authorityCode);
            assertEquals(authorityCode.getAuthority(),"EPSG");
            assertEquals(authorityCode.getCode(),"15846");
        }
        catch (Exception ex) {
            fail();
        }
    }

}
