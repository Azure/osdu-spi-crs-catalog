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

package org.opengroup.osdu.crs.model.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NamedReferenceTest {
    @Test
    public void testDefaultConstructor() {
        NamedReference nr = new NamedReference();
        assertNotNull(nr);
        assertNull(nr.getName());
        assertNull(nr.getPersistableReference());
        assertEquals(0, nr.hashCode());
        NamedReference other = new NamedReference();
        assertEquals(nr, other);
    }

    @Test
    public void testPopulated(){
        NamedReference nr1 = new NamedReference("Name", "PersistableReference");
        assertNotNull(nr1);
        assertEquals("Name", nr1.getName());
        assertEquals("PersistableReference", nr1.getPersistableReference());
        assertTrue(nr1.hashCode() != 0);
        NamedReference nr2 = new NamedReference();
        assertNotEquals(nr1, nr2);
        nr2 = new NamedReference(nr1.getName(), nr1.getPersistableReference());
        assertEquals(nr1, nr2);
        Object other = new Object();
        assertNotEquals(nr1, other);
   }
}