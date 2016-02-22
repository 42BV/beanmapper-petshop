package io.beanmapper.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonDateDeserializer extends JsonDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
        return LocalDate.parse(jsonparser.getText(), formatter);
    }
}