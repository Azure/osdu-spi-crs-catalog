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
 *  CompoundCT is a child of the {@link CT} interface. It is a compound
 *  cartographic transformation which consists of a list of child {@link CT}s.
 */
public interface CompoundCT extends CT {

	/**
	 * get the {@link CompoundCTEssence} object
	 * @return the {@link CompoundCTEssence} object
	 */
	public CompoundCTEssence getCompoundCTEssence();

	/**
	 * get the child cartographic transforms
	 * @return list of child cartographic transforms
	 */
	public List<SingleCT> getCTs();

	/**
	 * Create a copy prepared according to requested representation mode
	 * @param mode The requested {@link RepresentationMode}
	 * @return a temporary {@link CompoundCT} copy populated depending on the requested mode
	 */
	public CompoundCT convert(RepresentationMode mode);

}

