package com.example.service;

import java.util.ArrayList;
import java.util.List;
import com.example.entity.Message;
import com.example.exception.InvalidCredentialsException;

import org.jboss.logging.Messages;
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

    public List<Message> getMessageList(){
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageByMessageId(int messageId){
        // Note: use .orElse() so JPARepository will unwrap the Optional<Message> as is 'safe' to do so
        return messageRepository.findById(messageId).orElse(null);
    }

    public boolean deleteMessageByMessageId(int messageId){
        boolean result = false;
        if(messageRepository.findById(messageId).isPresent()){
            messageRepository.deleteById(messageId);
            result = true;
        }
        return result;
    }

    public int updateMessageByMessageId(int messageId, String messageText){
        // find id - if exists - client error
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid messageId."));

        // check that text is not blank and not over 255 characters - client error
        if(messageText.isEmpty() || messageText.length() > 255){
            throw new IllegalArgumentException("Message must not be empty.\nMessage must not have more than 255 characters.");
        }
        // update message text 
        message.setMessageText(messageText);
        messageRepository.save(message); // alternatively could create custom query in messageRespository to return number of rows affected
        return 1;
    }

    public List<Message> getAllMessagesPostedByUser(int accountId){
        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return messages;
    }
}
