package oldboy.lesson_32;
/*
Запрос данных из БД без явного открытия транзакции.
По умолчанию у Hibernate стоит режим автоматического
коммита транзакций.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import oldboy.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class NonTransactionalDataAccess {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            /*
            Мы можем установить режим автокоммита в false:
            sessionOne.doWork(connection -> connection.setAutoCommit(false));

            Однако, режим AutoCommit имеет серьезные ограничения, т.е. вносить
            изменения в БД мы не сможем. Да мы сможем создать сущность, НО только
            если ее ID имеет стратегию GenerationType.IDENTITY.
            */
            Student student = Student.
                    builder().
                    firstName("Boris").
                    lastName("Karykov").
                    scholarship(4323).
                    build();
            sessionOne.save(student);


            List<Payment> payments = sessionOne.
                    createQuery("select p from Payment p", Payment.class).
                    list();
            payments.forEach(System.out::println);
            /*
            Если же попытаться вызвать sessionOne.flash();
            то компилятор бросит исключение.
            */

        }
    }
}
