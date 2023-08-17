package oldboy.entity;
/*
Данный класс (сущность) наследует от класса AuditableEntity, а поскольку,
тот в свою очередь аннотирован как @EntityListeners(AuditListener.class),
то текущая сущность заполучила в свой арсенал слушателя процессов, в
нашем случае некий аудит жизненного цикла.

Для того чтобы избежать ошибок вносим изменения в таблицу БД:
- DOC/SQL_Scripts/Make_tables.sql;
- DOC/SQL_Scripts/Insert_data_to_tables.sql;
*/
import lombok.*;
import oldboy.entity.accessory.AuditableEntity;
import oldboy.listener.UserChatListener;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "users_chats", schema = "part_four_base")
/*
Добавим индивидуальный слушатель для данной сущности,
он будет слушать и считать, сколько человек добавились
в чат и сколько ушло из него.
*/
@EntityListeners(UserChatListener.class)
public class UserChat extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_chats_id")
    private Long usersChatsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public void setUser(User user) {
        this.user = user;
        this.user.getUserChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUserChats().add(this);
    }

    @Override
    public void setId(Long id) {
        this.usersChatsId = id;
    }

    @Override
    public Long getId() {
        return usersChatsId;
    }
}
