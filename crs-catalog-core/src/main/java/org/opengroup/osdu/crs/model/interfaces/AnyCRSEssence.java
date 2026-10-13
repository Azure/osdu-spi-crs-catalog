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

package org.opengroup.osdu.crs.model.interfaces;

public interface AnyCRSEssence extends CRSEssence {
    /**
     * get the well known text for this late bound coordinate reference system
     * @return the well known text string
     */
    public String getWellKnownText();

    /**
     * get the late bound coordinate reference system essence
     * @return the late bound coordinate reference system essence
     */
    public LateBoundCRSEssence getLateBoundCRSEssence();

    /**
     * get the single cartographic transform essence
     * @return the {@link SingleCTEssence}
     */
    public SingleCTEssence getSingleCTEssence();
    /**
     * get the compound cartographic transform essence
     * @return the {@link CompoundCTEssence}
     */
    public CompoundCTEssence getCompoundCTEssence();

    /**
     * get the late-bound horizontal coordinate reference system's Essence
     * @return {@link LateBoundCRSEssence} Essence
     */
    public LateBoundCRSEssence getHorizontalLateBoundCRSEssence();
    /**
     * get the late-bound horizontal coordinate reference system's Essence
     * @return {@link EarlyBoundCRSEssence} Essence
     */
    public EarlyBoundCRSEssence getHorizontalEarlyBoundCRSEssence();

    /**
     * get the late-bound vertical coordinate reference system's Essence
     * @return {@link LateBoundCRSEssence} Essence
     */
    public LateBoundCRSEssence getVerticalLateBoundCRSEssence();
    /**
     * get the late-bound vertical coordinate reference system's Essence
     * @return {@link EarlyBoundCRSEssence} Essence
     */
    public EarlyBoundCRSEssence getVerticalEarlyBoundCRSEssence();

}
