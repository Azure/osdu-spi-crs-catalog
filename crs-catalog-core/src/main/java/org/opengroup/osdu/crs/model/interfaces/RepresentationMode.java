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
import com.google.common.base.Strings;

/*
 * Output representation mode enumeration
 */
public enum  RepresentationMode {
    ESSENCE, PERSISTABLE_REFERENCE, ESSENCE_AND_PERSISTABLE_REFERENCE;

    /**
     * Get the output mode enum
     * @return the persistable reference string of the object
     */
    public static RepresentationMode getRepresentationMode(String mode){
        if (!Strings.isNullOrEmpty(mode)) {
            String lower = mode.toLowerCase();
            boolean per = lower.contains("pers");
            boolean ref = lower.contains("ref");
            boolean ess = lower.contains("ess");
            if ((per || ref) && ess) return ESSENCE_AND_PERSISTABLE_REFERENCE;
            else if (ess) return ESSENCE;
        }
        return PERSISTABLE_REFERENCE;  // default
    }
}
