
package com.nicolas.app_academy.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.ExerciseLog;
import com.nicolas.app_academy.entities.User;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByUser(User user);

    List<ExerciseLog> findByExercise(Exercise exercise);

    List<ExerciseLog> findByUserAndStatus(User user, String status);

    Long countByUserIdAndStatus(Long userId, String status);

    @Query("SELECT COUNT(e) FROM ExerciseLog e WHERE e.user.id = :userId AND e.status = 'Concluido' " +
            "AND e.performedAt >= :startDate AND e.performedAt <= :endDate")
    Long countConcluidoExercisesByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
