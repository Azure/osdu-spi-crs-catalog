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

import org.opengroup.osdu.crs.model.interfaces.DeprecationInfo;
import org.opengroup.osdu.crs.model.interfaces.Essence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import java.util.List;

public class BaseImplTest {

    @Test
    public void testEmptyBase() {
        try {
            BaseImpl baseImpl = new BaseImpl() {
                @Override
                public Essence getEssence() {
                    return null;
                }

                @Override
                public DeprecationInfo getDeprecationInfo() {
                    return null;
                }
            };
            assertNotNull(baseImpl);
            assertTrue(baseImpl.getId() == null);
            assertTrue(baseImpl.getDescription() == null);
            assertTrue(baseImpl.getLastModified() == null);
            assertTrue((baseImpl.getAliasNames() != null) && (baseImpl.getAliasNames().size() == 0)) ;
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testBase() {
        try {
            BaseImpl baseImpl = new BaseImpl() {
                @Override
                public Essence getEssence() {
                    return null;
                }

                @Override
                public DeprecationInfo getDeprecationInfo() {
                    return null;
                }

                @Override
                public String getId() {
                    return "1";
                }

                @Override
                public String getDescription() {
                    return "test";
                }

                @Override
                public String getLastModified() {
                    return "20180731";
                }

                @Override
                public List<String> getAliasNames() {
                    List<String> aliasNames = new ArrayList<String>();
                    aliasNames.add("test");
                    return aliasNames;
                }

            };
            assertNotNull(baseImpl);
            assertEquals(baseImpl.getId(), "1");
            assertEquals(baseImpl.getDescription(),"test");
            assertEquals(baseImpl.getLastModified(), "20180731");
            assertTrue((baseImpl.getAliasNames() != null) && 
                       (baseImpl.getAliasNames().size() == 1) && 
                       (baseImpl.getAliasNames().get(0).equals("test")));
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
