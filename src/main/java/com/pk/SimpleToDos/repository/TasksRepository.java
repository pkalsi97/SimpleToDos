package com.pk.SimpleToDos.repository;

import com.pk.SimpleToDos.model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, UUID> {

    @Query("SELECT tl FROM TaskLists tl WHERE tl.uuid = :uuid AND tl.user.id = :userId")
    Optional<Tasks> findByIdAndUserId(@Param("uuid") UUID uuid, @Param("userId") Long userId);

    // New method to find tasks by task list UUID, user's ID, and deletion status
    List<Tasks> findByTaskListUuidAndUserIdAndIsDeleted(UUID taskListUuid, Long userId, boolean isDeleted);
}
