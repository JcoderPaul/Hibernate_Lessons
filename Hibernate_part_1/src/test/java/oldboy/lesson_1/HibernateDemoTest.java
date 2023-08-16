package oldboy.lesson_1;
/* Только для визуального понимания процессов в Hibernate */
import javax.persistence.Column;
import javax.persistence.Table;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateDemoTest {

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {

        User firstHiberUser = new User(
                "lito@arracis.spy",
                "Lito",
                "Atridis",
                new Birthday(LocalDate.of(1988, 2,12)),
                Role.ADMIN);
        /*
        Три параметра нужно получить/извлечь:
        - первый %s - название таблицы - public.users
        - второй (%s) - названия полей БД (или нашей сущности)
        - третий (%s) - само содержимое полей
        */
        String SQL_query = """
                INSERT
                INTO
                %s
                (%s)
                VALUES
                (%s)
                """;
        /* Первый % в SQL_query - Извлекаем параметр из firstHiberUser - название таблицы */
        String tableName = Optional.ofNullable(firstHiberUser.getClass().getAnnotation(Table.class)).
                map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name()).
                orElse(firstHiberUser.getUserName());

        /* Второй % в SQL_query - Извлекаем параметр из firstHiberUser - названия полей User */
        /* Выделяем массив полей для удобства подсчета этих самых полей при формировании 3-го % */
        Field[] declaredUserFields = firstHiberUser.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredUserFields).
        /* Получаем список полей или из аннотаций, или из имени полей */
                map(field -> Optional.ofNullable(field.getAnnotation(Column.class)).
                        map(Column::name).
                        orElse(field.getName())).
                /* Получаем строку с элементами, разделенными через ',' */
                collect(Collectors.joining(","));

        /* Третий % в SQL_query - Извлекаем значения соответствующие полям Entity, а вернее ? */
        String columnValues = Arrays.stream(declaredUserFields).
                map(field -> "?").
                collect(Collectors.joining(", "));

        /* Проверим визуально наш SQL_query*/
        String queryToPrepareStatement = SQL_query.formatted(tableName, columnNames, columnValues);
        System.out.println(queryToPrepareStatement);
        /*
        INSERT
        INTO
        public.users
        (username,firstname,lastname,birth_data,age)
        VALUES
        (?, ?, ?, ?, ?)
        */
        /* Теперь используем Hibernate */
        Connection testConnection = null;
        PreparedStatement testPreparedStatement = testConnection.
                                        prepareStatement(queryToPrepareStatement);
        /* Итерируемся по всем полям нашего класса, чтобы получить значения полей и задать их в statement */
        for (Field declaredField : declaredUserFields){
            declaredField.setAccessible(true);
            /*
            В Hibernate есть методы преобразующие одни типы в другие,
            необходимые для БД, например, LocalDate -> DateType:
            SQL, TimeStamp и т.д. если преобразовать нельзя, то Exception.

            !!! Тут Hibernate использует реализации интерфейса Type !!!
            */
            testPreparedStatement.setObject(1, declaredField.get(firstHiberUser));
        }

    }
}