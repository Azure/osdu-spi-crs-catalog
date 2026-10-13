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

/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 */
package org.opengroup.osdu.crs.model.interfaces;

/**
 *  CompoundCRS is a child interface of the CRS interface. It is a compound
 *  coordinate reference system consisting of both a horizontal coordinate reference system and a vertical
 *  coordinate reference system.
 */
public interface CompoundCRS extends CRS{

    /**
     * get the {@link CompoundCRSEssence} object
     * @return the {@link CompoundCRSEssence} object
     */
    public CompoundCRSEssence getCompoundCRSEssence();

    /**
     * Create a copy prepared according to requested representation mode
     * @param mode The requested {@link RepresentationMode}
     * @return a temporary {@link CompoundCRS} copy populated depending on the requested mode
     */
    public CompoundCRS convert(RepresentationMode mode);

}

