package com.javadoterr.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javadoterr.api.entity.User;

@Repository
public interface UserRepositoy extends JpaRepository<User, Integer>{

}
