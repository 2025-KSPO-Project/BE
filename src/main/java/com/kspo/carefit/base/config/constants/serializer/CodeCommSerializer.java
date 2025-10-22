package com.kspo.carefit.base.config.constants.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import java.io.IOException;

public class CodeCommSerializer extends StdSerializer<CodeCommInterface> {
    public CodeCommSerializer() {
        this((Class)null);
    }

    protected CodeCommSerializer(Class<CodeCommInterface> t) {
        super(t);
    }

    public void serialize(CodeCommInterface value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeStartObject();
            gen.writeStringField("code", "");
            gen.writeStringField("codeName", "");
            gen.writeEndObject();
        } else {
            gen.writeStartObject();
            gen.writeStringField("code", value.getCode());
            gen.writeStringField("codeName", value.getCodeName());
            gen.writeEndObject();
        }

    }
}
