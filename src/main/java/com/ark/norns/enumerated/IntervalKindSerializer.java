package com.ark.norns.enumerated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class IntervalKindSerializer extends StdSerializer<IntervalKind> {
    public IntervalKindSerializer() {
        super(IntervalKind.class);
    }

    public void serialize(IntervalKind interval, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeString(interval.name());
        generator.writeFieldName("name");
        generator.writeString(interval.getName());
        generator.writeEndObject();
    }
}
