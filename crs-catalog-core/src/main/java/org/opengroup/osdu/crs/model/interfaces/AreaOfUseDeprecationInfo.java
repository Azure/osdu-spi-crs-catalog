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
 *  AreaOfUseDeprecationInfo is an interface which encapsulate an area of use's deprecation information.
 *  It is a child of the DeprecationInfo interface
 */
public interface AreaOfUseDeprecationInfo extends DeprecationInfo {

	/**
	 * get {@link AreaOfUse} which the supersedes it
	 * @return {@link AreaOfUse} which the supersedes it
	 */
	public AreaOfUse getSupersededByArea();

}
