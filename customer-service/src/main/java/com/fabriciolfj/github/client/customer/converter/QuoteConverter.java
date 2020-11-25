package com.fabriciolfj.github.client.customer.converter;

import com.fabriciolfj.github.client.customer.entity.Company;
import com.fabriciolfj.github.client.customer.entity.Operation;
import com.fabriciolfj.github.client.customer.entity.Quote;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.JsonbBuilder;
import java.util.HashMap;
import java.util.Random;

@ApplicationScoped
public class QuoteConverter {

    HashMap<String, Double> quotes;
    private Random random = new Random();

    @PostConstruct
    public void init() {
        quotes = new HashMap<>();
        for (Company company: Company.values()) {
            quotes.put(company.name(), new Double(random.nextInt(100) + 50));
        }
    }

    @Incoming("stocks")
    @Outgoing("in-memory-stream")
    @Broadcast
    public String newQuote(final String quoteJson) {
        var jsonb = JsonbBuilder.create();
        var operation = jsonb.fromJson(quoteJson, Operation.class);
        var currentQuote = quotes.get(operation.getCompany().name());
        var change = (operation.getAmount() / 25);
        double newQuote;

        if (operation.getType() == Operation.BUY) {
            newQuote = currentQuote + change;
        } else {
            newQuote = currentQuote - change;
        }

        quotes.replace(operation.getCompany().name(), newQuote);
        var quote = new Quote(operation.getCompany().name(), newQuote);

        return jsonb.toJson(quote);
    }
}
