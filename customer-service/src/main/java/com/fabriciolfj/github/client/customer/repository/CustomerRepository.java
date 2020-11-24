package com.fabriciolfj.github.client.customer.repository;

import com.fabriciolfj.github.client.customer.entity.Customer;
import com.fabriciolfj.github.client.customer.exceptions.CustomerNotfoundException;
import io.quarkus.panache.common.Sort;
import io.quarkus.security.identity.SecurityIdentity;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.vertx.reactivex.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.smallrye.config.ConfigLogging.log;

@Slf4j
@ApplicationScoped
public class CustomerRepository {

    //pegar o usuário logado
    @Inject
    private SecurityIdentity securityIdentity;

    @Inject
    io.vertx.reactivex.core.Vertx vertx;

    @ConfigProperty(name = "file.path")
    String path;

    public CompletionStage<String> readFile() {
        var future = new CompletableFuture<String>();
        long start = System.nanoTime();

        vertx.setTimer(100, l -> {
            long duraton = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            vertx.fileSystem().readFile(path, ar -> {
                if (ar.succeeded()) {
                    var response= ar.result().toString("UTF-8");
                    future.complete(response);
                } else {
                    future.complete("Cannot readthe file: " + ar.cause().getMessage());
                }
            });
        });

        return future;
    }

    //completionstage: é baseado no conceito de etapas, consideradas como múltiplas computação intermediárias.
    public CompletionStage<String> writeFile() {
        var future = new CompletableFuture<String>();
        var sb = new StringBuffer("id,name,surname");
        sb.append(System.lineSeparator());

        final List<Customer> customers = Customer.listAll();
        Observable.fromIterable(customers)
                .map(c -> c.id + "," + c.name + "," + c.surname + System.lineSeparator())
                .subscribe(
                        data -> sb.append(data),
                        error -> log.error(error.toString()),
                        () -> vertx.fileSystem().writeFile(path, Buffer.buffer(sb.toString()), handler -> {
                            if (handler.succeeded()) {
                                future.complete("File written in " + path);
                            } else {
                                log.error("Error while writing in file: " + handler.cause().getMessage());
                            }
                        })
                );

        return future;
    }

    @Timeout(1000) //fallback
    @Fallback(fallbackMethod = "findAllStatic") //fallback
    @Retry(maxRetries = 3, retryOn = {RuntimeException.class, TimeoutException.class}) //fallback
    @CircuitBreaker(failOn = {RuntimeException.class}, successThreshold = 5,requestVolumeThreshold = 4, failureRatio = 0.75, delay = 100) //metrics
    @Counted(description = "Customer list count", absolute = true) //metrics
    @Timed(name = "timeCheck", description = "Tempo para listar os clientes", unit = MetricUnits.MILLISECONDS)
    public List<Customer> findAll() {
        //randomSleep();
        log.info("Connected with user: {}", securityIdentity.getPrincipal().getName());
        possibleFailure();
        return Customer.listAll(Sort.by("id"));
    }

    @Transactional
    public void add(final Customer customer) {
        customer.persist();
    }

    public Customer findById(final Long id) {
        final Customer customer = Customer.findById(id);

        if (customer == null) {
            throw new CustomerNotfoundException("Customer not found. Id: " + id);
        }

        return customer;
    }

    @Transactional
    public void updateCustomer(final Customer customer, final Long id) {
        final Customer customerUpdate = findById(id);
        customerUpdate.name = customer.name;
        customerUpdate.surname = customer.surname;
    }

    @Transactional
    public void deleteCustomer(final Long id) {
        Customer.deleteById(id);
    }

    public List<Customer> findAllStatic() {
        return List.of(Customer.builder()
                .id(99L)
                .name("fallback")
                .surname("fallback")
                .build());
    }

    private void randomSleep() {
        try {
            Thread.sleep(new Random().nextLong());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void possibleFailure() {
        if (new Random().nextFloat() <  0.5f) {
            throw new RuntimeException("Resource failure");
        }
    }
}
