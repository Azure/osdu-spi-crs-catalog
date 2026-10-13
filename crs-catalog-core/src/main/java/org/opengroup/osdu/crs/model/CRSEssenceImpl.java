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
package org.opengroup.osdu.crs.model;

import org.opengroup.osdu.crs.model.interfaces.CRSEssence;

/**
 *  CRSEssenceImpl is a class which encapsulates the essence of a coordinate reference system
 */
public class CRSEssenceImpl extends CRSCTEssenceImpl implements CRSEssence {

	/**
	 * defines the late bound coordinate reference system type.
	 */
	public static final String LBCRS = "LBC";

	/**
	 * defines the early bound coordinate reference system type.
	 */
	public static final String EBCRS = "EBC";

	/**
	 * defines the compound coordinate reference system type.
	 */
	public static final String CCRS = "CC";

	/**
	 * Constructor
	 */
	public CRSEssenceImpl() {
		super();
	}

	/*
     * create an {@link CRSEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CRSEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static CRSEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, CRSEssenceImpl.class);
	}
}

