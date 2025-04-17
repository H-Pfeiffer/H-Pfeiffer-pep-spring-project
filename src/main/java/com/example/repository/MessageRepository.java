package com.example.repository;

import java.util.List;
import java.util.Optional;
import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>  {

    List<Message> findByPostedBy(int accountId);
}
