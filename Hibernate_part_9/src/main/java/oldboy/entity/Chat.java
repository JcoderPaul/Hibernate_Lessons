package oldboy.entity;

import lombok.*;

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
@Table(name = "chats", schema = "part_four_base")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name", nullable = false, unique = true)
    private String chatName;
    /*
    Добавим поле счетчик участников чата, хотя есть вариант просто узнать
    размер коллекции userChat. Но мы изучаем работу callbacks и listeners
    и тут начинается магия Hibernate и рефлексии. Наш Chat связан с UserChat
    и когда User добавляется в UserChat, в работу включаются callback - и из
    класса в аннотации @EntityListeners(UserChatListener.class).
    */
    @Builder.Default
    private Integer count = 0;

    @Builder.Default
    @OneToMany(mappedBy = "chat")
    private Set<UserChat> userChats = new HashSet<>();
}