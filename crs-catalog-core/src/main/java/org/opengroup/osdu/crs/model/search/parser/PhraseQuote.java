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
 * A class represents the quote operator, '"'.
 */
public class PhraseQuote {
    private static final char Self = '"';

    /**
     * Check whether a character is the quote operator.
     * @param ch character
     * @return true if the ch is the quote operator; otherwise, false.
     */
    public static boolean isPhraseQuote(char ch)
    {
        return Self == ch;
    }

    @Override
    public String toString()
    {
        return String.valueOf(Self);
    }
}
