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

package org.opengroup.osdu.crs.model.search.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WildcardTest {

    @Test
    public void isWildcardTest() {
        assertTrue(Wildcard.isWildcard('*'));
        assertFalse(Wildcard.isWildcard('?')); // We don't support ? as wildcard
        assertFalse(Wildcard.isWildcard('a'));
    }

    @Test
    public void toStringTest() {
        assertEquals("*", new Wildcard().toString());
    }

}
