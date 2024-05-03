package com.batch.entities;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "last_name")
    private String surname;
    private int age;
    @Column(name = "date_act")
    private String dateAct;
}
