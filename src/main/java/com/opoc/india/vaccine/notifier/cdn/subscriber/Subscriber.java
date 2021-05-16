package com.opoc.india.vaccine.notifier.cdn.subscriber;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@AllArgsConstructor
@Builder
@Getter
@ToString
@NoArgsConstructor
@JsonDeserialize(builder = Subscriber.SubscriberBuilder.class)
@EqualsAndHashCode
public class Subscriber {

    @Id
    @SequenceGenerator(sequenceName = "seq_notifier", name = "seq_notifier", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notifier")
    private Integer id;

    private String email;
    private int age;
    private String state;
    private String district;
    private String vaccine;
    private Integer pincode;

}
