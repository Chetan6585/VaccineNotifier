package com.opoc.india.vaccine.notifier.cdn.service;

import com.opoc.india.vaccine.notifier.cdn.CdnFacade;
import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.center.SessionPerCenter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CdnService {

    private CdnFacade cdnFacade;

    public List<Center> getActiveVaccinationFor45Plus(String state, String district){
        final List<Center> vaccineCenterByDistrict = cdnFacade.getVaccineCenterByDistrict(district, state);
       return vaccineCenterByDistrict.parallelStream().filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(45)) &&
                center.getAvailableCapacity().longValue() != 0).collect(Collectors.toList());
    }

    public List<Center> getActiveVaccinationFor18Plus(String state, String district){
        final List<Center> vaccineCenterByDistrict = cdnFacade.getVaccineCenterByDistrict(district, state);
        return vaccineCenterByDistrict.parallelStream().filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(18)) &&
                center.getAvailableCapacity().longValue() != 0).collect(Collectors.toList());
    }

    public List<CenterByDistrict> getActiveVaccinationForCovishield18Plus(String state, String district){
        final List<CenterByDistrict> calenderOfCenterFor18Plus = getCalenderOfCenterFor18Plus(state, district);
        calenderOfCenterFor18Plus.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVISHIELD.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });

        return calenderOfCenterFor18Plus;
    }

    public List<CenterByDistrict> getActiveVaccinationForCovishield45Plus(String state, String district){
        final List<CenterByDistrict> calenderOfCenterFor45Plus = getCalenderOfCenterFor45Plus(state, district);
        calenderOfCenterFor45Plus.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVISHIELD.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });
        return calenderOfCenterFor45Plus;
    }

    public List<CenterByDistrict> getActiveVaccinationForCovaxin18Plus(String state, String district){
        final List<CenterByDistrict> calenderOfCenterFor18Plus = getCalenderOfCenterFor18Plus(state, district);
        calenderOfCenterFor18Plus.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVAXIN.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });

        return calenderOfCenterFor18Plus;
    }

    public List<CenterByDistrict> getActiveVaccinationForCovaxin45Plus(String state, String district){

        final List<CenterByDistrict> calenderOfCenterFor45Plus = getCalenderOfCenterFor45Plus(state, district);
        calenderOfCenterFor45Plus.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVAXIN.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });
        return calenderOfCenterFor45Plus;
    }

    public List<CenterByDistrict> getCalenderOfCenterFor18Plus(String state, String district){
        final List<CenterByDistrict> calenderByVaccinationCenter = cdnFacade.getCalenderByVaccinationCenter(district, state);

       calenderByVaccinationCenter.stream().parallel()
                .forEach(centerByDistrict -> {

                    final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                            .filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(18)) &&
                                    center.getAvailableCapacity().longValue() > 1)
                            .collect(Collectors.toList());
                    centerByDistrict.setSessionPerCenter(sessionPerCenters);

                });
        return  calenderByVaccinationCenter.parallelStream().filter(centerByDistrict -> !centerByDistrict.getSessionPerCenter().isEmpty()).collect(Collectors.toList());
    }

    public List<CenterByDistrict> getCalenderOfCenterFor45Plus(String state, String district){
        final List<CenterByDistrict> calenderByVaccinationCenter = cdnFacade.getCalenderByVaccinationCenter(district, state);

        calenderByVaccinationCenter.stream().parallel()
                .forEach(centerByDistrict -> {

                    final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                            .filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(45)) &&
                                    center.getAvailableCapacity().longValue() > 1)
                            .collect(Collectors.toList());
                    centerByDistrict.setSessionPerCenter(sessionPerCenters);

                });
        return  calenderByVaccinationCenter.parallelStream().filter(centerByDistrict -> !centerByDistrict.getSessionPerCenter().isEmpty()).collect(Collectors.toList());
    }

}
