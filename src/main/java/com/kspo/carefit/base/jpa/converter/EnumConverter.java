package com.kspo.carefit.base.jpa.converter;

import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import com.kspo.carefit.base.config.constants.util.EnumUtil;
import jakarta.persistence.AttributeConverter;

public interface EnumConverter<T extends CodeCommInterface> extends AttributeConverter<T, String> {

    @Override
    default String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    default T convertToEntityAttribute(String dbData) {
        return null;
    }

    default T convertToEntityAttribute(String dbData, Class<T> tClass) {
        return EnumUtil.findByCode(tClass, dbData);
    }

}
