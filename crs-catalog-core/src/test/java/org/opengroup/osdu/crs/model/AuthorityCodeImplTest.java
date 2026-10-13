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

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by SYang on 7/11/2016.
 */
public class AuthorityCodeImplTest {

    @Test
    public void testEmptyAuthorityCode() {
        AuthorityCodeImpl authorityCodeImpl = new AuthorityCodeImpl();
        assertTrue(authorityCodeImpl != null);
        assertTrue(authorityCodeImpl.toString() != null);
        assertTrue(authorityCodeImpl.equals(authorityCodeImpl));
        assertTrue(authorityCodeImpl.hashCode() == 0);
        assertTrue(authorityCodeImpl.getAuthority().equals(""));
        assertTrue(authorityCodeImpl.getCode().equals(""));
    }

    @Test
    public void testAuthorityCode() {
        String  authority = "authority";
        String  code = "code";

        AuthorityCodeImpl authorityCodeImpl = new AuthorityCodeImpl(authority, code);
        assertTrue(authorityCodeImpl != null);
        assertTrue(authorityCodeImpl.toString() != null);
        assertTrue(authorityCodeImpl.equals(authorityCodeImpl));
        assertTrue(authorityCodeImpl.hashCode() > 0);
        assertTrue(authorityCodeImpl.getAuthority().equals(authority));
        assertTrue(authorityCodeImpl.getCode().equals(code));
    }

}
