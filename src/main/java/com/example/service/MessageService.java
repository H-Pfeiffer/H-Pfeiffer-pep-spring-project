package com.example.service;

import com.example.entity.Message;
import com.example.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) throws InvalidCredentialsException{
        // check if message is not blank and over 255 characters, return 400 if not
        if(message.getMessageText().isEmpty() || message.getMessageText().length() > 255){
            throw new IllegalArgumentException("Message must not be empty.\nMessage text must be within 255 characters.");
        }
        // check if user exist and return 400 if not 
        if(accountRepository.findById(message.getPostedBy()).isEmpty()){
            throw new IllegalArgumentException("User does not exist.");
        }
       return messageRepository.save(message);
    }
}
