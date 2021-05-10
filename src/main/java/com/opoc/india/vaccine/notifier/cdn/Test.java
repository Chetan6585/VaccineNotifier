package com.opoc.india.vaccine.notifier.cdn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Test {
    public  static void main(String [] s){
        ZonedDateTime zonedUTC = ZonedDateTime.of(LocalDateTime.now(ZoneId.of("UTC")), ZoneId.of("UTC"));
        // converting to IST
        LocalDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
        zonedIST.format(DateTimeFormatter.ofPattern("dd-mm-YYYY HH:MM:SS"));
    }
}
