package com.bankmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Branch") // table name remains as is
@Data
@NoArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "division", nullable = false)
    private String division;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Override
    public String toString() {
        return "Branch{" + "id=" + id + ", name='" + name + '\'' +
                ", division='" + division + '\'' + ", city='" + city + '\'' + ", street='" + street + '\'' +
                '}';
    }

}
