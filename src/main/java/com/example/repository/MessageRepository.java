package com.example.repository;

import java.util.List;
import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * Message repository interacts with the database via Spring Data JPA
 * This repository class uses both derived and custom JPA Query using named parameters
 * 
 * For more info about @modifying, visit https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/Modifying.html
 * For more info about JPA Queries, visit https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 *  
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>  {

    // derived query, Spring Data JPA understands method name and is able to parse without annotations
    List<Message> findMessagesByPostedBy(int accountId);

    @Modifying
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")    // Note: in JPQL, Java class and field names are case-sensitive
    int updateMessageByMessageId(@Param("messageId") int messageId, @Param("messageText") String messageText);
}
