package com.kspo.carefit.base.config.web;

import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import com.kspo.carefit.base.config.constants.util.EnumUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

final class StringToEnumConverterFactory implements ConverterFactory<String, CodeCommInterface> {

    @Override
    public <T extends CodeCommInterface> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private record StringToEnumConverter<T extends CodeCommInterface>(
        Class<T> targetClass) implements Converter<String, T> {

        public T convert(String source) {
            return EnumUtil.findByCode(this.targetClass, source);
        }
    }

}