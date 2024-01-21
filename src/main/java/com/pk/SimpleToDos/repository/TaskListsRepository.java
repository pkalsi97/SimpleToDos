package com.pk.SimpleToDos.repository;

import com.pk.SimpleToDos.model.TaskLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListsRepository extends JpaRepository<TaskLists, UUID> {

    Optional<TaskLists> findByName(String name);

    Optional<TaskLists> findByNameAndUserId(String name, Long userId);

    @Query("SELECT tl FROM TaskLists tl WHERE tl.uuid = :uuid AND tl.user.id = :userId")
    Optional<TaskLists> findByIdAndUserId(@Param("uuid") UUID uuid, @Param("userId") Long userId);

    List<TaskLists> findAllByUserId(Long id);
}
