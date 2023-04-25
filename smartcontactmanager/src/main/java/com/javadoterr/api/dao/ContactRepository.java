package com.javadoterr.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadoterr.api.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

}
