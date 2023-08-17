package oldboy.lesson_29;
/*
Транзакции в Hibernate - изучение LockModeType.OPTIMISTIC
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.LockModeType;

public class VersionOptimisticLock {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
             session.beginTransaction();

                /* Задаем через параметр режим оптимистической блокировки */
                Worker worker = session.find(Worker.class, 1L, LockModeType.OPTIMISTIC);
                System.out.println("Было: " + worker.getAmount());
                worker.setAmount(worker.getAmount() + 1000);
                System.out.println("Стало: " + worker.getAmount());
                /*
                select
                        worker0_.worker_id as worker_i1_6_0_,
                        worker0_.amount as amount2_6_0_,
                        worker0_.first_name as first_na3_6_0_,
                        worker0_.last_name as last_nam4_6_0_,
                        worker0_.version as version5_6_0_
                    from
                        part_four_base.workers worker0_
                    where
                        worker0_.worker_id=?

                Было: 2354
                Стало: 3354
                */
             System.out.println("------------ Перед коммитом транзакции ------------");
             session.getTransaction().commit();
             /*
             Hibernate:
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
             Hibernate:
                select
                    version as version_
                from
                    part_four_base.workers
                where
                    worker_id =?

             С каждым изменение значение поля version в БД тоже меняется.
             */
        }
    }
}
