package oldboy.entity;

import lombok.*;
import oldboy.entity.accessory.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "chatName")
@ToString(exclude = "userChats")
@Builder
@Entity
@Table(name = "chats")
public class Chat implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name", nullable = false, unique = true)
    private String chatName;

    @Builder.Default
    @OneToMany(mappedBy = "chat")
    private Set<UserChat> userChats = new HashSet<>();

    @Override
    public void setId(Long id) {
        this.chatId = id;
    }

    @Override
    public Long getId() {
        return chatId;
    }
}