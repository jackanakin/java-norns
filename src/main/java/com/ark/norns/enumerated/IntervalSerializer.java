package com.ark.norns.enumerated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class IntervalSerializer extends StdSerializer<Interval> {
    public IntervalSerializer() {
        super(Interval.class);
    }

    public void serialize(Interval interval, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeString(interval.name());
        generator.writeFieldName("name");
        generator.writeString(interval.getName());
        generator.writeFieldName("time");
        generator.writeNumber(interval.getTime());
        generator.writeEndObject();
    }
}
