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
public class SphericalBoundingBoxImplTest {

    @Test
    public void testEmptySphericalBoundingBox() {

        SphericalBoundingBoxImpl sphericalBoundingBoxImpl = new SphericalBoundingBoxImpl();
        SphericalBoundingBoxImpl sphericalBoundingBoxImpl2 = new SphericalBoundingBoxImpl();
        assertTrue(sphericalBoundingBoxImpl.equals(sphericalBoundingBoxImpl2));
        assertFalse(sphericalBoundingBoxImpl.equals(""));
        assertTrue(sphericalBoundingBoxImpl.hashCode() == sphericalBoundingBoxImpl2.hashCode());
        assertTrue(sphericalBoundingBoxImpl.toString().equals(sphericalBoundingBoxImpl2.toString()));
    }

    @Test
    public void testSphericalBoundingBox() {
        double longitudeLeft = 0.0;
        double latitudeLower = 0.0;
        double longitudeRight = 10.0;
        double latitudeUpper = 20.0;
        double delta = 0.00001;

        SphericalBoundingBoxImpl sphericalBoundingBoxImpl = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
        assertEquals(longitudeLeft, sphericalBoundingBoxImpl.getLongitudeLeft(), delta);
        assertEquals(latitudeLower, sphericalBoundingBoxImpl.getLatitudeLower(), delta);
        assertEquals(longitudeRight, sphericalBoundingBoxImpl.getLongitudeRight(), delta);
        assertEquals(latitudeUpper, sphericalBoundingBoxImpl.getLatitudeUpper(), delta);
        SphericalBoundingBoxImpl sphericalBoundingBoxImpl2 = new SphericalBoundingBoxImpl(longitudeLeft, latitudeLower, longitudeRight, latitudeUpper);
        assertTrue(sphericalBoundingBoxImpl.equals(sphericalBoundingBoxImpl2));
        assertTrue(sphericalBoundingBoxImpl.hashCode() == sphericalBoundingBoxImpl2.hashCode());
        assertTrue(sphericalBoundingBoxImpl.toString().equals(sphericalBoundingBoxImpl2.toString()));
    }

}
