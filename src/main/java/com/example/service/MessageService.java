package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * confirms message validity, i.e. total characters greater than 1, less than 255
     * 
     * @param messageText
     * @return boolean
     * @throws IllegalArgumentException
     */
    public boolean checkForValidMessage(String messageText) throws IllegalArgumentException {
        // check if message is not blank and over 255 characters, return 400 if not
        if(messageText.isEmpty() || messageText.length() > 255){
            throw new IllegalArgumentException("Message must not be empty.\nMessage text must be within 255 characters.");
        }
        return true;
    }

    /**
     * confirms a message exists in database using postedBy id
     * 
     * @param postedBy
     * @return boolean
     * @throws IllegalArgumentException
     */
    public boolean checkForValidId(int postedBy) throws IllegalArgumentException {
        // check if user exist and return 400 if not 
        if(accountRepository.findById(postedBy).isEmpty()){
            throw new IllegalArgumentException("User does not exist.");
        }
        return true;
    }

    /**
     * sends a new message to be persisted in database
     * 
     * @param message
     * @return Message obj
     */
    public Message createMessage(Message message){
        // helper functions to check message validity and if id (for user) exists, then saves using JpaRepository's built-in save method 
        if(checkForValidMessage(message.getMessageText()) && checkForValidId(message.getPostedBy())){
            return messageRepository.save(message);
        }
        return null;
    }

    /**
     * retrieves all persisted messages from database
     * 
     * @param message
     * @return List<Messsage>
     */
    public List<Message> getMessageList(){
        return (List<Message>) messageRepository.findAll();
    }

    /**
     * retrieves persisted message from database or returns null
     * 
     * Note: intentionally avoiding .orElse(), .get() to safely unwrap JPARepository returned Optional<Message>
     * This way we can use Optional's built-in methods later
     * 
     * @param messageId
     * @return Optional<Message>
     */
    public Optional<Message> getMessageByMessageId(int messageId){
        return messageRepository.findById(messageId);          
    }

    /**
     * deletes a persisted message from database
     * 
     * @param messageId
     * @return boolean
     */
    public boolean deleteMessageByMessageId(int messageId){
        boolean result = false;

        // if message is present, deletes is and set result to true
        if(getMessageByMessageId(messageId).isPresent()){
            messageRepository.deleteById(messageId);
            result = true;
        }
        return result;
    }

    /**
     * updates messageText of a message in the database
     * 
     * @param messageId
     * @param messageText
     * @return int
     * @throws IllegalArgumentException
     */
    @Transactional
    public int updateMessageByMessageId(int messageId, String messageText) throws IllegalArgumentException {
        // get persisted message from database
        Optional<Message> message = getMessageByMessageId(messageId);

        // check messages for valid message, updates and saves to database
        if (message.isPresent() && checkForValidMessage(messageText)){
            Message unwrappedMessage = message.get();
            int rowsAffected = messageRepository.updateMessageByMessageId(messageId, unwrappedMessage.getMessageText()); // alternatively could set new text to uwrappedMessage and use built-in JPA Repsoitory .save() method
            return rowsAffected;
        }
        throw new IllegalArgumentException("Invalid messageId.");
    }

    /**
     * retrieves messages of a particular user from database
     * 
     * @param accountId
     * @return List<Message>
     */
    public List<Message> getAllMessagesPostedByUser(int accountId){
        List<Message> messages = messageRepository.findMessagesByPostedBy(accountId);
        return messages;
    }
}
