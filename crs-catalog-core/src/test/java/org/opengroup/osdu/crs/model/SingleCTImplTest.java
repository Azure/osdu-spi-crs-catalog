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
public class SingleCTImplTest {


    @Test
    public void testEmptySingleCT() {
        try {
            SingleCTImpl ct = new SingleCTImpl();
            assertNotNull(ct);
            String source = ct.getSource();
            ct.getAreaOfUse();
            ct.getAreaOfUseId();
            ct.setDeprecationInfo((CTDeprecationInfoImpl)ct.getDeprecationInfo());
            ct.getFromCRSAuthorityCode();
            ct.getToCRSAuthorityCode();
            SingleCTEssenceImpl essence = (SingleCTEssenceImpl)ct.getEssence();
            assertTrue(essence != null);
            assertTrue(ct.toString() != null);
            assertTrue(ct.hashCode() == essence.hashCode());
            assertTrue(ct.equals(ct));
            assertFalse(ct.equals(essence));
            assertEquals(essence, ct.getEssence());
            assertTrue(essence.equals(essence));
            assertFalse(essence.equals(ct));
            essence.getEngineVersion();
            essence.getName();
            assertTrue(essence.toString() != null);
            AuthorityCodeImpl authorityCodeImpl = (AuthorityCodeImpl)essence.getAuthorityCode();
            assertTrue(authorityCodeImpl != null);
            assertTrue(authorityCodeImpl.toString() != null);
            authorityCodeImpl.equals(authorityCodeImpl);
            ct.setEssence(essence);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
