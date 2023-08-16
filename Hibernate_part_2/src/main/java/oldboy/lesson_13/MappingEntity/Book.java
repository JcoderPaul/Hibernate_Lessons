package oldboy.lesson_13.MappingEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books", schema = "public")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "book_name")
    private String bookName;
    /*
    Если не указать имя явно, то Hibernate будет искать таблицу
    по имени нашей сущности book и нижнее подчеркивание название
    текущего поля, т.е. locales - book_locales. Либо нужно
    правильно подбирать названия полей и таблиц, либо явно
    именовать таблицу через аннотацию @CollectionTable
    */
    @ElementCollection
    @CollectionTable(name = "book_locale",
                     schema = "public",
                     joinColumns = @JoinColumn(name = "book_id"))
    @Builder.Default
    private List<LocaleInfo> locales = new ArrayList<>();
}
