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

package org.opengroup.osdu.crs.util;

public class Config {

    public static String getEntitlementsHostUrl() {
        return getEnvironmentVariable("ENTITLEMENT_URL");
    }

    public static int getAllowedClockSkewSeconds() {
        return Integer.parseInt(getEnvironmentOrDefaultVariable("ALLOWED_CLOCK_SKEW_SECONDS", "120"));
    }

    public static int getCacheTimeToLive() {
        return Integer.parseInt(getEnvironmentOrDefaultVariable("CACHE_TIME_TO_LIVE","60"));
    }

    public static String getEnvironmentVariable(String propertyKey) {
        String property = System.getProperty(propertyKey, System.getenv(propertyKey));
        if ((property == null) || (property.isEmpty())) {
            return null;
        }
        return property;
    }

    public static String getEnvironmentOrDefaultVariable(String propertyKey, String defaultValue) {
        String property = System.getProperty(propertyKey, System.getenv(propertyKey));
        if ((property == null) || (property.isEmpty())) {
            return defaultValue;
        }
        return property;
    }
}
