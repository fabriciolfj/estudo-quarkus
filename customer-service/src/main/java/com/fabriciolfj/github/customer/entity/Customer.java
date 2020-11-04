package com.fabriciolfj.github.customer.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

//@Cacheable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "Customers.findAll",
            query = "Select c FROM Customer c ORDER By c.id",
            hints = @QueryHint(name = "org.hibernate.cacheable", value = "true")) //o query hint, fazo uso no cache quando habilitado
public class Customer extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "customerSequence",
            sequenceName = "customerId_seq",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSequence")
    public Long id;

    @Column(length = 40)
    public String name;

    @Column(length = 40)
    public String surname;

    @OneToMany(mappedBy = "customer")
    @JsonbTransient
    public List<Orders> orders;
}
