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
public class CRSDeprecationInfoImplTest {

    @Test
    public void testDefaultConstructor() {
        CRSDeprecationInfoImpl crsDeprecationInfoImpl = new CRSDeprecationInfoImpl();
        assertNotNull(crsDeprecationInfoImpl);
        crsDeprecationInfoImpl.setSupersededByCRS(null);
        CRS crs = crsDeprecationInfoImpl.getSupersededByCRS();
        assertNull(crs);
        assertNull(crsDeprecationInfoImpl.getDeprecationState());
        assertTrue(crsDeprecationInfoImpl.toString().length() > 0);
        assertNull(crsDeprecationInfoImpl.getSupersededByCRSId());
        assertNull(crsDeprecationInfoImpl.getRemarks());
        assertTrue(crsDeprecationInfoImpl.hashCode() != 0);
    }

    private LateBoundCRSImpl makeCRS() {
        LateBoundCRSImpl crs = new LateBoundCRSImpl();
        crs.setEssence(new LateBoundCRSEssenceImpl());
        crs.setNamedReference(new NamedReference("CRS-name", crs.getLateBoundCRSEssence().toJsonString()));
        return crs;
    }

    @Test
    public void testConstructor() {
        String deprecationState = "state";
        String remarks = "remarks";
        String supersededByAouId = "id";
        CRSDeprecationInfoImpl crsDeprecationInfoImpl = new CRSDeprecationInfoImpl(
                deprecationState,
                remarks,
                supersededByAouId
        );
        assertNotNull(crsDeprecationInfoImpl);
        crsDeprecationInfoImpl.setSupersededByCRS(null);
        CRS crs = crsDeprecationInfoImpl.getSupersededByCRS();
        assertNull(crs);
        assertEquals(crsDeprecationInfoImpl.getDeprecationState(), deprecationState);
        assertTrue(crsDeprecationInfoImpl.toString().length() > 0);
        assertEquals(crsDeprecationInfoImpl.getSupersededByCRSId(), supersededByAouId);
        assertEquals(crsDeprecationInfoImpl.getRemarks(), remarks);
        assertTrue(crsDeprecationInfoImpl.hashCode() != 0);
        CRSImpl superseded = makeCRS();
        crsDeprecationInfoImpl.setSupersededByCRS(superseded);
        assertEquals(superseded, crsDeprecationInfoImpl.getSupersededByCRS());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String  json = mapper.writeValueAsString(crsDeprecationInfoImpl);
            assertTrue(json.contains("\"supersededByCRS\""));
            assertFalse(json.contains("\"supersededByCRSId\""));
        } catch (JsonProcessingException ex) {
            fail();
        }
    }
}
