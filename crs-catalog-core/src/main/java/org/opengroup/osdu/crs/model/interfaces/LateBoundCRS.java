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
 *  LateBoundCRS is a child interface of the CRS interface
 *  which encapsulate a late bound coordinate reference system. It needs to be associated with
 *  a {@link CT} before a coordinate transformation operation can be performed.
 */
public interface LateBoundCRS extends CRS {

	/**
	 * get the {@link LateBoundCRSEssence} object
	 * @return the {@link LateBoundCRSEssence} object
	 */
	public LateBoundCRSEssence getLateBoundCRSEssence();

	/**
	 * get the source
	 * @return source string
	 */
	public String getSource();

	/**
	 *
	 * @param mode {@link RepresentationMode}
	 * @return a temporary {@link LateBoundCRS} copy populated depending on the requested mode
	 */
	public LateBoundCRS convert(RepresentationMode mode);
}

