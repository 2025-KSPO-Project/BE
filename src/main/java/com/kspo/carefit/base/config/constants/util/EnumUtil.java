package com.kspo.carefit.base.config.constants.util;

import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import java.util.Arrays;
import java.util.List;

public class EnumUtil {
    private EnumUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T extends CodeCommInterface> T findByCode(Class<T> codeEnum, String code) {
        if (code != null && codeEnum.isEnum()) {
            for(T e : codeEnum.getEnumConstants()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static <T extends CodeCommInterface> List<T> toList(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }
}
