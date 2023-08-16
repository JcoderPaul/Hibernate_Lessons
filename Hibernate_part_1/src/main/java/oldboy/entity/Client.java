package oldboy.entity;
/*
Тут мы используя стороннюю библиотеку com.vladmihalcea.hibernate.type
и ее методы для интеграции данных в JSON формате в соответствующее поле
в БД. Для этого используется специальная аннотация @Type и ее параметр
указывающий на класс "описатель" или JsonTypeDescriptor.
*/
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.*;
import lombok.*;
import oldboy.converter.BirthdayConverter;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients", schema = "public")
/* Данная аннотация позволяет ввести именованный тип см. ниже */
@TypeDef(name = "myFirstType", typeClass = JsonBinaryType.class)
public class Client {
    @Id
    @Column(name = "client_id")
    /* Стратегия автогенерация БД */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;
    @Column(name = "client_name")
    private String clientName;
    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_data")
    private Birthday bDay;
    /*
    Можно просто оставить параметр: type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType",
    а можно добавить аннотацию над классом: @TypeDef(name = "myFirstType",
                                                     typeClass = JsonBinaryType.class)
    и сократить текущую до см. ниже:
    */
    @Type(type = "myFirstType")
    @Column(name = "info")
    /* В данное поле будет передан JSON объект. */
    private String info;

}
