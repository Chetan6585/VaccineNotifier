package com.opoc.india.vaccine.notifier.cdn.service;

import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.email.GmailSender;
import com.opoc.india.vaccine.notifier.infrastructure.settings.VaccineNotifierPropertyConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Builder
@Slf4j
@Getter
public class VaccineFinderTask implements Runnable {

    private Integer age;
    private String state;
    private String district;
    private VACCINE vaccine;
    private CdnService cdnService;
    private String recipentEmailId;
    private VaccineNotifierPropertyConfiguration notifierPropertyConfiguration;

    @Override
    public void run() {
        LocalDateTime threadStartTime = LocalDateTime.now();

        while (true) {
            try {
                if (age.equals(45)) {
                    switch (vaccine) {
                        case COVAXIN:
                            printCenterByDistrictLog(cdnService.getActiveVaccinationForCovaxin45Plus(state, district));
                            break;
                        case COVISHIELD:
                            printCenterByDistrictLog(cdnService.getActiveVaccinationForCovishield45Plus(state, district));
                            break;
                        default:
                            printCenterByDistrictLog(cdnService.getCalenderOfCenterFor45Plus(state, district));
                            break;
                    }
                } else {
                    switch (vaccine) {
                        case COVAXIN:
                            printCenterByDistrictLog(cdnService.getActiveVaccinationForCovaxin18Plus(state, district));
                            break;
                        case COVISHIELD:
                            printCenterByDistrictLog(cdnService.getActiveVaccinationForCovishield18Plus(state, district));
                            break;
                        default:
                            printCenterByDistrictLog(cdnService.getCalenderOfCenterFor18Plus(state, district));
                            break;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                threadSleep(40000);
            }
      //      log.info("Thread going for sleep:" + LocalDateTime.now());
            threadSleep(20000);
       //     log.info("Thread out of sleep:" + LocalDateTime.now());

            final Duration threadDuration = Duration.between(threadStartTime, LocalDateTime.now());
            if (threadDuration.toMinutes() > 10) {
                log.info("Thread running for: {}, {}, {}, {}",this.district, this.recipentEmailId, this.age, this.vaccine);
            }

        }
    }

    private void threadSleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void printLog(List<Center> centers) {
        if (!centers.isEmpty()) {
            log.info(String.valueOf(centers));
        }
    }

    private void printCenterByDistrictLog(List<CenterByDistrict> centers) throws MessagingException {
        GmailSender sender = new GmailSender();
        StringBuilder messageBody = new StringBuilder();
        sender.setSender(notifierPropertyConfiguration.getUserName(), notifierPropertyConfiguration.getPassword());
        sender.addRecipient(recipentEmailId);

        if (!centers.isEmpty()) {
            centers.forEach(centerByDistrict -> {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder
                        .append("\n")
                        .append("Center:" + centerByDistrict.getName())
                        .append("\n")
                        .append("State:" + centerByDistrict.getStateName())
                        .append("\n")
                        .append("District:" + centerByDistrict.getDistrictName())
                        .append("\n")
                        .append("Address:" + centerByDistrict.getAddress())
                        .append("\n")
                        .append("PinCode:" + centerByDistrict.getPincode())
                        .append("\n");
                centerByDistrict.getSessionPerCenter().forEach(sessionPerCenter ->
                        stringBuilder.append("availableCapacity:" + sessionPerCenter.getAvailableCapacity())
                                .append("\n")
                                .append("minAge:" + sessionPerCenter.getMinAgeLimit())
                                .append("\n")
                                .append("date:" + sessionPerCenter.getDate())
                                .append("\n")
                                .append("Vaccine:" + sessionPerCenter.getVaccine())
                                .append("\n")
                                .append("slots:" + sessionPerCenter.getSlots())
                                .append("\n")
                );
                messageBody.append(stringBuilder);
                messageBody.append("\n\n\n\n\n");
                log.info(stringBuilder.toString());
            });
            try {
                LocalDateTime zonedIST = getISTLocalDateTime();
                sender.setSubject("Vaccine Available for Age: "+this.age+" at: "+this.district+" " + zonedIST.format(DateTimeFormatter.ofPattern("dd-mm-YYYY HH:MM:SS")));
                sender.setBody(messageBody.toString());
                sender.send();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDateTime getISTLocalDateTime() {
        LocalDateTime localNow = LocalDateTime.now();
        // setting UTC as the timezone
        ZonedDateTime zonedUTC = localNow.atZone(ZoneId.of("UTC+2"));
        // converting to IST
        LocalDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
        return zonedIST;
    }


}
