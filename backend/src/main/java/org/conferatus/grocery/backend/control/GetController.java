package org.conferatus.timetable.backend.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class GetController {

    @GetMapping("/example")
    public ResponseEntity<String> example() {
        return ResponseEntity.ok("Hello World");
    }

}
