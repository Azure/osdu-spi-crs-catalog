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
 *  DeprecationInfo is an interface which encapsulates a set of deprecation information properties
 */
public interface DeprecationInfo extends Root {

    /**
     * get the deprecation state
     * @return the deprecation state
     */
    public String getDeprecationState();

    /**
     * get the deprecation remarks
     * @return the deprecation remarks
     */
    public String getRemarks();
}
