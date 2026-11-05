package org.opengroup.osdu.crs.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/** Accepts JSON strings and null without coercing numbers or booleans to strings. */
public final class NullableStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            return parser.getText();
        }
        return (String) context.handleUnexpectedToken(String.class, parser);
    }
}
