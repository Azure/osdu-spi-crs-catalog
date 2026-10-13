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
 *  Base is a CRSCTBase interface for common features of a coordinate reference system or a cartographic transformation.
 */
public interface CRSCTBase extends Base {

	/**
	 * Get the {@link AreaOfUse} of the coordinate reference system or cartographic transformation.
	 * @return the {@link AreaOfUse} of the coordinate reference system or cartographic transformation.
	 */
	public AreaOfUse getAreaOfUse();

	/**
	 * get the coordinate reference system or cartographic transform source.
	 * @return the coordinate reference system or cartographic transform source.
	 */
	public String getSource();
	/**
	 *
	 * @param mode The requested {@link RepresentationMode}
	 * @return the {@link CRSCTBase}
	 */
	public CRSCTBase convert(RepresentationMode mode);

}

