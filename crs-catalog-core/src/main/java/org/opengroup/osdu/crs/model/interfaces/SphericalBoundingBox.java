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
 *  SphericalBoundingBox is an interface which encapsulates a two dimensional spherical bounding box
 *  consisting of lower left corner and upper right corner to describe an area.
 */
public interface SphericalBoundingBox extends Root {


	/**
	 * Gets the left longitude value
	 * @return left longitude value
	 */
	public double getLongitudeLeft();

	/**
	 * Gets the lower latitude value
	 * @return lower latitude value
	 */
	public double getLatitudeLower();

	/**
	 * Gets the right longitude value
	 * @return right longitude value
	 */
	public double getLongitudeRight();

	/**
	 * Gets the upper latitude value
	 * @return upper latitude value
	 */
	public double getLatitudeUpper();

}

