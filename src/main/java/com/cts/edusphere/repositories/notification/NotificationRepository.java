package com.cts.edusphere.repositories.notification;

import com.cts.edusphere.modules.notification.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Notification} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations along with
 * custom query methods for retrieving notifications by user and bulk-marking
 * notifications as read.</p>
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  /**
   * Retrieves all notifications belonging to a specific user.
   *
   * <p>Uses nested property navigation on {@code user.id} to match the user association.</p>
   *
   * @param id the {@link UUID} of the user whose notifications are to be fetched
   * @return a {@link List} of {@link Notification} instances associated with the given user;
   *         an empty list if the user has no notifications
   */
  List<Notification> findByUser_Id(UUID id);

  /**
   * Marks all notifications for a specific user as read in a single bulk update.
   *
   * <p>Executes a JPQL {@code UPDATE} statement that sets {@code isRead = true}
   * for every notification where {@code user.id} matches the supplied {@code userId}.</p>
   *
   * <p>Annotated with {@link Modifying} to indicate a state-changing query, and
   * with {@link Query} to define the custom JPQL statement.</p>
   *
   * @param userId the {@link UUID} of the user whose notifications should be marked as read
   * @Query("UPDATE Notification n set n.isRead = true where n.user.id = :userId")
   * @Modifying
   */
  @Modifying
  @Query("UPDATE Notification n set n.isRead = true where n.user.id = :userId")
  void markAllAsReadByUserId(UUID userId);
}
