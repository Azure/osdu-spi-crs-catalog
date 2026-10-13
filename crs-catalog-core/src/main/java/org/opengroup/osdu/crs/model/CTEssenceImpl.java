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
package org.opengroup.osdu.crs.model;

import org.opengroup.osdu.crs.model.interfaces.CTEssence;

/**
 *  CTEssenceImpl is a base class which encapsulates the essence of a cartographic transformation
 */
public class CTEssenceImpl extends CRSCTEssenceImpl implements CTEssence {

	/**
	 * defines the single cartographic transform type.
	 */
	public static final String SCT = "ST";

	/**
	 * defines the compound cartographic transform type.
	 */
	public static final String CCT = "CT";

	/**
	 * Constructor
	 */
    public CTEssenceImpl() {
    	super();
    }

	/*
     * create an {@link CTEssenceImpl from a JSON string
     * @param json the input JSON string
	 * @return the fully initialized{@link CTEssenceImpl} object
	 * @throws Exception which indicates failure to create the object
     */
	public static CTEssenceImpl fromJson(String json) throws Exception {
		return Utility.fromJson(json, CTEssenceImpl.class);
	}
}

