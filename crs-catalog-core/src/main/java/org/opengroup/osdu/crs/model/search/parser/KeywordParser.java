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
 * Keyword parser
 */
public class KeywordParser {

    /**
     * Parse the keyword to a list of items
     * @param keyword keyword to be parsed
     * @return a list of items parsed from the keyword
     * @throws UnexpectedParameterException an exception will be thrown if the syntax of the keyword is wrong
     * @throws MissingFieldValueException an exception will be thrown if the expected field value is missed
     */
    public List<List<Item>> parse(String keyword) throws UnexpectedParameterException, MissingFieldValueException {
        List<List<Item>> items = new ArrayList<List<Item>>();

        TokenScanner scanner = new TokenScanner();
        List<Token> tokens = scanner.scan(keyword);
        if (tokens.size() == 0) return items;

        List<List<Token>> trunks = trunkTokensBySpaceSeperator(tokens);
        List<Item> currentItemList = new ArrayList<>();
        items.add(currentItemList);
        for (List<Token> trunk : trunks) {
            if (trunk.size() == 0) continue;

            if (trunk.size() == 1 && trunk.get(0).isOrOperator()) {
                currentItemList = new ArrayList<>();
                items.add(currentItemList);

                //Skip OR operator
            } else {
                Item item = parseItem(trunk);
                if (item != null)
                    currentItemList.add(item);
            }
        }

        List<List<Item>> nonEmptyItems = new ArrayList<List<Item>>();
        for (List<Item> itemList : items) {
            if (itemList.size() > 0)
                nonEmptyItems.add(itemList);
        }
        return nonEmptyItems;
    }


    private List<List<Token>> trunkTokensBySpaceSeperator(List<Token> tokens) {
        List<List<Token>> trunks = new ArrayList<List<Token>>();
        Token currentToken = tokens.get(0);
        List<Token> currentTrunk = new ArrayList<Token>();
        currentTrunk.add(currentToken);

        trunks.add(currentTrunk);
        for (int i = 1; i < tokens.size(); i++) {
            Token nextToken = tokens.get(i);

            if (isSeparatedBySpace(currentToken, nextToken)) {
                currentTrunk = new ArrayList<Token>();
                trunks.add(currentTrunk);
            }

            currentToken = nextToken;
            currentTrunk.add(currentToken);
        }

        return trunks;
    }

    private boolean isSeparatedBySpace(Token current, Token next) {
        return (current.getEndPosition() < next.getStartPosition());
    }

    private Item parseItem(List<Token> tokens) throws UnexpectedParameterException, MissingFieldValueException {
        if (tokens == null || tokens.size() == 0)
            return null;

        Token firstToken = tokens.get(0);
        if (firstToken.isFieldOperator() || (firstToken.isExcludedOperator() && tokens.size() == 1))
            throw new UnexpectedParameterException(firstToken.toString(), firstToken.getStartPosition());

        Item item;
        if (firstToken.isExcludedOperator()) {
            List<Token> tokensExceptFirstOne = new ArrayList<>();
            for (int i = 1; i < tokens.size(); i++)
                tokensExceptFirstOne.add(tokens.get(i));
            Item tmp = parseItemWithoutExcludedPrefix(tokensExceptFirstOne);
            item = new Item(tmp.getField(), tmp.getValue(), true);
        } else
            item = parseItemWithoutExcludedPrefix(tokens);

        return item;
    }

    private Item parseItemWithoutExcludedPrefix(List<Token> tokens) throws UnexpectedParameterException, MissingFieldValueException {
        Token firstToken = tokens.get(0);
        Token lastToken = tokens.get(tokens.size() - 1);
        if (firstToken.isFieldOperator() || firstToken.isExcludedOperator())
            throw new UnexpectedParameterException(firstToken.toString(), firstToken.getStartPosition());

        List<Token> fieldOperators = new ArrayList<>();
        for (Token token : tokens) {
            if (token.isFieldOperator())
                fieldOperators.add(token);
        }
        if (fieldOperators.size() == 0) {
            ItemValue fieldValue = parseItemValue(tokens);
            return (fieldValue == null || Utility.isNullOrEmpty(fieldValue.getValue())) ? null : new Item(fieldValue);
        }

        if (fieldOperators.size() > 1) {
            Token unexpectedToken = fieldOperators.get(1);
            throw new UnexpectedParameterException(unexpectedToken.toString(), unexpectedToken.getStartPosition());
        }

        //fieldValueSeperatorCount == 1
        if (lastToken.isFieldOperator()) {
            //TODO: Use this specific exception to trigger fetching the facets for keyword suggestions in the UI
            int nextPosition = lastToken.getStartPosition() + lastToken.toString().length();
            throw new MissingFieldValueException(nextPosition);
        }

        // Take all the field tokens before the first field operator
        List<Token> fieldTokens = new ArrayList<>();
        for (Token token : tokens) {
            if (!token.isFieldOperator())
                fieldTokens.add(token);
            else
                break;
        }
        ItemValue field = parseItemValue(fieldTokens);
        if (field == null || Utility.isNullOrEmpty(field.getValue())) {
            int position = fieldTokens.get(0).getStartPosition();
            throw new UnexpectedParameterException("empty field name", position);
        }

        List<Token> contentTokens = new ArrayList<>();
        for (int i = fieldTokens.size() + 1; i < tokens.size(); i++)
            contentTokens.add(tokens.get(i));
        ItemValue content = parseItemValue(contentTokens);
        if (content == null || Utility.isNullOrEmpty(content.getValue())) {
            int position = contentTokens.get(0).getStartPosition();
            throw new UnexpectedParameterException("empty field value", position);
        }
        return new Item(field, content);
    }

    private ItemValue parseItemValue(List<Token> tokens) throws UnexpectedParameterException {
        Token firstToken = tokens.get(0);
        Token lastToken = tokens.get(tokens.size() - 1);
        if (firstToken.isFieldOperator() || firstToken.isExcludedOperator())
            throw new UnexpectedParameterException(firstToken.toString(), firstToken.getStartPosition());

        if (firstToken.isPhraseQuote() && !lastToken.isPhraseQuote()) {
            int position = lastToken.getStartPosition() + lastToken.toString().length();
            throw new UnexpectedParameterException(firstToken.toString(), position);
        }
        if (!firstToken.isPhraseQuote() && lastToken.isPhraseQuote()) {
            int position = Math.max(0, firstToken.getStartPosition() - 1);
            throw new UnexpectedParameterException(lastToken.toString(), position);
        }

        if (firstToken.isPhraseQuote()) {
            if (tokens.size() != 3)
                throw new UnexpectedParameterException("empty string", firstToken.getStartPosition() + 1);

            Token valueToken = tokens.get(1);
            return new ItemValue(valueToken.toString(), true);
        }

        if (tokens.size() == 1)
            return new ItemValue(firstToken.toString());

        throw new UnexpectedParameterException(firstToken.toString(), firstToken.getStartPosition());
    }
}

