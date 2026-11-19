package org.opengroup.osdu.crs.config;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/** Accepts JSON strings and null without coercing numbers or booleans to strings. */
public final class NullableStringDeserializer extends ValueDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            return parser.getString();
        }
        return (String) context.handleUnexpectedToken(String.class, parser);
    }
}
