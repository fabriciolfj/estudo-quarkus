package com.fabriciolfj.github.client.customer.integration;

import com.fabriciolfj.github.client.customer.entity.Company;
import com.fabriciolfj.github.client.customer.entity.Operation;
import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.JsonbBuilder;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class QuoteGenerator {

    private Random random = new Random();

    @Outgoing("stock-quote")
    public Flowable<String> generate() {
        return Flowable.interval(2, TimeUnit.SECONDS)
                .map(tick -> generateOrder(random.nextInt(2), random.nextInt(5), random.nextInt(100)));
    }

    private String generateOrder(int type, int company, int amount) {
        var jsonb = JsonbBuilder.create();
        var operation = new Operation(type, Company.values()[company], amount);
        return jsonb.toJson(operation);
    }
}
