# Systemd Notifier for Java

[![Release](https://jitpack.io/v/kaplaukhd/systemd-notifier.svg)](https://jitpack.io/#kaplaukhd/systemd-notifier)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**[English](#english)** | **[Русский](#russian)**

---

<a name="english"></a>
## 🇺🇸 English

**Systemd Notifier** is a modern, lightweight, and efficient Java library for notifying `systemd` about your application's status.

Unlike older libraries that spawn external processes (calling `systemd-notify` CLI via `ProcessBuilder`), this library uses **JNA (Java Native Access)** to make direct native calls to `libsystemd` or write directly to the Unix Domain Socket. This approach is significantly faster, resource-efficient, and safer for high-frequency calls like Watchdogs.

### Features
*   🚀 **High Performance:** Uses JNA, no overhead from creating OS processes.
*   🛠 **Full Support:** Supports `READY`, `STOPPING`, `RELOADING`, `WATCHDOG`, and arbitrary `STATUS` text.
*   🛡 **Safe:** Gracefully degrades on non-systemd environments (Windows, local dev) without throwing exceptions.
*   ☕ **Modern:** Built for Java 17+.

### Installation

The easiest way to install is via **JitPack**.

**Step 1.** Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
**Step 2.** Add the dependency:
```xml
<dependency>
    <groupId>com.github.kaplaukhd</groupId>
    <artifactId>systemd-notifier</artifactId>
    <version>1.0.0</version> <!-- Use the latest release tag -->
</dependency>
```

## Usage
**Basic Usage** (Plain Java)

```java
import com.kaplaukhd.systemd.SystemdNotifier;

public class MyApp {
    public static void main(String[] args) {
        // 1. Notify systemd that app is starting
        SystemdNotifier.status("Initializing modules...");

        // ... initialization logic ...

        // 2. Notify systemd that app is ready to serve traffic
        SystemdNotifier.ready();
        SystemdNotifier.status("Running and waiting for requests");
        
        // 3. (Optional) Watchdog loop
        new Thread(() -> {
            while (true) {
                SystemdNotifier.watchdog();
                try { Thread.sleep(5000); } catch (Exception e) {}
            }
        }).start();
    }
}
```

**Spring Boot Integration**

Define a bean or component to handle lifecycle events:
```java
@Component
public class SystemdLifecycle implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        SystemdNotifier.ready();
        SystemdNotifier.status("Spring Boot application started");
    }

    @PreDestroy
    public void onExit() {
        SystemdNotifier.stopping();
    }
}
```

## Requirements
Java 17 or higher.

Linux environment with systemd (for actual functionality). Works safely on Windows/Mac (no-op).


<a name="russian"></a>
## 🇷🇺 Русский
**Systemd Notifier** — это современная, легковесная и эффективная Java библиотека для интеграции с `systemd`.
Эта библиотека использует **JNA (Java Native Access)**. Это позволяет делать прямые нативные вызовы к libsystemd или писать напрямую в Unix сокет. Такой подход значительно быстрее, потребляет меньше ресурсов и идеально подходит для частых вызовов, таких как Watchdog (heartbeat).

### Возможности
* 🚀 **Высокая производительность**: Использует JNA, никаких накладных расходов на создание процессов ОС.
* 🛠 **Полная поддержка**: Поддерживает статусы `READY`, `STOPPING`, `RELOADING`, `WATCHDOG`, а также произвольный текст `STATUS`.
* 🛡 **Безопасность**: При запуске без systemd (например, на Windows или при локальной разработке) библиотека просто игнорирует вызовы, не выбрасывая ошибок.
* ☕ **Современность**: Собрана под Java 17+.

### Установка

Самый простой способ — использовать **JitPack**.

**Шаг 1.** Добавьте репозиторий JitPack в ваш `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

**Шаг 2.** Добавьте зависимость:

```xml
<dependency>
    <groupId>com.github.kaplaukhd</groupId>
    <artifactId>systemd-notifier</artifactId>
    <version>1.0.0</version> <!-- Use the latest release tag -->
</dependency>
```

## Использование
**Базовое использование** (Plain Java)

```java
import com.kaplaukhd.systemd.SystemdNotifier;

public class MyApp {
    public static void main(String[] args) {
        // 1. Notify systemd that app is starting
        SystemdNotifier.status("Initializing modules...");

        // ... initialization logic ...

        // 2. Notify systemd that app is ready to serve traffic
        SystemdNotifier.ready();
        SystemdNotifier.status("Running and waiting for requests");
        
        // 3. (Optional) Watchdog loop
        new Thread(() -> {
            while (true) {
                SystemdNotifier.watchdog();
                try { Thread.sleep(5000); } catch (Exception e) {}
            }
        }).start();
    }
}
```

**Интеграция со Spring Boot**

Создайте компонент для обработки событий жизненного цикла:

```java
@Component
public class SystemdLifecycle implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        SystemdNotifier.ready();
        SystemdNotifier.status("Spring Boot application started");
    }

    @PreDestroy
    public void onExit() {
        SystemdNotifier.stopping();
    }
}
```

## Требования
**Java 17** или выше.
Linux с systemd (для работы уведомлений). На Windows/Mac библиотека работает в "тихом" режиме (no-op), не ломая приложение.