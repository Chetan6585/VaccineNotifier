package com.opoc.india.vaccine.notifier.cdn;

import java.util.Objects;

public enum VACCINE {
    COVISHIELD,
    COVAXIN,
    ANY_VACCINE;


    public static VACCINE getVaccine(String value) {
        return Objects.nonNull(value) ? valueOf(value) : ANY_VACCINE;
    }
}
