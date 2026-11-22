package com.spandan.llm_gateway_guard;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>(); // CORE

    private TenantContext() {}

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId); // CORE
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get(); // CORE
    }

    public static void clear() {
        CURRENT_TENANT.remove(); // CORE
    }
}
