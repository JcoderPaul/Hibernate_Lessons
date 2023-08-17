package oldboy.entity.revtable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.listeners.MyFirstRevisionListener;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
/*
Чтобы Hibernate Envers стал писать данные в нашу новую
таблицу, а не в 'revinfo', которую он создал сам, применяем
аннотацию @RevisionEntity.

!!! САМОЕ ВАЖНОЕ - в каждом конкретном проекте может
существовать только одна RevisionEntity !!!
*/
@RevisionEntity(MyFirstRevisionListener.class)
@Table(name = "revision_records")
public class RevisionRecorder {
    /* ID ревизионной таблицы всегда числовые */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*
    Первая обязательная аннотация, которая должна находиться
    внутри нашей самописной ревизионной сущности - @RevisionNumber
    */
    @RevisionNumber
    @Column(name = "rev_id")
    private Long revId;
    /*
    Вторая обязательная аннотация помечающая поле фиксирующее
    время ревизии, т.е. когда были внесены изменения.
    */
    @RevisionTimestamp
    @Column(name = "rev_time_stamp")
    private Long timeStamp;
    /*
    Теоретически сюда информация должна попадать из нашего
    security модуля, который закрыт для обычного пользователя
    и кроме аутентификации так же фиксирует всю возню обычных
    юзеров.
    */
    @Column(name = "user_name")
    private String userName;
}
