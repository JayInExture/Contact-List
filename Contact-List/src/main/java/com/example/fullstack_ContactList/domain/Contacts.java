package com.example.fullstack_ContactList.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Contacts {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, updatable = false)
    Long Id;
    String name;
    String email;
    String title;
    String phone;
    String address;
    String status;
    String dpImageUrl;
}
