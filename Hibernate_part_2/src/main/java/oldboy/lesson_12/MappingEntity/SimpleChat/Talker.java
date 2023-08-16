package oldboy.lesson_12.MappingEntity.SimpleChat;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "chatList")
@ToString(exclude = "chatList")
@Builder
@Entity
@Table(name = "talkers", schema = "public")
public class Talker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talker_id")
    private Long talkerId;

    @Column(name = "talker_name")
    private String talkerName;

    @Column(name = "talker_age")
    private Integer talkerAge;

    @Builder.Default
    @ManyToMany
    /* Аннотация указывает какая таблица в БД будет связывать наши две сущности */
    @JoinTable(
            name = "talkers_chats",
            joinColumns = @JoinColumn(name = "talker_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    /*
    Если не указывать данной аннотации, то при сохранении
    сущностей в БД будет важен порядок их добавления.
    */
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<SimpleChat> chatList = new ArrayList<>();

    /* Вспомогательный метод делающий перекрестное присваивание для удобства продублируем в Talker */
    public void setChat(SimpleChat chat) {
        /* В наш 'чатЛист' добавляем текущий чат */
        chatList.add(chat);
        /* Достаем у текущего чата 'токерЛист' и добавляем текущего 'токера' */
        chat.getTalkerList().add(this);
    }
}
