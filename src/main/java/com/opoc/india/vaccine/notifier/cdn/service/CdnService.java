package com.opoc.india.vaccine.notifier.cdn.service;

import com.opoc.india.vaccine.notifier.cdn.CdnFacade;
import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.center.SessionPerCenter;
import com.opoc.india.vaccine.notifier.cdn.subscriber.Subscriber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CdnService {

    private CdnFacade cdnFacade;

    public List<Center> getActiveVaccinationFor45Plus(String state, String district) {
        final List<Center> vaccineCenterByDistrict = cdnFacade.getVaccineCenterByDistrict(district, state);
        return vaccineCenterByDistrict.parallelStream().filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(45)) &&
                center.getAvailableCapacity().longValue() != 0).collect(Collectors.toList());
    }

    public List<Center> getActiveVaccinationFor18Plus(String state, String district) {
        final List<Center> vaccineCenterByDistrict = cdnFacade.getVaccineCenterByDistrict(district, state);
        return vaccineCenterByDistrict.parallelStream().filter(center -> center.getMinAgeLimit().equals(Integer.valueOf(18)) &&
                center.getAvailableCapacity().longValue() != 0).collect(Collectors.toList());
    }

    public List<CenterByDistrict> getActiveVaccinationForCovishield(Subscriber subscriber) {
        final List<CenterByDistrict> calenderOfCenterFor45Plus = getCalenderOfCenter(subscriber);
        calenderOfCenterFor45Plus.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVISHIELD.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });
        return calenderOfCenterFor45Plus;
    }

    public List<CenterByDistrict> getActiveVaccinationForCovaxin(Subscriber subscriber) {

        final List<CenterByDistrict> calenderOfCenter = getCalenderOfCenter(subscriber);
        calenderOfCenter.parallelStream().forEach(centerByDistrict ->
        {
            final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                    .filter(center -> center.getVaccine().equalsIgnoreCase(VACCINE.COVAXIN.name()))
                    .collect(Collectors.toList());
            centerByDistrict.setSessionPerCenter(sessionPerCenters);
        });
        return calenderOfCenter;
    }

    /*public List<CenterByDistrict> getCalenderOfCenterFor18Plus(String state, String district){
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
    }*/

    public List<CenterByDistrict> getCalenderOfCenter(Subscriber subscriber) {
        final List<CenterByDistrict> calenderByVaccinationCenter;
        if (Objects.nonNull(subscriber.getPincode())) {
            calenderByVaccinationCenter = cdnFacade.getCalenderByPin(subscriber.getPincode());
        } else {
            calenderByVaccinationCenter = cdnFacade.getCalenderByVaccinationCenter(subscriber.getDistrict(), subscriber.getState());
        }

        calenderByVaccinationCenter.stream().parallel()
                .forEach(centerByDistrict -> {

                    final List<SessionPerCenter> sessionPerCenters = centerByDistrict.getSessionPerCenter().stream()
                            .filter(center -> center.getMinAgeLimit().equals(subscriber.getAge()) &&
                                    center.getAvailableCapacity().longValue() > 1)
                            .filter(center -> {
                                if (Objects.nonNull(subscriber.getDoseNumber()) && subscriber.getDoseNumber() > 0) {
                                    if (subscriber.getDoseNumber() == 1) {
                                        return center.getAvailableCapacityDose1() > 0;
                                    } else {
                                        return center.getAvailableCapacityDose2() > 0;
                                    }
                                }
                                return true;
                            })
                            .collect(Collectors.toList());
                    centerByDistrict.setSessionPerCenter(sessionPerCenters);
                });
        return calenderByVaccinationCenter.parallelStream().filter(centerByDistrict -> !centerByDistrict.getSessionPerCenter().isEmpty()).collect(Collectors.toList());
    }

}
