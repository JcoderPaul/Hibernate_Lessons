package oldboy.lesson_8.EntityDemo;
/*
Пример генерации ключа БД - TABLE, как и ранее счетчик будет увеличиваться
на величину указанную в параметре allocationSize = 1 (n) авто-инкрементом.
Однако тут мы для хранения данных счетчика создали отдельную таблицу, которая
содержит сведения: о других таблицах, в которых идет инкремент счетчика
записей и текущую величину счетчика (общее количество записей).
См. DOC/SQLScripts/teacher_table.sql
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
@Table(name = "teachers", schema = "public")
public class Teacher {

    @Id
    /* Обязательно указываем имя генератора */
    @GeneratedValue(generator = "teach_gen", strategy = GenerationType.TABLE)
    /* Теперь нам нужно указать на нашу таблицу SEQUENCE */
    @TableGenerator(name = "teach_gen", table = "all_sequence",
                    allocationSize = 1, pkColumnName = "table_name",
                    valueColumnName = "pk_value")
    private Long id;

    private String teacher_name;
    private String teacher_email;
    private int age;
}
