package com.opoc.india.vaccine.notifier.infrastructure.settings;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
public class VaccineNotifierPropertyConfiguration {
    @Value("${application.url.cdn.covinByDistrict}")
    private String vaccineCenterByDistrictUrl;

    @Value("${application.url.cdn.states}")
    private String statesUrl;

    @Value("${application.url.cdn.districts}")
    private String districtUrl;

    @Value("${application.url.cdn.calenderByDistrict}")
    private String calenderByVaccineCenterUrl;

    @Value("${application.userName}")
    private String userName;
    @Value("${application.password}")
    private String password;
}

