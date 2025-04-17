package com.example.service;

// import com.example.exception.ExceptionAndErrorController; // Note: @RestControllerAdvice - SB automatically picks up at runtime
import com.example.exception.InvalidCredentialsException;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    // private MessageService messageService;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    //     this.messageService = messageService;
    }

    public Account register(Account newAccount) throws ConstraintViolationException, IllegalArgumentException {
        if(accountRepository.findByUsername(newAccount.getUsername()) != null){
            throw new ConstraintViolationException("Username already exists.", null, null);
        }
        if(newAccount.getUsername().isEmpty() || newAccount.getPassword().length() < 4){
            throw new IllegalArgumentException("Username must not be blank.\nPassword must be at least 4 characters long.");
        }
        return accountRepository.save(newAccount);
    }

    public Account login(Account account) throws InvalidCredentialsException {
        Account dbAccount = accountRepository.findByUsername(account.getUsername()
            .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if(account.getPassword() == null || !dbAccount.getPassword().equals(account.getPassword())){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        return dbAccount;
    }
}
