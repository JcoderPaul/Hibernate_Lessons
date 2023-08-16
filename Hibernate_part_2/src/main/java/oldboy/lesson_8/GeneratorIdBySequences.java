package oldboy.lesson_8;
/*
Демонстрация способов генерации ID записи в БД,
стратегия генерации GenerationType.SEQUENCE
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_8.EntityDemo.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GeneratorIdBySequences {

    public static void main(String[] args) {

        Student studOne = Student.
                            builder().
                            student_name("Vasko Gurich").
                            student_email("vg@mail.ru").
                            age(21).
                            build();

        Student studTwo = Student.
                            builder().
                            student_name("Goiko Chepovich").
                            student_email("chepo@yandex.ru").
                            age(22).
                            build();
        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

                /* Следующий лог после начала транзакции */
                sessionOne.beginTransaction();

                sessionOne.saveOrUpdate(studOne);
                /*
                При сохранении сущности, сначала идет обращение к 'students_id_seq'
                ********************************************************************
                   Hibernate:
                        select
                             nextval ('students_id_seq')

                ********************************************************************
                и только за тем:
                ********************************************************************
                   Hibernate:
                        insert
                        into
                            public.students
                            (age, student_email, student_name, id)
                        values
                            (?, ?, ?, ?)
                ********************************************************************
                это легко видно в консоли и при пошаговой отладке.
                */
                sessionOne.saveOrUpdate(studTwo);

                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();
            } catch (Exception exc) {
                throw exc;
            } finally {
                sessionOne.close();
            }
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

            System.out.println("------------ Close first session ------------");
        }
    }
}
