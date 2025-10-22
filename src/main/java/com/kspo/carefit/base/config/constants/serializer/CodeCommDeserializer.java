package com.kspo.carefit.base.config.constants.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import com.kspo.carefit.base.config.constants.util.EnumUtil;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeCommDeserializer<T extends CodeCommInterface> extends
    JsonDeserializer<T> implements ContextualDeserializer {
    private static final Logger log = LoggerFactory.getLogger(CodeCommDeserializer.class);
    private Class<T> targetClass;

    public T deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = (JsonNode)jsonParser.getCodec().readTree(jsonParser);
        if (jsonNode.asText().length() == 1) {
            return (T) EnumUtil.findByCode(this.targetClass, jsonNode.asText());
        } else {
            String reqCode;
            if (jsonNode.get("code") == null) {
                reqCode = jsonNode.asText();
            } else {
                reqCode = jsonNode.get("code").asText();
            }

            return (T) EnumUtil.findByCode(this.targetClass, reqCode);
        }
    }

    @SuppressWarnings("unchecked")
    public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        String targetClassName = ctxt.getContextualType().toCanonical();

        try {
            this.targetClass = (Class<T>) Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            log.error("ERROR : ", e);
        }

        return this;
    }
}

