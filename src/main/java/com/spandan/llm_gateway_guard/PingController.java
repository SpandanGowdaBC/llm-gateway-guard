package com.spandan.llm_gateway_guard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // CORE
public class PingController {

    @GetMapping("/ping") // CORE
    public String ping() {
        return "gateway is alive"; // CORE
    }
}
