package com.codegym.repository;

import com.codegym.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE (c.user.id = :userId OR c.host.id = :userId) AND c.status = 'ACTIVE' ORDER BY c.lastMessageAt DESC")
    List<Conversation> findByUserIdOrderByLastMessageAtDesc(@Param("userId") Long userId);

    @Query("SELECT c FROM Conversation c WHERE (c.user.id = :userId AND c.host.id = :hostId AND c.house.id = :houseId) OR (c.user.id = :hostId AND c.host.id = :userId AND c.house.id = :houseId)")
    Optional<Conversation> findByUserIdAndHostIdAndHouseId(@Param("userId") Long userId, @Param("hostId") Long hostId, @Param("houseId") Long houseId);

    @Query("SELECT c FROM Conversation c WHERE c.id = :conversationId AND (c.user.id = :userId OR c.host.id = :userId)")
    Optional<Conversation> findByIdAndUserId(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Conversation c WHERE (c.user.id = :userId OR c.host.id = :userId) AND c.status = 'ACTIVE' AND " +
           "(CASE WHEN c.user.id = :userId THEN c.unreadCountUser ELSE c.unreadCountHost END) > 0")
    Long countUnreadConversationsByUserId(@Param("userId") Long userId);
} 