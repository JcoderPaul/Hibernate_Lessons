package oldboy.lesson_29;
/* Вызовем принудительно исключение */
import oldboy.Util.HibernateUtil;
import oldboy.entity.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.LockModeType;

public class ExceptionEmulation {
    public static void main(String[] args) {
       try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session sessionOne = sessionFactory.openSession();
            Session sessionTwo = sessionFactory.openSession()) {
            /* Запускаем две транзакции */
            sessionTwo.beginTransaction();
            sessionOne.beginTransaction();
            /* Внесем два изменения в одной сущности, но из разных транзакций */
            Worker worker = sessionOne.find(Worker.class, 1L, LockModeType.OPTIMISTIC);
            worker.setAmount(worker.getAmount() + 100);
            System.out.println("Version from transactionOne: " +
                                sessionOne.get(Worker.class, 1L).getVersion());

            Worker theSameWorker = sessionTwo.find(Worker.class, 1L, LockModeType.OPTIMISTIC);
            theSameWorker.setAmount(theSameWorker.getAmount() + 20);
            System.out.println("Version from transactionTwo: " +
                                sessionTwo.get(Worker.class, 1L).getVersion());

            /* Коммитим первую транзакцию */
            sessionOne.getTransaction().commit();
            System.out.println("Version from transactionOne after commit: " +
                                sessionOne.get(Worker.class, 1L).getVersion());
            System.out.println("Version from transactionTwo before commit: " +
                                sessionTwo.get(Worker.class, 1L).getVersion());
            /* Коммитим вторую транзакцию и ловим исключение */
            sessionTwo.getTransaction().commit();
            /*
            Exception in thread "main" javax.persistence.OptimisticLockException:
            oldboy.lesson_29.ExceptionEmulation.main(ExceptionEmulation.java:26)

            !!! Что же произошло? !!!
            Мы обращаемся из двух транзакций к одной сущности, и в обеих получаем
            одинаковое значение поля version, допустим = 0. Далее первая транзакция
            коммитит изменения и получает значение version = 1, пока все хорошо. Но
            тут изменения вносит вторая транзакция, а у нее значение version на старте
            было 0, и тоже стало 1, и теперь она пытается внести изменения, однако:
            **************************************************************************
            select
                version as version_
            from
                part_four_base.workers
            where
                worker_id =?

            update
                part_four_base.workers
            set
                amount=?,
                first_name=?,
                last_name=?,
                version=?
            where
                worker_id=?
                and version=?
            **************************************************************************
            Значение version после внесения изменений не может оставаться одним и тем же,
            а у нас так и получилось.

            Первая транзакция изменяет version с 0 на 1, вторая транзакция делает тоже самое,
            и значение version осталось равным 1, тогда при сохранении изменений внесенных
            второй транзакцией возникает ошибка - version не может быть равна 1, т.к. она уже 1
            (условно, значение занято) и после внесения изменений не может оставаться таким же.
            */
        }
    }
}
