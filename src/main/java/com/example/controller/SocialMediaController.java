package com.example.controller;

// import com.azul.crs.client.Response;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import javax.naming.AuthenticationException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
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
}
