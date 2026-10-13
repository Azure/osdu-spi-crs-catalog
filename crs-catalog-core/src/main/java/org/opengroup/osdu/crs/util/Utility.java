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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by ZMai on 5/10/2016.
 */
public final class Utility {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static boolean isEqual(double a, double b) {
        if(Double.isNaN(a) || Double.isNaN(b))
            return false;

        return Math.abs(a - b) <= Double.MIN_VALUE;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static <T> String Serialize(T object) throws Exception {
        String jsonInString = CreateGson().toJson(object);

        // Temporary replacement for C# Uri.EscapeDataString(value)
        return URLEncoder.encode(jsonInString, "UTF-8");
    }

    public static <T> T Deserialize(String jsonInString, Class<T> type) throws Exception {
        // Temporary replacement for C# Uri.UnescapeDataString(value);
        jsonInString = URLDecoder.decode(jsonInString, "UTF-8");

        return CreateGson().fromJson(jsonInString, type);
    }

    private static Gson CreateGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.excludeFieldsWithoutExposeAnnotation().create();
    }
}
