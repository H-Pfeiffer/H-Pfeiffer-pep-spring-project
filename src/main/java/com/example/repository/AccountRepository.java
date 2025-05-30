package com.example.repository;

import java.util.Optional;
import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>  {

    Optional<Account> findByUsername(String username); // Note: returning Optional<Account> allows for use of builtin .orElseThrow() method
}
