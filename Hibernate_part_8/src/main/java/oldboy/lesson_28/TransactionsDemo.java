package oldboy.lesson_28;
/*
Транзакции в Hibernate.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TransactionsDemo {
    public static void main(String[] args) {
        /* Классический вариант, который мы уже видели
        Шаг 1. - создаем фабрику сессий
        */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             /* Шаг 2. - открываем сессию */
             Session session = sessionFactory.openSession()) {
             /*
             Контроллер, позволяющий пользователям выполнять задачи, связанные
             с JDBC, с использованием соединения, управляемого этим сеансом.
             В данном случае мы проверяем установленный уровень изоляции транзакций.
             Поскольку мы не вносили изменения в настройки БД то увидим - 2,
             установка PostgreSQL по-умолчанию.
             */
             session.doWork(connection ->
                     System.out.println(connection.getTransactionIsolation()));

           try {
               /* Шаг 3. - начинаем транзакцию */
               var transaction = session.beginTransaction();

               var payment1 = session.find(Payment.class, 1L);
               var payment2 = session.find(Payment.class, 2L);
               /* Шаг 4. - коммитим текущую транзакцию */
               session.getTransaction().commit();
            } catch (Exception exception) {
                /* Шаг 5. - в случае выброса исключения откатываем транзакцию */
                session.getTransaction().rollback();
                /* Однако, транзакция уже может быть откачена и мы снова хапнем исключение */
                throw exception;
           }
        }
    }
}
