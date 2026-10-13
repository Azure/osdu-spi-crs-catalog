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

import java.util.ArrayList;
import java.util.List;

import org.opengroup.osdu.crs.model.Utility;

/**
 * Created by ZMai on 6/14/2016.
 */
public class TokenScanner {
    /*
     * <Keyword> := <empty>
     *        | <Items>
     *        | <Items> (<OR> <Items>)+
     *
     * <empty> := (<White_Space>)*
     *
     * <Items> := <Item>
     *         | <Item> (<White_Space>+ <Item>)+
     *
     * <Item> := <value>
     *        | <field><FieldOperator><value>
     *        | <Excluded_Operator><value>
     *        | <Excluded_Operator><field><FieldOperator><value>
     *
     * <field> := <Word>
     *         | <Phrase_Quote> <String_Excluding_Phrase_Quote> <Phrase_Quote>
     * <value> := <Word>
     *         | <Phrase_Quote><String_Excluding_Phrase_Quote><Phrase_Quote>
     *
     * <Word := <any characters except <FieldOperator>, <Phrase_Quote> and <White_Space>>
     * <String_Excluding_Phrase_Quote> := <any characters except <Phrase_Quote>>
     * <OR_TOperator> := "OR"
     * <Excluded_Operator> := '-'
     * <FieldValueSeperator> := ':'
     * <Wildcard> := '*'
     * <Phrase_Quote> := '"'
     * <White_Space> := ' '
     *
     */

    /**
     * Returns a list of tokens by parsing the keyword string
     * @param keyword input keyword string
     * @return a list of tokens
     * @throws UnexpectedParameterException an exception will be thrown
     * if the syntax of the keyword string is wrong.
     */
    public List<Token> scan(String keyword) throws UnexpectedParameterException {
        List<Token> tokens = new ArrayList<Token>();
        if (Utility.isNullOrEmpty(keyword)) return tokens;

        CharBuffer charBuffer = new CharBuffer(keyword.trim());
        while (charBuffer.hasNext())
        {
            int position = charBuffer.getPosition();
            char ch = charBuffer.getPeek();

            if (Character.isWhitespace(ch) ||
                ExcludedOperator.isExcludedOperator(ch) ||
                FieldOperator.isFieldOperator(ch) ||
                PhraseQuote.isPhraseQuote(ch))
            {
                ch = charBuffer.getNext();
                if (ExcludedOperator.isExcludedOperator(ch))
                {
                    Token token = new Token(new ExcludedOperator(), position, position + 1);
                    tokens.add(token);
                }

                if (FieldOperator.isFieldOperator(ch))
                {
                    Token token = new Token(new FieldOperator(), position, position + 1);
                    tokens.add(token);
                }

                if (PhraseQuote.isPhraseQuote(ch))
                {
                    Token token = new Token(new PhraseQuote(), position, position + 1);
                    tokens.add(token);

                    readPhrase(charBuffer, tokens);
                }

                continue;
            }

            readSingleWord(charBuffer, tokens);
        }

        return tokens;
    }

    private static final char EscapeChar = '\\';

    /**
     * Gets the keyword string that escapes the escaped characters
     * @param keyword input keyword string
     * @param escapedChar the character to be escaped
     * @return the keyword with escaped characters being escaped.
     */
    public static String escape(String keyword, char escapedChar)
    {
        if (Utility.isNullOrEmpty(keyword))
            return keyword;

        StringBuilder stringBuilder = new StringBuilder();
        CharBuffer charBuffer = new CharBuffer(keyword);
        while (charBuffer.hasNext())
        {
            char ch = charBuffer.getNext();
            if (ch == EscapeChar || ch == escapedChar) //Add escape char
            {
                stringBuilder.append(EscapeChar);
            }

            stringBuilder.append(ch);
        }

        return stringBuilder.toString();
    }

    private void readPhrase(CharBuffer charBuffer, List<Token> tokens) throws UnexpectedParameterException {
        int startPosition = charBuffer.getPosition();
        StringBuilder stringBuilder = new StringBuilder();
        int consecutiveEscape = 0;
        while (charBuffer.hasNext())
        {
            char ch = charBuffer.getPeek();
            if (ch == EscapeChar)
            {
                consecutiveEscape++;
                ch = charBuffer.getNext();
                //Only oven consecutive Escape character is considered a valid character '\'
                if (consecutiveEscape % 2 == 0)
                    stringBuilder.append(ch);
            }
            else
            {
                //If ch == '"' and number of consecutive Escape characters is not odd,
                //then it is true double quote for this string value
                if (PhraseQuote.isPhraseQuote(ch) && consecutiveEscape % 2 != 1)
                    break;

                stringBuilder.append(charBuffer.getNext());
                //reset
                consecutiveEscape = 0;
            }
        }

        if (charBuffer.hasNext() && PhraseQuote.isPhraseQuote(charBuffer.getPeek()))
        {
            int endPosition = charBuffer.getPosition();
            FieldValue obj = new FieldValue(stringBuilder.toString());
            tokens.add(new Token(obj, startPosition, endPosition));

            char ch = charBuffer.getNext(); //Pop the quote char '"'
            tokens.add(new Token(new PhraseQuote(), endPosition, endPosition + 1));
        }
        else
        {
            int position = charBuffer.getPosition();
            throw new UnexpectedParameterException(new PhraseQuote().toString(), position);
        }
    }

    private void readSingleWord(CharBuffer charBuffer, List<Token> tokens) throws UnexpectedParameterException {
        int startPosition = charBuffer.getPosition();
        StringBuilder stringBuilder = new StringBuilder();
        while (charBuffer.hasNext())
        {
            char ch = charBuffer.getPeek();
            if (Character.isWhitespace(ch) || FieldOperator.isFieldOperator(ch))
                break;

            if (PhraseQuote.isPhraseQuote(ch))
            {
                int position = charBuffer.getPosition() + 1;
                throw new UnexpectedParameterException("white space", position);
            }

            stringBuilder.append(charBuffer.getNext());
        }

        String word = stringBuilder.toString();
        if (OrOperator.isOrOperator(word))
            tokens.add(new Token(new OrOperator(), startPosition, startPosition + word.length()));
        else if(!Utility.isNullOrEmpty(word))
            tokens.add(new Token(new FieldValue(word), startPosition, startPosition + word.length()));
    }
}
