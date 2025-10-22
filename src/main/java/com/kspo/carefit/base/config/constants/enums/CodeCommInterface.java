package com.kspo.carefit.base.config.constants.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kspo.carefit.base.config.constants.serializer.CodeCommDeserializer;
import com.kspo.carefit.base.config.constants.serializer.CodeCommSerializer;

@JsonSerialize(
    using = CodeCommSerializer.class
)
@JsonDeserialize(
    using = CodeCommDeserializer.class
)
public interface CodeCommInterface {
    String getCode();

    String getCodeName();
}
