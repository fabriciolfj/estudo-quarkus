package com.fabriciolfj.github.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o WHERE o.customer.id = :customerId ORDER BY o.item")
public class Orders {

    @Id
    @SequenceGenerator(
            name = "orderSequence",
            sequenceName = "orderId_seq",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequence")
    public Long id;

    @Column(length = 40)
    public String item;

    @Column(length = 40)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonbTransient
    public Customer customer;
}
