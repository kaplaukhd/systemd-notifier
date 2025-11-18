package com.kphd.systemd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemdNotifier {
    private static final Logger log = LoggerFactory.getLogger(SystemdNotifier.class);
    private static final boolean ENABLED;

    static {
        // Проверяем наличие переменной NOTIFY_SOCKET.
        // Если её нет, значит мы запущены не через systemd (или Type != notify),
        // и нам не нужно ничего делать.

        // Check for the presence of the NOTIFY_SOCKET variable.
        // If it's not there, then we're not running via systemd (or Type != notify),
        // and we don't need to do anything.
        String socketName = System.getenv("NOTIFY_SOCKET");
        ENABLED = (socketName != null && !socketName.isEmpty());
        
        if (ENABLED) {
            log.info("Systemd notification enabled. Socket: {}", socketName);
        } else {
            log.debug("Systemd notification disabled (NOTIFY_SOCKET not found).");
        }
    }

    /**
     * Сообщить systemd, что сервис готов.
     * <br><br>
     * Notify systemd that the service is ready.
     */
    public static void ready() {
        notify(ServiceStatus.READY.getPayload());
    }

    /**
     * Отправить сигнал Watchdog.
     * Используйте это в цикле, если в юните включен WatchdogSec=...
     * <br><br>
     *  Send a Watchdog signal.
     *  Use this in a loop if WatchdogSec= is enabled in the unit.
     */
    public static void watchdog() {
        notify(ServiceStatus.WATCHDOG.getPayload());
    }

    /**
     * Сообщить systemd, что сервис останавливается.
     * <br><br>
     * Tell systemd that the service is stopping.
     */
    public static void stopping() {
        notify(ServiceStatus.STOPPING.getPayload());
    }

    /**
     * Установить текстовый статус, который виден при вызове `systemctl status my-service`.
     * <br><br>
     * Set the text status that is visible when calling `systemctl status my-service`.
     * @param text is arbitrary text (e.g., "Processing job #123")
     */
    public static void status(String text) {
        if (text == null || text.isEmpty()) return;
        notify("STATUS=" + text);
    }
    
    /**
     * Сообщить об ошибке (аналог выхода с кодом ошибки).
     * <br><br>
     * Report an error (similar to exiting with an error code).
     * @param errno error code (standard Linux errno)
     */
    public static void error(int errno) {
        notify("ERRNO=" + errno);
    }

    /**
     * Расширить таймаут запуска (если приложение грузится дольше, чем TimeoutStartSec).
     * <br> <br>
     * Extend the startup timeout (if the app takes longer to load than TimeoutStartSec).
     * @param microseconds - time in microseconds
     */
    public static void extendTimeout(long microseconds) {
        notify("EXTEND_TIMEOUT_USEC=" + microseconds);
    }

    private static void notify(String state) {
        if (!ENABLED) return;

        try {
            int result = LibSystemd.INSTANCE.sd_notify(0, state);
            if (result < 0) {
                log.warn("Failed to notify systemd. Error code: {}", result);
            }
        } catch (UnsatisfiedLinkError e) {
            log.debug("libsystemd not found, skipping notification.");
        } catch (Exception e) {
            log.error("Exception while notifying systemd", e);
        }
    }
}