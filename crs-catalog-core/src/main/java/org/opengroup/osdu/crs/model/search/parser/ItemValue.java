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
package org.opengroup.osdu.crs.model.search.parser;

/**
 * An item value defines a word or a list of words that are quoted by '"'.
 */
public class ItemValue {
    private String value;
    private boolean isQuoted;

    /**
     * Constructor. The value is not quoted by '"'
     * @param value a word
     */
    public ItemValue(String value)
    {
        this(value, false);
    }

    /**
     * Constructor.
     * @param value a word or a list of words if isQuoted is true
     * @param isQuoted whether the value is quoted by '"'
     */
    public ItemValue(String value, boolean isQuoted)
    {
        this.value = value;
        this.isQuoted = isQuoted;
    }

    /**
     * Gets the value that is a word or a list of words that are quoted by '"'.
     * @return the value
     */
    public String getValue() { return value;}

    /**
     * Gets whether the value is quoted by '"'
     * @return true if it is quoted; otherwise, false.
     */
    public boolean isQuoted() { return isQuoted; }

    @Override
    public String toString()
    {
        return isQuoted ? "\"" + TokenScanner.escape(value, '"') + "\"" : value;
    }
}
