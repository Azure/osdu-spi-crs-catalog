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

import org.opengroup.osdu.crs.model.AreaOfUseImpl;
import org.opengroup.osdu.crs.model.interfaces.AreaOfUse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Created by SYang on 7/12/2016.
 */
public class AreaOfUseImplResultsTest {

    @Test
    public void testDefaultConstructor() {
        AreaOfUseResults results = new AreaOfUseResults();
        assertTrue(results != null);
        assertTrue(results.getAreasOfUse().size() == 0);
        assertTrue(results.getCount() == 0);
        assertTrue(results.getOffset() == 0);
        assertTrue(results.getTotalCount() == 0);
    }

    @Test
    public void testConstructor() {

        AreaOfUseImpl areaOfUseImpl = new AreaOfUseImpl();
        List<AreaOfUse> areaOfUseImplList = new ArrayList<>();
        areaOfUseImplList.add(areaOfUseImpl);
        AreaOfUseResults results = new AreaOfUseResults(areaOfUseImplList);
        assertTrue(results != null);
        AreaOfUseResults results2 = new AreaOfUseResults(areaOfUseImplList, 0, 1);
        assertTrue(results2 != null);
        assertTrue(results2.getAreasOfUse().size() == 1);
        assertTrue(results2.getAreasOfUse().get(0).equals(results.getAreasOfUse().get(0)));
        assertTrue(results2.getCount() == 1);
        assertTrue(results2.getOffset() == 0);
        assertTrue(results2.getTotalCount() == 1);
    }
}
