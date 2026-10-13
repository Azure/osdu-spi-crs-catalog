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

import java.util.List;


/**
 *  CRS is a base interface which encapsulates a coordinate reference system.
 */
public interface CRS extends CRSCTBase {

	/**
	 * get the base coordinate reference system's authority code.
	 * @return the base coordinate reference system's authority code.
	 */
	public AuthorityCode getBaseCRSAuthorityCode();

	/**
	 * get the number of axes.
	 * @return the number of axes.
	 */
	public int getNumberOfAxes();

	/**
	 * get the axis units as unit persistable references.
	 * @return axis unit persistable references as list of strings.
	 */
	public List<String> getAxisUnits();

	/**
	 * get the coordinate reference system type.
	 * @return the coordinate reference system type.
	 */
	public String getCRSType();

	/**
	 * get the transformation ready status.
	 * @return true if ready for transformation; false otherwise.
	 */
	public boolean isTransformationReady();

	/**
	 * Get the {@link CRSDeprecationInfo} object
	 * @return the {@link CRSDeprecationInfo} object
	 */
	public CRSDeprecationInfo getCRSDeprecationInfo();

}

