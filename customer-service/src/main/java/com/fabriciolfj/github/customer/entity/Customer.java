package com.fabriciolfj.github.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Data
//@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "Customers.findAll",
            query = "Select c FROM Customer c ORDER By c.id",
            hints = @QueryHint(name = "org.hibernate.cacheable", value = "true")) //o query hint, fazo uso no cache quando habilitado
public class Customer {

    @Id
    @SequenceGenerator(
            name = "customerSequence",
            sequenceName = "customerId_seq",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSequence")
    private Long id;

    @Column(length = 40)
    private String name;

    @Column(length = 40)
    private String surname;

    @OneToMany(mappedBy = "customer")
    @JsonbTransient
    public List<Orders> orders;
}
