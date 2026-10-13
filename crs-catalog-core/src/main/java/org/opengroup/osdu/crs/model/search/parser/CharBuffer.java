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

import org.opengroup.osdu.crs.model.Utility;

/**
 * A buffer to read the characters of the input string.
 */
public class CharBuffer {
    private char[] charArray;
    private int index = 0;

    /**
     * Constructor
     * @param input input string.
     */
    public CharBuffer(String input)
    {
        if (!Utility.isNullOrEmpty(input))
            this.charArray = input.toCharArray();
    }

    /**
     * Returns true if the current cursor does not point to the end of the buffer.
     * @return true if the current cursor does not point to the end of the buffer; otherwise, false.
     */
    public boolean hasNext()
    {
        return (this.charArray != null && this.index < this.charArray.length);
    }

    /**
     * Return next char in buffer without moving the cursor.
     * @return next char in buffer
     */
    public char getPeek()
    {
        if (hasNext())
            return this.charArray[index];

        throw new IndexOutOfBoundsException() ;
    }

    /**
     * Gets next char in buffer and move the cursor to the next position
     * @return next char in buffer
     */
    public char getNext()
    {
        char ch = getPeek();
        this.index++;
        return ch;
    }

    /**
     * Gets current position of the cursor in buffer.
     * @return current position of the cursor in buffer
     */
    public int getPosition()
    {
        return this.index;
    }
}
