package com.ark.norns.enumerated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SensorKindSerializer extends StdSerializer<SensorKind> {
    public SensorKindSerializer() {
        super(SensorKind.class);
    }

    public void serialize(SensorKind sensorKind, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeString(sensorKind.name());
        generator.writeFieldName("name");
        generator.writeString(sensorKind.getName());
        generator.writeEndObject();
    }
}
