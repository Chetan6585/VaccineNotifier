package com.opoc.india.vaccine.notifier.cdn;

import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.state.District;
import com.opoc.india.vaccine.notifier.cdn.state.State;

import java.util.List;

public interface CdnFacade {

    List<Center> getVaccineCenterByDistrict(String districtName, String stateName);
    List<State> getStates();
    List<District> getDistrictByState(String state);
    List<CenterByDistrict> getCalenderByVaccinationCenter(String districtName, String stateName);
    List<CenterByDistrict> getCalenderByPin(Integer pinCode);


}
