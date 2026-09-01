# Socket.IO-client Java (ParadiseDevTeam fork)

Fork of [socketio/socket.io-client-java](https://github.com/socketio/socket.io-client-java) `1.0.2`, maintained at [ParadiseDevTeam/socket.io-client-java](https://github.com/ParadiseDevTeam/socket.io-client-java).

## Changes from upstream 1.0.2 (`feature/event-executor-options`)

- Added `IO.Options#eventExecutor` to route every socket callback (open/message/close/error and all `Socket#on` listeners) through a caller-supplied `Executor`, without having to call `EventThread.setExecutor` yourself.
- Fixed `ExecutionTest` to launch its subprocess via `java` directly instead of `mvn exec:java`.
- Updated dependencies to their latest versions (`org.json`, `junit`, `hamcrest-library`).
- Migrated the build from Maven (`pom.xml`) to Gradle (`build.gradle.kts`).
- Publishes to [repo.prdis.me](https://repo.prdis.me/) (Reposilite) instead of Maven Central.
- Depends on [ParadiseDevTeam/engine.io-client-java](https://github.com/ParadiseDevTeam/engine.io-client-java) `1.0.2-patch.1`, which introduces `EventThread#setExecutor`.

## Usage

### Add the dependency (Gradle)

```kotlin
repositories {
    maven { url = uri("https://repo.prdis.me/releases") }
}

dependencies {
    implementation("io.socket:socket.io-client:1.0.2")
}
```

### Route callbacks through a custom Executor

Useful for frameworks (e.g. Bukkit/Paper plugins) that require callbacks to run on a specific thread rather than the library's internal one:

```java
IO.Options opts = new IO.Options();
opts.eventExecutor = runnable -> Bukkit.getScheduler().runTask(plugin, runnable);
Socket socket = IO.socket(uri, opts);
```

This is equivalent to calling `EventThread.setExecutor(myExecutor)` yourself before connecting. It's a single, process-wide setting shared by every `Socket` instance in the JVM, not scoped to one `Socket` or `Manager`. Leave `eventExecutor` unset (`null`) to keep the default internal executor.

## License

MIT (unchanged from upstream)
