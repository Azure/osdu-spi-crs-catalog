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

/*
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model.interfaces;

import java.util.List;

/**
 *  CompoundCTEssence is an interface which encapsulates the Essence of a compound
 *  cartographic transformation. It is a child interface of the CTEssence interface
 */
public interface CompoundCTEssence extends CTEssence {

    /**
     * get the policy
     * @return the policy string
     */
	public String getPolicy();

    /**
     * get the child cartographic transforms' Essences
     * @return list of child cartographic transforms' Essences
     */
	public List<CTEssence> getCTEssences();

    /**
     * get the child cartographic transforms' {@link AuthorityCode}'s
     * @return list of child cartographic transforms' {@link AuthorityCode}'s
     */
    public List<AuthorityCode> getCTAuthorityCodes();

}

