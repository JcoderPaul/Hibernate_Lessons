package oldboy.lesson_31;
/*
Установка ограничений на уровне БД из приложения, средствами Session и
методом *.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate()
Однако, каждая БД имеет свой набор оптимизаций по ограничению доступа
к данным по установке - SET TRANSACTION READ ONLY. См. документацию по
конкретной БД.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ReadOnlyNativeQuery {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {

            sessionOne.beginTransaction();
            /* Устанавливаем запрет на изменения данных на уровне БД */
            sessionOne.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();
            /* Пытаемся внести изменения в БД*/
            Student student = sessionOne.get(Student.class, 1L);
            student.setScholarship(student.getScholarship() + 1110);
            /* Коммитим изменения */
            sessionOne.getTransaction().commit();
            /*
            Ловим ожидаемый бросок исключения:
            WARN [org.hibernate.engine.jdbc.spi.SqlExceptionHelper: 137] SQL Error: 0, SQLState: 25006

            Exception in thread "main" javax.persistence.PersistenceException
            Caused by: org.hibernate.exception.GenericJDBCException: could not execute statement
            */
        }
    }
}
