package org.conferatus.timetable.backend.control;

import io.micrometer.core.instrument.MeterRegistry;
import org.conferatus.timetable.backend.services.SomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.LongAdder;

@RestController
@RequestMapping("/api/ping")
public class PingController {
    private final MeterRegistry meterRegistry;
    private final LongAdder counter;
    private final SomeService someService;

    public PingController(MeterRegistry meterRegistry, SomeService someService) {
        this.meterRegistry = meterRegistry;
        counter = new LongAdder();
        meterRegistry.gauge("pingCounter", counter);
        this.someService = someService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        counter.increment();
        return ResponseEntity.ok("pong");

    }
}
