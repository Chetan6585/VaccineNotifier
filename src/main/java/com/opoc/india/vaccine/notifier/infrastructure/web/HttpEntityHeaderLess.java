package com.opoc.india.vaccine.notifier.infrastructure.web;

import org.springframework.http.HttpEntity;

public final class HttpEntityHeaderLess {

    private HttpEntityHeaderLess() {

    }

    public static HttpEntity<?> withNoHeader() {
        return new HttpEntity<>(null);
    }

}
