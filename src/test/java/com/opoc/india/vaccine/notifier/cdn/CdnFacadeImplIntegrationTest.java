package com.opoc.india.vaccine.notifier.cdn;

import com.opoc.india.vaccine.notifier.AbstractIntegrationTest;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.state.District;
import com.opoc.india.vaccine.notifier.cdn.state.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CdnFacadeImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    CdnFacade cdnFacade;


    @Test
    public void givenStateurl_whenCallingCdnApi_fetchAllStates(){
        final List<State> states = cdnFacade.getStates();

        final Optional<State> maharashtra = states.stream().parallel().filter(state -> state.getStateName().equalsIgnoreCase("Maharashtra")).findFirst();
        Assertions.assertEquals(maharashtra.get().getStateName(), "Maharashtra");
    }

    @Test
    public void givenDistrictUrl_whenCallingCdnApi_fetchAllDistricts(){
        final List<District> district = cdnFacade.getDistrictByState("Maharashtra");

        final Optional<District> maharashtra = district.stream().parallel().filter(state -> state.getDistrictName().equalsIgnoreCase("Akola")).findFirst();
        Assertions.assertEquals(maharashtra.get().getDistrictName(), "Akola");
    }

    @Test
    public void givenVaccinationCenterUri_whenCallingCdnApi_fetchAllCenters(){
        final List<Center> district = cdnFacade.getVaccineCenterByDistrict("Ahmednagar","Maharashtra");

        final Optional<Center> maharashtra = district.stream().parallel().filter(state -> state.getDistrictName().equalsIgnoreCase("Akola")).findFirst();
        Assertions.assertEquals(maharashtra.get().getDistrictName(), "Akola");
    }
}