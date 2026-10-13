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
 *  EarlyBoundCRS is a child interface of the CRS interface. It encapsulates
 *  an early bound coordinate reference system, which has both a {@link LateBoundCRS} and
 *  a {@link CT} specified within so that it is ready for a coordinate transformation operation
 */
public interface EarlyBoundCRS extends CRS {

	/**
	 * get the {@link EarlyBoundCRSEssence} object
	 * @return the {@link EarlyBoundCRSEssence} object
	 */

	public EarlyBoundCRSEssence getEarlyBoundCRSEssence();
	/**
	 *
	 * @param mode {@link RepresentationMode}
	 * @return the a temporary {@link EarlyBoundCRS} copy populated depending on the requested mode
	 */
	public EarlyBoundCRS convert(RepresentationMode mode);
}

