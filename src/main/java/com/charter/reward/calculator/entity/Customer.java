package com.charter.reward.calculator.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    public Customer(long l, String name1) {
        this.id = l;
        this.name= name1;
    }

    public Customer addToPurchases(Purchase purchase) {
        this.purchases.add(purchase);
        purchase.setCustomer(this);

        return this;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", purchases=" + purchases +
            '}';
    }
}
