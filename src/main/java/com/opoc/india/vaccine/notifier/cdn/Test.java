package com.opoc.india.vaccine.notifier.cdn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Test {
    public  static void main(String [] s){
        LocalDateTime localNow = LocalDateTime.now();
        // setting UTC as the timezone
        ZonedDateTime zonedUTC = localNow.atZone(ZoneId.of("UTC+2"));
        // converting to IST
        LocalDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
        zonedIST.format(DateTimeFormatter.ofPattern("dd-mm-YYYY HH:MM:SS"));
    }
}
