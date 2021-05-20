package com.opoc.india.vaccine.notifier.cdn.service;

import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.email.GmailSender;
import com.opoc.india.vaccine.notifier.cdn.subscriber.Subscriber;
import com.opoc.india.vaccine.notifier.infrastructure.settings.VaccineNotifierPropertyConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Slf4j
@Getter
@EqualsAndHashCode(exclude = {"notifierPropertyConfiguration", "cdnService"})
public class VaccineFinderTask implements Runnable {


    private Subscriber subscriber;
    private CdnService cdnService;
    private VaccineNotifierPropertyConfiguration notifierPropertyConfiguration;
    @Builder.Default
    private LocalDateTime lastMailTiming = LocalDateTime.now().minusMinutes(3);

    @Override
    public void run() {
        LocalDateTime lastlogInfoTiming = LocalDateTime.now();

        while (true) {
            try {
                switch (VACCINE.getVaccine(subscriber.getVaccine())) {
                    case COVAXIN:
                        printCenterByDistrictLog(cdnService.getActiveVaccinationForCovaxin(this.subscriber));
                        break;
                    case COVISHIELD:
                        printCenterByDistrictLog(cdnService.getActiveVaccinationForCovishield(this.subscriber));
                        break;
                    default:
                        printCenterByDistrictLog(cdnService.getCalenderOfCenter(this.subscriber));
                        break;
                }

            } catch (Exception e) {
                log.error(e.getMessage());
                //      e.printStackTrace();
                threadSleep(20000);
            }
            //      log.info("Thread going for sleep:" + LocalDateTime.now());
            threadSleep(15000);
            //     log.info("Thread out of sleep:" + LocalDateTime.now());

            final Duration threadDuration = Duration.between(lastlogInfoTiming, LocalDateTime.now()).abs();
            if (threadDuration.toHours() > 2) {
                lastlogInfoTiming = LocalDateTime.now();
                //log.info("Thread running for: {}, {}, {}, {}",this.district, this.recipentEmailId, this.age, this.vaccine);
                sendThreadInfo();
            }

        }
    }

    private void sendThreadInfo() {
        try {
            GmailSender sender = new GmailSender();
            StringBuilder messageBody = new StringBuilder();
            sender.setSender(notifierPropertyConfiguration.getEmailId(), notifierPropertyConfiguration.getPassword());

            sender.addRecipient("chetan.thetiger@gmail.com");

            messageBody.append("\n")
                    .append("Thread is Running:")
                    .append("\n")
                    .append("Email:" + this.subscriber.getEmail())
                    .append("\n")
                    .append("District:" + this.subscriber.getDistrict())
                    .append("\n")
                    .append("Vaccine:" + VACCINE.getVaccine(this.subscriber.getVaccine()));

            sender.setBody(messageBody.toString());
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
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
        final Duration duratoinBetween = Duration.between(this.lastMailTiming, LocalDateTime.now()).abs();
        if (duratoinBetween.toMinutes() < 1) {
            return;
        }
        this.lastMailTiming = LocalDateTime.now();
        GmailSender sender = new GmailSender();
        StringBuilder messageBody = new StringBuilder();
        sender.setSender(notifierPropertyConfiguration.getEmailId(), notifierPropertyConfiguration.getPassword());
        sender.addRecipient(subscriber.getEmail());
        messageBody.append("\n\n\n\n\n");
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
                        .append("\n")
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
                                .append("\n")
                );
                messageBody.append(stringBuilder);
                messageBody.append("\n\n\n\n\n");
           //     log.info(stringBuilder.toString());
            });
            try {
                LocalDateTime zonedIST = getISTLocalDateTime();
                String serachBy = Objects.nonNull(subscriber.getPincode()) ? "Pincode- " + this.subscriber.getPincode() : " district- " + this.subscriber.getDistrict();
                final String subject = "Vaccine Available at: " + serachBy + " for age: " +this.subscriber.getAge() ;
                sender.setSubject(subject);
                sender.setBody(messageBody.toString());
                sender.send();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDateTime getISTLocalDateTime() {
        // setting UTC as the timezone
        ZonedDateTime zonedUTC = ZonedDateTime.of(LocalDateTime.now(ZoneId.of("UTC")), ZoneId.of("UTC"));
        // converting to IST
        LocalDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
        return zonedIST;
    }


}
