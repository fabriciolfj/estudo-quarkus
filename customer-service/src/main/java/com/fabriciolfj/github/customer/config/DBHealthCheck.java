package com.fabriciolfj.github.customer.config;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.resource.spi.ConfigProperty;
import java.io.IOException;
import java.net.Socket;

@ApplicationScoped
public class DBHealthCheck implements HealthCheck {

    @ConfigProperty(defaultValue = "db.host")
    String host;

    @ConfigProperty(defaultValue = "db.port")
    Integer port;

    @Override
    public HealthCheckResponse call() {
        final var responseBuilder = HealthCheckResponse.named("Database connection health check");

        try {
            serverListening(host, port);
            return responseBuilder.build();
        } catch (IOException e) {
            return responseBuilder.down()
                    .withData("error", e.getMessage())
                    .build();
        }
    }

    private void serverListening(final String host, final int port) throws IOException {
        var s = new Socket(host, port);
        s.close();
    }
}
