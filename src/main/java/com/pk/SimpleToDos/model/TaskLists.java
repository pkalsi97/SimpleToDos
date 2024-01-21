package com.pk.SimpleToDos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.type.NumericBooleanConverter;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "task_lists")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLists extends Auditable {

    @Id
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted", insertable = false)
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean isDeleted;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "taskList")
    private List<Tasks> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}