package com.ark.norns.enumerated;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SNMPVersionSerializer extends StdSerializer<SNMPVersion> {
    public SNMPVersionSerializer() {
        super(SNMPVersion.class);
    }

    public void serialize(SNMPVersion snmpv, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeString(snmpv.name());
        generator.writeFieldName("name");
        generator.writeString(snmpv.getName());
        generator.writeEndObject();
    }
}
