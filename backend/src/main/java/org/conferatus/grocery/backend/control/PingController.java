package org.conferatus.grocery.backend.control;

import io.micrometer.core.instrument.MeterRegistry;
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

    public PingController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        counter = new LongAdder();
        meterRegistry.gauge("pingCounter", counter);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        counter.increment();
        return ResponseEntity.ok("pong");

    }
}
