package org.opengroup.osdu.crs.config;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/** Boolean request fields accept JSON booleans without scalar coercion. */
public final class StrictBooleanDeserializer extends ValueDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext context) {
        if (parser.hasToken(JsonToken.VALUE_TRUE) || parser.hasToken(JsonToken.VALUE_FALSE)) {
            return parser.getBooleanValue();
        }
        return (Boolean) context.handleUnexpectedToken(Boolean.class, parser);
    }
}
