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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.opengroup.osdu.crs.model.interfaces.*;

import static org.junit.jupiter.api.Assertions.*;

import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 * Created by SYang on 7/11/2016.
 */
public class AreaOfUseImplDeprecationInfoImplTest {

    @Test
    public void testDefaultConstructor() {
        AreaOfUseDeprecationInfoImpl areaOfUseDeprecationInfoImpl = new AreaOfUseDeprecationInfoImpl();
        assertNotNull(areaOfUseDeprecationInfoImpl);
        areaOfUseDeprecationInfoImpl.setSupersededByArea(null);
        AreaOfUse areaOfUse = areaOfUseDeprecationInfoImpl.getSupersededByArea();
        assertNull(areaOfUse);
        assertNull(areaOfUseDeprecationInfoImpl.getDeprecationState());
        assertTrue(areaOfUseDeprecationInfoImpl.toString().length() > 0);
        assertNull(areaOfUseDeprecationInfoImpl.getSupersededByAouId());
        assertNull(areaOfUseDeprecationInfoImpl.getRemarks());
        assertTrue(areaOfUseDeprecationInfoImpl.hashCode() != 0);
    }

    private AreaOfUseImpl makeAoU() {
        AreaOfUseImpl aou = new AreaOfUseImpl();
        aou.setEssence(new AreaOfUseEssenceImpl());
        aou.setNamedReference(new NamedReference("CRS-name", aou.getEssence().toJsonString()));
        return aou;
    }

    @Test
    public void testConstructor() {
        String deprecationState = "state";
        String remarks = "remarks";
        String supersededByAouId = "id";
        AreaOfUseDeprecationInfoImpl areaOfUseDeprecationInfoImpl = new AreaOfUseDeprecationInfoImpl(
                deprecationState,
                remarks,
                supersededByAouId
        );
        assertNotNull(areaOfUseDeprecationInfoImpl);
        areaOfUseDeprecationInfoImpl.setSupersededByArea(null);
        AreaOfUse areaOfUse = areaOfUseDeprecationInfoImpl.getSupersededByArea();
        assertNull(areaOfUse);
        assertEquals(areaOfUseDeprecationInfoImpl.getDeprecationState(), deprecationState);
        assertTrue(areaOfUseDeprecationInfoImpl.toString().length() > 0);
        assertEquals(areaOfUseDeprecationInfoImpl.getSupersededByAouId(), supersededByAouId);
        assertEquals(areaOfUseDeprecationInfoImpl.getRemarks(), remarks);
        assertTrue(areaOfUseDeprecationInfoImpl.hashCode() != 0);
        AreaOfUseImpl superseded = makeAoU();
        areaOfUseDeprecationInfoImpl.setSupersededByArea(superseded);
        assertEquals(superseded, areaOfUseDeprecationInfoImpl.getSupersededByArea());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String  json = mapper.writeValueAsString(areaOfUseDeprecationInfoImpl);
            assertTrue(json.contains("\"supersededByArea\""));
            assertFalse(json.contains("\"supersededByAouId\""));
        } catch (JsonProcessingException ex) {
            fail();
        }
    }
}
