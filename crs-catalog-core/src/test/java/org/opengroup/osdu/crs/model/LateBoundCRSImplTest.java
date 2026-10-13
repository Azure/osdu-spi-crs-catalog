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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by SYang on 7/11/2016.
 */
public class LateBoundCRSImplTest {

    @Test
    public void testEmptyLateBoundCRS() {
        try {
            LateBoundCRSImpl crs = new LateBoundCRSImpl();
            assertNotNull(crs);
            crs.getCRSType();
            assertTrue(crs.equals(crs));
            LateBoundCRSEssenceImpl essence = (LateBoundCRSEssenceImpl)crs.getEssence();
            assertTrue(essence != null);
            assertFalse(crs.equals(essence));
            crs.getCRSType();
            String source = crs.getSource();
            assertTrue(crs.hashCode() == essence.hashCode());
            assertEquals(essence, crs.getEssence());
            assertTrue(essence.equals(essence));
            assertFalse(essence.equals(crs));
            String wellKnownText = essence.getWellKnownText();
            assertTrue(essence.toString() != null);
            AuthorityCodeImpl authorityCodeImpl = (AuthorityCodeImpl)essence.getAuthorityCode();
            assertTrue(authorityCodeImpl != null);
            assertTrue(authorityCodeImpl.toString() != null);
            authorityCodeImpl.equals(authorityCodeImpl);
            authorityCodeImpl.hashCode();
            authorityCodeImpl.getAuthority();
            authorityCodeImpl.getCode();
            crs.setEssence(essence);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}