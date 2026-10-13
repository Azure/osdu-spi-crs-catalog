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
import org.junit.jupiter.api.Test;

import org.opengroup.osdu.crs.model.interfaces.*;

import static org.junit.jupiter.api.Assertions.*;

public class AreaOfUseEssenceImplTest {
    @Test
    public void testAreaOfUseFromJson() {
        try {
            String json = "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"3958\"},\"boundBox\":{\"latMin\":7.75,\"latMax\":9.32,\"lonMin\":116.89,\"lonMax\":118.0},\"type\":\"AOU\"}";
            AreaOfUseEssenceImpl essence = AreaOfUseEssenceImpl.fromJson(json);
            assertNotNull(essence);
            assertTrue(essence.equals(essence));
            //TODO: assertTrue(essence.hashCode() > 0); 
            AuthorityCode authorityCode = essence.getAuthorityCode();
            assertNotNull(authorityCode);
            assertEquals(authorityCode.getAuthority(),"EPSG");
            assertEquals(authorityCode.getCode(),"3958");
            SphericalBoundingBox bb = essence.getBoundingBox();
            assertNotNull(bb);
            double delta = 0.00001;
            assertEquals(116.89, bb.getLongitudeLeft(), delta);
            assertEquals(7.75, bb.getLatitudeLower(), delta);
            assertEquals(118.0, bb.getLongitudeRight(), delta);
            assertEquals(9.32, bb.getLatitudeUpper(), delta);
        }
        catch (Exception ex) {
            fail();
        }
    }
}
