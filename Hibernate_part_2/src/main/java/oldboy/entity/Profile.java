package oldboy.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString (exclude = "client")
@Entity
@Table(name = "profile")
public class Profile {
    /* Он же сразу и foreign key */
    @Id
    @Column(name = "profile_id")
    private Integer profileId;

    private String street;

    private String language;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Client client;
    /*
    Поскольку ключ у нас не автогенерируется мы должны
    прописать взаимные ссылки на связанные сущности:
    - в сущности Client задается текущая сущность Profile;
    - в сущности Profile задается Id созданного/извлеченного Client;
    */
    public void setClient(Client client){
        client.setProfile(this);
        this.client = client;
        this.profileId = client.getClientId();
    }
}