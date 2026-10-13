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
 *
 */
package org.opengroup.osdu.crs.model.interfaces;

/**
 *  AreaOfUse is an interface which encapsulates an area of use for a coordinate reference system
 *  or a cartographic transform. The area of use is specified by an {@link AuthorityCode} and
 *  a {@link SphericalBoundingBox} as well as other properties and information.
 */
public interface AreaOfUse extends Base {

    /**
     * Get the name of the {@link AreaOfUse}
     * @return the name of the {@link AreaOfUse}
     */
    public String getName();

    /**
     * get the {@link AreaOfUseEssence} object
     * @return the {@link AreaOfUseEssence} object
     */
    public AreaOfUseEssence getAreaOfUseEssence();

    /**
     * Get the {@link AreaOfUseDeprecationInfo} object
     * @return the {@link AreaOfUseDeprecationInfo} object
     */
    public AreaOfUseDeprecationInfo getAreaOfUseDeprecationInfo();

    /**
     *
     * @param mode The requested {@link RepresentationMode}
     * @return the {@link AreaOfUse}
     */
    public AreaOfUse convert(RepresentationMode mode);
}

