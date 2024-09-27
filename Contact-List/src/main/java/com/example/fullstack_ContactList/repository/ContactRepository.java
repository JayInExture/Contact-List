package com.example.fullstack_ContactList.repository;

import com.example.fullstack_ContactList.domain.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, Long> {

    Optional<Contacts> findById(Long Id);
}
