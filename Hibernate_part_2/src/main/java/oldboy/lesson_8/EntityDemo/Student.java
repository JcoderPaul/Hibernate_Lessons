package oldboy.lesson_8.EntityDemo;
/*
Пример генерации ключа БД - SEQUENCE,
в данном случае это аналог счетчика с
авто-инкрементом. Нужно помнить, что
подобные последовательности есть не во
всех БД (например в MySQL их нет).
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "students", schema = "public")
public class Student {

    @Id
    /* Обязательно указываем имя генератора */
    @GeneratedValue(generator = "stud_gen", strategy = GenerationType.SEQUENCE)
    /* Теперь нам нужно указать на нашу таблицу SEQUENCE */
    @SequenceGenerator(name = "stud_gen", sequenceName = "students_id_seq", allocationSize = 1)
    private Long id;

    private String student_name;
    private String student_email;
    private int age;
}
