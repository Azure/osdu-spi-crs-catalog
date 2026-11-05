package org.opengroup.osdu.crs.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.opengroup.osdu.core.common.model.search.Point;

/** Enforces coordinate types for points-in-aou without changing the shared Point model. */
public final class RequestPointDeserializer extends JsonDeserializer<Point> {
    @Override
    public Point deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode point = parser.readValueAsTree();
        if (!point.isObject() || !point.path("latitude").isNumber() || !point.path("longitude").isNumber()) {
            return context.reportInputMismatch(Point.class,
                    "Each point must contain numeric latitude and longitude");
        }
        return new Point(point.get("latitude").doubleValue(), point.get("longitude").doubleValue());
    }
}
