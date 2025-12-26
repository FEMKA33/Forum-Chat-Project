package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "message_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "message_id"}))
public class MessageLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private Message message;

    @Column(name = "is_like")
    private boolean liked;
}
