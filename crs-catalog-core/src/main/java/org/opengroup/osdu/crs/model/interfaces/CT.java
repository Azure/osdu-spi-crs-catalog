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
 */
package org.opengroup.osdu.crs.model.interfaces;

/**
 *  A base interface which specifies a cartographic transformation.
 */
public interface CT extends CRSCTBase {

	/**
	 * get the from coordinate reference system's {@link AuthorityCode}.
	 * @return  the from coordinate reference system's {@link AuthorityCode}.
	 */
	public AuthorityCode getFromCRSAuthorityCode();

	/**
	 * get the to coordinate reference system's {@link AuthorityCode}.
	 * @return  the to coordinate reference system's {@link AuthorityCode}
	 */
	public AuthorityCode getToCRSAuthorityCode();

	/**
	 * Get the {@link CTDeprecationInfo} object
	 * @return the {@link CTDeprecationInfo} object
	 */
	public CTDeprecationInfo getCTDeprecationInfo();

}

