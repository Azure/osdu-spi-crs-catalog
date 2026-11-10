package org.opengroup.osdu.crs.config;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/** Numeric request fields accept integral and fractional JSON numbers. */
public final class StrictDoubleDeserializer extends ValueDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context) {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT) || parser.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
            return parser.getDoubleValue();
        }
        return (Double) context.handleUnexpectedToken(Double.class, parser);
    }
}
