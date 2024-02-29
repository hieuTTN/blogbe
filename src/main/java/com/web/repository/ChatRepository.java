package com.web.repository;

import com.web.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c where (c.sender.id = ?1 and c.receiver.id = ?2) or (c.sender.id = ?2 and c.receiver.id = ?1)")
    public List<Chat> findByUser(Long user1, Long user2);
}
