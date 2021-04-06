package com.tekmentor.resiliencectf.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ContextMapConverter implements Converter<String,Integer> {

    @Override
    public Integer convert(String s) {
        return null;
    }

    @Override
    public <U> Converter<String, U> andThen(Converter<? super Integer, ? extends U> after) {
        return null;
    }
}
