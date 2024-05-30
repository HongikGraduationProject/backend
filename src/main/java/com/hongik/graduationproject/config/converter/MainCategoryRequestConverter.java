package com.hongik.graduationproject.config.converter;

import com.hongik.graduationproject.enums.MainCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class MainCategoryRequestConverter implements Converter<String, MainCategory> {
    @Override
    public MainCategory convert(String source) {
        return MainCategory.valueOf(source.toUpperCase());
    }
}