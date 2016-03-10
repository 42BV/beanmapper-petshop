package io.beanmapper.support;

import io.beanmapper.core.converter.SimpleBeanConverter;

import java.time.LocalDate;

public class AgeCalculator extends SimpleBeanConverter<LocalDate, Integer> {

    @Override
    public Integer doConvert(LocalDate birthDate) {
        return birthDate.until(LocalDate.now()).getYears();
    }
}
