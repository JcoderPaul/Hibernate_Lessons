package oldboy.entity;
/*
Сущность (класс) содержащая внутри две callback аннотации,
которые нужны нам, чтобы перехватывать события в жизненном
цикле сущностей и например корректировать (изменять),
фиксировать эти события.

Какие события мы можем перехватывать см. DOC/CallBackInHibernate.txt
*/
import lombok.*;
import oldboy.entity.accessory.AuditableEntity;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
@Table(name = "meeting_rooms", schema = "part_four_base")
public class MeetingRoom extends AuditableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_name", nullable = false, unique = true)
    private String roomName;

    @Column(name = "enter_price")
    private Integer enterPrice;

    /*
    Callback аннотация активирующаяся перед сохранением данных,
    в данном случае мы зашили функционал callback - ов сразу в
    сущность, что не рекомендуется делать.
    */
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(Instant.now());
    }
    /* Callback аннотация активирующаяся перед изменениями данных */
    @PreUpdate
    public void preUpdated() {
        this.setUpdatedAt(Instant.now());
    }

    @Override
    public void setId(Long id) {
        this.roomId = id;
    }

    @Override
    public Long getId() {
        return roomId;
    }
}