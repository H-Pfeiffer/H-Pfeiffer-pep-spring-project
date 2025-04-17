package com.example.service;

import com.example.exception.ExceptionAndErrorController;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import javax.naming.AuthenticationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private MessageService messageService;

    @Autowired
    public AccountService(AccountRepository accountRepository, MessageService messageService){
        this.accountRepository = accountRepository;
        this.messageService = messageService;
    }

    public Account register(Account newAccount) throws ConstraintViolationException {
        if(accountRepository.findByUsername(newAccount.getUsername()) != null){
            throw new ConstraintViolationException("Username already exists.", null, null);
        }
        return accountRepository.save(newAccount);
    }

}
