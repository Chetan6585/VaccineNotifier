package com.opoc.india.vaccine.notifier.cdn;

import com.opoc.india.vaccine.notifier.cdn.center.CalendarByDistrictDTO;
import com.opoc.india.vaccine.notifier.cdn.center.Center;
import com.opoc.india.vaccine.notifier.cdn.center.CenterByDistrict;
import com.opoc.india.vaccine.notifier.cdn.center.Sessions;
import com.opoc.india.vaccine.notifier.cdn.state.District;
import com.opoc.india.vaccine.notifier.cdn.state.Districts;
import com.opoc.india.vaccine.notifier.cdn.state.State;
import com.opoc.india.vaccine.notifier.cdn.state.States;
import com.opoc.india.vaccine.notifier.infrastructure.settings.VaccineNotifierPropertyConfiguration;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CdnFacadeImpl implements CdnFacade {

    private static ConcurrentHashMap<String, List<District>> stringDistrictConcurrentHashMap = new ConcurrentHashMap<>();
    private RestTemplate restTemplate;
    private VaccineNotifierPropertyConfiguration vaccineNotifierPropertyConfiguration;

    public CdnFacadeImpl(RestTemplate restTemplate,
                         VaccineNotifierPropertyConfiguration vaccineNotifierPropertyConfiguration) {
        this.restTemplate = restTemplate;
        this.vaccineNotifierPropertyConfiguration = vaccineNotifierPropertyConfiguration;
    }

    @Override
    public List<Center> getVaccineCenterByDistrict(String districtName, String stateName) {
        final District district = getDistrictByState(districtName, stateName);
        String vaccinationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
        List<Center> centers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ResponseEntity<Sessions> response = getVaccinationCenter(district, vaccinationDate);
            if (!response.getBody().getCenterList().isEmpty())
                centers.addAll(response.getBody().getCenterList());

            vaccinationDate = LocalDate.now().plusDays(i + 1).format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
        }

        //     log.info(String.valueOf(response.getBody()));
        return centers;
    }

    @Override
    @Synchronized
    public List<CenterByDistrict> getCalenderByVaccinationCenter(String districtName, String stateName) {
        final District district = getDistrictByState(districtName, stateName);
        String vaccinationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));

        return getCalenderOfVaccinationCenter(district, vaccinationDate).getBody().getCenters();
    }

    @Override
    @Synchronized
    public List<CenterByDistrict> getCalenderByPin(Integer pinCode) {
        String vaccinationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));

        final URI uri = UriComponentsBuilder.fromUriString(vaccineNotifierPropertyConfiguration.getCalenderByVaccineCenterByPinUrl())
                .queryParam("pincode", pinCode)
                .queryParam("date", vaccinationDate)
                .build().toUri();
        return fetchCalendarByDistrictDTOResponseEntity(uri).getBody().getCenters();
    }

    @Override
    public List<State> getStates() {
        final URI uri = UriComponentsBuilder.fromUriString(vaccineNotifierPropertyConfiguration.getStatesUrl()).
                buildAndExpand().toUri();
        HttpEntity<String> entity = getHttpEntityHeaders();

        ResponseEntity<States> response = getRestTemplate().exchange(uri, HttpMethod.GET,
                entity, new ParameterizedTypeReference<States>() {
                });
        //     log.info(String.valueOf(response.getBody()));
        return response.getBody().getStates();
    }

    private ResponseEntity<Sessions> getVaccinationCenter(District district, String vaccinationDate) {
        final URI uri = UriComponentsBuilder.fromUriString(vaccineNotifierPropertyConfiguration.getVaccineCenterByDistrictUrl())
                .queryParam("district_id", district.getDistrictId())
                .queryParam("date", vaccinationDate)
                .build().toUri();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        HttpEntity<String> entity = getHttpEntityHeaders();

        return getRestTemplate(requestFactory).exchange(uri, HttpMethod.GET,
                entity, new ParameterizedTypeReference<Sessions>() {
                });
    }


    private ResponseEntity<CalendarByDistrictDTO> getCalenderOfVaccinationCenter(District district, String vaccinationDate) {
        final URI uri = UriComponentsBuilder.fromUriString(vaccineNotifierPropertyConfiguration.getCalenderByVaccineCenterUrl())
                .queryParam("district_id", district.getDistrictId())
                .queryParam("date", vaccinationDate)
                .build().toUri();
        return fetchCalendarByDistrictDTOResponseEntity(uri);
    }

    private ResponseEntity<CalendarByDistrictDTO> fetchCalendarByDistrictDTOResponseEntity(URI uri) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        HttpEntity<String> entity = getHttpEntityHeaders();

        try {
            ResponseEntity<CalendarByDistrictDTO> response = getRestTemplate().exchange(uri, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<CalendarByDistrictDTO>() {
                    });
            return response;
        } catch (Exception e) {
            log.error(uri.toString());
            throw e;
        }
    }

    private HttpEntity<String> getHttpEntityHeaders() {
        HttpHeaders headers = new HttpHeaders();
        //    headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en_US");
        //  headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add("authority", "cdn-api.co-vin.in");
        headers.add("pragma", "no-cache");
        headers.add("cache-control", "no-cache");
        headers.add("scheme","https");
        headers.add("sec-ch-ua", "^^");
        headers.add("accept", "application/json, text/plain, */*");
        headers.add("accept-encoding","gzip, deflate, br");
        headers.add("cache-control","max-age=0");
        headers.add("dnt", "1");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");
        headers.add("origin", "https://selfregistration.cowin.gov.in");
        headers.add("sec-fetch-site", "cross-site");
        headers.add("sec-fetch-mode", "cors");
        headers.add("sec-fetch-dest", "empty");
        headers.add("referer", "https://selfregistration.cowin.gov.in/");
        headers.add("accept-language", "en,en-GB;q=0.9,en-US;q=0.8,hi;q=0.7,fr;q=0.6,mr;q=0.5");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return entity;
    }

    @Override
    public List<District> getDistrictByState(String state) {
        final Optional<State> optionalState = getStates().parallelStream().filter(state1 -> state1.getStateName().equalsIgnoreCase(state)).findFirst();
        if (!optionalState.isPresent()) {
            return Collections.emptyList();
        }
        final String uri = vaccineNotifierPropertyConfiguration.getDistrictUrl() + optionalState.get().getStateId();
        HttpEntity<String> entity = getHttpEntityHeaders();

        ResponseEntity<Districts> response = getRestTemplate().exchange(uri, HttpMethod.GET,
                entity, new ParameterizedTypeReference<Districts>() {
                });
        //       log.info(String.valueOf(response.getBody()));
        return response.getBody().getDistricts();
    }

    private District getDistrictByState(String district, String state) {
        final List<District> districtByState;
        if (stringDistrictConcurrentHashMap.containsKey(state)) {
            districtByState = stringDistrictConcurrentHashMap.get(state);
        } else {
            districtByState = getDistrictByState(state);
            stringDistrictConcurrentHashMap.put(state, districtByState);
        }
        final Optional<District> optionalDistrict = districtByState.parallelStream().filter(district1 -> district1.getDistrictName().equalsIgnoreCase(district)).findFirst();
        return optionalDistrict.get();
    }

    private RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    private RestTemplate getRestTemplate(HttpComponentsClientHttpRequestFactory requestFactory) {
        return new RestTemplate(requestFactory);
    }


}
