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

import org.opengroup.osdu.crs.model.interfaces.CT;

import static org.junit.jupiter.api.Assertions.*;

import org.opengroup.osdu.crs.model.search.NamedReference;

/**
 * Created by SYang on 7/11/2016.
 */
public class CTDeprecationInfoImplTest {

    @Test
    public void testDefaultConstructor() {
        CTDeprecationInfoImpl ctDeprecationInfo = new CTDeprecationInfoImpl();
        assertNotNull(ctDeprecationInfo);
        ctDeprecationInfo.setSupersededByCT(null);
        CT ct = ctDeprecationInfo.getSupersededByCT();
        assertNull(ct);
        assertNull(ctDeprecationInfo.getDeprecationState());
        assertTrue(ctDeprecationInfo.toString().length() > 0);
        assertNull(ctDeprecationInfo.getSupersededByCTId());
        assertNull(ctDeprecationInfo.getRemarks());
        assertTrue(ctDeprecationInfo.hashCode() != 0);
    }

    private SingleCTImpl makeCT() {
        SingleCTImpl ct = new SingleCTImpl();
        ct.setEssence(new SingleCTEssenceImpl());
        ct.setNamedReference(new NamedReference("CRS-name", ct.getSingleCTEssence().toJsonString()));
        return ct;
    }

    @Test
    public void testConstructor() {
        String deprecationState = "state";
        String remarks = "remarks";
        String supersededByAouId = "id";
        CTDeprecationInfoImpl ctDeprecationInfo = new CTDeprecationInfoImpl(
                deprecationState,
                remarks,
                supersededByAouId
        );
        assertNotNull(ctDeprecationInfo);
        ctDeprecationInfo.setSupersededByCT(null);
        CTImpl ct = (CTImpl)ctDeprecationInfo.getSupersededByCT();
        assertNull(ct);
        assertEquals(deprecationState, ctDeprecationInfo.getDeprecationState());
        assertTrue(ctDeprecationInfo.toString().length() > 0);
        assertEquals(supersededByAouId, ctDeprecationInfo.getSupersededByCTId());
        assertEquals(remarks, ctDeprecationInfo.getRemarks());
        assertTrue(ctDeprecationInfo.hashCode() != 0);
        CTImpl superseded = makeCT();
        ctDeprecationInfo.setSupersededByCT(superseded);
        assertEquals(superseded, ctDeprecationInfo.getSupersededByCT());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String  json = mapper.writeValueAsString(ctDeprecationInfo);
            assertTrue(json.contains("\"supersededByCT\""));
            assertFalse(json.contains("\"supersededByCTId\""));
        } catch (JsonProcessingException ex) {
            fail();
        }
    }

}
