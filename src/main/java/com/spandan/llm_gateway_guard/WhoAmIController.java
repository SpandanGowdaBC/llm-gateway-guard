package com.spandan.llm_gateway_guard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // CORE
@RequestMapping("/api") // CORE
public class WhoAmIController {

    @GetMapping("/whoami") // CORE
    public Map<String, String> whoAmI() {
        String tenantId = TenantContext.getTenantId(); // CORE: read from ThreadLocal
        return Map.of("tenant_id", tenantId); // CORE
    }
}
