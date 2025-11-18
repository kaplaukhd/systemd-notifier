package com.kphd.systemd;

public enum ServiceStatus {

    /** Приложение полностью запущено и готово к работе.
     * The application is fully launched and ready to work.
     */
    READY("READY=1"),
    
    /** Приложение начало процедуру остановки.
     * The application has started the shutdown procedure.
     */
    STOPPING("STOPPING=1"),
    
    /**  Приложение перезагружает конфигурацию.
     * The application is reloading the configuration.
     */
    RELOADING("RELOADING=1"),
    
    /** Сигнал Watchdog (сердцебиение). Нужно слать периодически.
     * Watchdog signal (heartbeat). Needs to be sent periodically.
     */
    WATCHDOG("WATCHDOG=1");

    private final String payload;

    ServiceStatus(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}