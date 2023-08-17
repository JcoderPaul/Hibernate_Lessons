package oldboy.entity;

import lombok.*;
import oldboy.entity.accessory.AuditableEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "users_chats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "UserChat")
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
