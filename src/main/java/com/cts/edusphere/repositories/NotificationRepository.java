package com.cts.edusphere.repositories;

import com.cts.edusphere.modules.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findByUser_Id(UUID id);

  @Modifying
  @Query("UPDATE Notification n set n.isRead = true where n.user.id = :userId")
  void markAllAsReadByUserId(UUID userId);
}
