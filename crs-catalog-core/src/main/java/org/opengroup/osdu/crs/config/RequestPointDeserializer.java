package org.opengroup.osdu.crs.config;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.JsonNode;
import org.opengroup.osdu.core.common.model.search.Point;

/** Enforces coordinate types for points-in-aou without changing the shared Point model. */
public final class RequestPointDeserializer extends ValueDeserializer<Point> {
    @Override
    public Point deserialize(JsonParser parser, DeserializationContext context) {
        JsonNode point = parser.readValueAsTree();
        if (!point.isObject() || !point.path("latitude").isNumber() || !point.path("longitude").isNumber()) {
            return context.reportInputMismatch(Point.class,
                    "Each point must contain numeric latitude and longitude");
        }
        return new Point(point.get("latitude").doubleValue(), point.get("longitude").doubleValue());
    }
}
