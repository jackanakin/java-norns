package com.ark.norns.enumerated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class StatusSerializer extends StdSerializer<Status> {
    public StatusSerializer() {
        super(Status.class);
    }

    public void serialize(Status status, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeString(status.name());
        generator.writeFieldName("name");
        generator.writeString(status.getName());
        generator.writeEndObject();
    }
}
