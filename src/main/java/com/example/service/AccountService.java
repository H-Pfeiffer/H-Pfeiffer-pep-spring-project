package com.example.service;

import java.util.List;
import java.util.Objects;

// import com.example.exception.ExceptionAndErrorController; // Note: with @RestControllerAdvice annotation, Spring Boot automatically handles this at runtime
import com.example.exception.InvalidCredentialsException;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * retrieves all accounts from database
     * 
     * @param none
     * @return List<Account>
     */
    public List<Account> getAccountList(){ return (List<Account>) accountRepository.findAll(); } // added for testing purposes 

    public boolean checkForValidUsername(String accountName) throws ConstraintViolationException {
        // username must not be empty string and must not be unique (doesn't already exist currently in database)
        if(accountName.isEmpty() || accountRepository.findByUsername(accountName).isPresent()){
            throw new ConstraintViolationException("Username is invalid.\nUsername must not be blank and must be unique", null, null);
        }
        return true;
    }

    /**
     * verifies if a password is valid
     * 
     * @param password
     * @return boolean
     * @throws IllegalArgumentException
     */
    public boolean checkForValidPassword(String password) throws IllegalArgumentException{
        // to be valid, paswords must be at least 4 characters long
        if(password.length() < 4){
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        return true;
    }

    /**
     * saves a new account that persists in the database
     * 
     * @param newAccount
     * @return Account
     */
    public Account register(Account newAccount) {
        // invokes helper functions to check if username and password follow business requirements, then saves in database using JpaRepository's built-in method 
        if (checkForValidUsername(newAccount.getUsername()) && checkForValidPassword(newAccount.getPassword())){
            return accountRepository.save(newAccount);
        }
        return null;
    }

    /**
     * retrieves a persisted account from the database
     * 
     * @param username
     * @return Account
     * @throws InvalidCredentialsException
     */
    public Account getAccountByUsername(String username) throws InvalidCredentialsException{
        return accountRepository.findByUsername(username)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
    }

    /**
     * validates login by comparing persisted password against input
     * 
     * Note: For more info on comparing objects and Object.equals(), visit https://www.baeldung.com/java-comparing-objects
     * 
     * @param account
     * @return Account
     * @throws InvalidCredentialsException
     */
    public Account login(Account account) throws InvalidCredentialsException {
        Account dbAccount = getAccountByUsername(account.getUsername());
        // check password is null and two passwords do not match, throw exception
        if(account.getPassword().isEmpty() || !Objects.equals(dbAccount.getPassword(), account.getPassword())){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        return dbAccount;
    }

}
