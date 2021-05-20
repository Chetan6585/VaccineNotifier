package com.opoc.india.vaccine.notifier.cdn.resource;

import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.service.CdnService;
import com.opoc.india.vaccine.notifier.cdn.service.VaccineFinderTask;
import com.opoc.india.vaccine.notifier.cdn.subscriber.Subscriber;
import com.opoc.india.vaccine.notifier.cdn.subscriber.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vaccine")
@AllArgsConstructor
@Slf4j
public class VaccineFinderResource {

    private TaskExecutor taskExecutor;
    private CdnService cdnService;
    private SubscriberRepository subscriberRepository;

    @GetMapping(path = "/center")
    public ResponseEntity<?> getVaccineCenter(@RequestParam("state") final String stateName,
                                           @RequestParam("district") final String districtName,
                                           @RequestParam(value = "age", required = true) final Integer age,
                                           @RequestParam(value = "email", required = true) final String email,
                                           @RequestParam(value = "vaccine", required = false) final String vaccine) {
        //log.info("Thread Started for:{} {} {}  {} ",stateName, districtName, age, email);
     /*   final VaccineFinderTask vaccineFinderTask = VaccineFinderTask.builder()
                .state(stateName)
                .district(districtName)
                .cdnService(cdnService)
                .age(age)
                .vaccine(vaccine == null ? VACCINE.NO_VACCINE : VACCINE.valueOf(vaccine))
                .recipentEmailId(email)
                .build();
        taskExecutor.execute(vaccineFinderTask);*/
        final Subscriber subscriber = subscriberRepository.save(Subscriber.builder()
                .age(age)
                .district(districtName)
                .email(email)
                .state(stateName)
                .vaccine(vaccine)
                .build());
        return new ResponseEntity<Subscriber>(subscriber, HttpStatus.OK);
    }
}
