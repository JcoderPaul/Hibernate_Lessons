package oldboy.lesson_12.MappingEntity.SimpleChat;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
Если мы будем использовать коллекции с Hash,
то он должен формироваться только по имени.
*/
@EqualsAndHashCode(of = "chatName")
/*
При формировании toString
список 'токеров' исключаем
*/
@ToString(exclude = "talkerList")
@Builder
@Entity
@Table(name = "chats", schema = "public")
public class SimpleChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name")
    private String chatName;
    /*
    Что сразу приходит на ум это не использовать в данном
    случае каскадирование, поскольку при удалении одного
    пользователя мы можем одним разом 'грохнуть' все чаты и
    всех пользователей.
    */
    @Builder.Default
    /*
    Будем считать, что 'токеры' более главная сущность, т.е. если мы
    решим добавить 'токера' в чат, мы это будем делать именно через
    'токера' и поэтому параметр (mappedBy = "chatList") помещен сюда
    и имеет именно такой вид, поскольку поле связка в классе Talker
    называется: private List<Chat> chatList.

    А вот основной маппинг мы будем настраивать в классе Talker.
    */
    @ManyToMany(mappedBy = "chatList")
    /*
    Если не указывать данной аннотации, то при сохранении
    сущностей в БД будет важен порядок их добавления.
    */
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Talker> talkerList = new ArrayList<>();
    /*
    Хотя мы решили, что будем добавлять 'токера' в чат именно
    через 'токера' нам никто не мешает добавлять его и через
    сам чат.
    */
    public void setTalker(Talker talker) {
        talkerList.add(talker);
        talker.getChatList().add(this);
    }
}
