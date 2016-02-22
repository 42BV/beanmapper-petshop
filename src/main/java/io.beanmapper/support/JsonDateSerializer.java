package io.beanmapper.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonDateSerializer extends JsonSerializer<LocalDate> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(date.format(dateFormatter));
    }
}
