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
 *  Essence is a base interface which encapsulates the common features of an Essence object.
 */
public interface Essence extends Root {

	/**
	 * get the {@link AuthorityCode} property
	 * @return the {@link AuthorityCode} property object
	 */
	public AuthorityCode getAuthorityCode();

	/**
	 * get the type property
	 * @return the type property
	 */
	public String getType();
}

