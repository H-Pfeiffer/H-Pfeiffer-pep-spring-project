package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping("/")
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> registerHandler(@RequestBody Account account) throws ConstraintViolationException, IllegalArgumentException{
        Account newAccount = accountService.register(account);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginHandler(@RequestBody Account account){
        Account loggedInAccount = accountService.login(account);
        return ResponseEntity.ok(loggedInAccount);
    }

    @GetMapping("accounts")
    public List<Account> getAccountsHandler(){
        return accountService.getAccountList();
    }

    @PostMapping("messages")
    public ResponseEntity<Message> postMessageHandler(@RequestBody Message message){
        Message newMessage = messageService.createMessage(message);
        return ResponseEntity.ok(newMessage);
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getMessageListHandler(){
        List<Message> messages = messageService.getMessageList();
        return ResponseEntity.ok(messages);
    }

    // PathVariable in endpoint and parameter must match, i.e. messageId
    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageByMessageIdHandler(@PathVariable int messageId){
        Optional<Message> message = messageService.getMessageByMessageId(messageId);
        Message result = message.orElse(null);
        return ResponseEntity.ok(result);
    }

    // Leveraging Object as return to allow flexibility for different values 
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Object> deleteMessageByMessageIdHandler(@PathVariable int messageId){
        if(messageService.deleteMessageByMessageId(messageId)) return ResponseEntity.ok(1);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByMessageIdHandler(@PathVariable int messageId, @RequestBody Message message){    // note: @RequestBody Message is only guaranteed to have messageText
        messageService.updateMessageByMessageId(messageId, message.getMessageText()); 
        return ResponseEntity.ok(1);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesPostedByUserHandler(@PathVariable int accountId){
        List<Message> messages = messageService.getAllMessagesPostedByUser(accountId);
        return ResponseEntity.ok(messages);
    }
}
