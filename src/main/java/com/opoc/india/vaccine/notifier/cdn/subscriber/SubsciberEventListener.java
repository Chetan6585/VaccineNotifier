package com.opoc.india.vaccine.notifier.cdn.subscriber;

import com.opoc.india.vaccine.notifier.cdn.VACCINE;
import com.opoc.india.vaccine.notifier.cdn.service.CdnService;
import com.opoc.india.vaccine.notifier.cdn.service.VaccineFinderTask;
import com.opoc.india.vaccine.notifier.infrastructure.settings.VaccineNotifierPropertyConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class SubsciberEventListener {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Builder.Default
    private List<Subscriber> subscribers = new ArrayList<>();
    @Autowired
    private CdnService cdnService;
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private VaccineNotifierPropertyConfiguration notifierPropertyConfiguration;


    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws InterruptedException {
        while (true) {
            final List<Subscriber> all = subscriberRepository.findAll();
            final List<Subscriber> filteredSubscriber = all.stream().filter(subscriber -> !subscribers.contains(subscriber)).collect(Collectors.toList());
            if (filteredSubscriber.isEmpty()) {
                return;
            }
            subscribers.addAll(filteredSubscriber);
            filteredSubscriber.forEach(subscriber ->
            {
                log.info("Thread Started for:{} {} {}  {} ", subscriber.getState(), subscriber.getDistrict(), subscriber.getAge(), subscriber.getEmail());
                final VaccineFinderTask vaccineFinderTask = VaccineFinderTask.builder()
                        .state(subscriber.getState())
                        .district(subscriber.getDistrict())
                        .cdnService(cdnService)
                        .age(subscriber.getAge())
                        .vaccine(subscriber.getVaccine() == null ? VACCINE.ANY_VACCINE : VACCINE.valueOf(subscriber.getVaccine()))
                        .recipentEmailId(subscriber.getEmail())
                        .notifierPropertyConfiguration(notifierPropertyConfiguration)
                        .build();
                taskExecutor.execute(vaccineFinderTask);
                log.info(vaccineFinderTask.getVaccine().name());
            });

            Thread.sleep(60000);

        }

    }
}
