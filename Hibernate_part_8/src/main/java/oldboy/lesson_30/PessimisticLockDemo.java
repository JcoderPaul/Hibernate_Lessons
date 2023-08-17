package oldboy.lesson_30;
/*
Демонстрация работы PessimisticLock.
Над классом Schoolboy нет аннотаций
задающих тип локирования транзакций.

Все настройки блокировок идут в коде
текущего приложения.
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.Schoolboy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.LockModeType;

public class PessimisticLockDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession();
             Session sessionTwo = sessionFactory.openSession();
             Session sessionThree = sessionFactory.openSession()) {
            /* Порядок открытия транзакций не важен */
            sessionTwo.beginTransaction();
            sessionOne.beginTransaction();
            sessionThree.beginTransaction();
            /*
            Внесем три изменения в одной сущности, но из разных транзакций.
            Как и ранее обратимся к одной и той же записи в БД, но в первой
            транзакции передадим LockModeType, а во второй ничего не передаем.
            */
            Schoolboy schoolboyOne = sessionOne.find(Schoolboy.class, 1L,
                                                            LockModeType.PESSIMISTIC_READ);
            schoolboyOne.setScholarship(schoolboyOne.getScholarship() + 100);


            Schoolboy theSameSchoolboy = sessionThree.find(Schoolboy.class, 1L,
                                                            LockModeType.PESSIMISTIC_WRITE);
            theSameSchoolboy.setScholarship(theSameSchoolboy.getScholarship() + 100);

            Schoolboy againSameSchoolboy = sessionTwo.find(Schoolboy.class, 1L);
            againSameSchoolboy.setScholarship(theSameSchoolboy.getScholarship() + 200);
            /*
            А вот коммитим мы сначала вторую транзакцию, затем треть, при этом, никаких локов
            при обращении к сущности во второй транзакции мы не передавали, в отличие от первой
            и третьей.

            Помним, что первая транзакция берет данные с параметром PESSIMISTIC_READ, а третья
            PESSIMISTIC_WRITE.

            При такой последовательности коммитов мы получим ожидание (зависание) -
            пока первая транзакция не отпустит лок.

            В запросе Hibernate из первой транзакции видно, что 'where' имеет дополнение
            'for share':
            select
                    schoolboy0_.id as id1_4_0_,
                    schoolboy0_.first_name as first_na2_4_0_,
                    schoolboy0_.last_name as last_nam3_4_0_,
                    schoolboy0_.scholarship as scholars4_4_0_
                from
                    part_four_base.schoolboys schoolboy0_
                where
                    schoolboy0_.id=? for share

            'FOR SHARE' будет блокировать UPDATE, DELETE, SELECT FOR UPDATE или
            SELECT FOR NO KEY UPDATE см. DOC/Row-Level-Lock-Modes.txt. Как раз то,
            что у нас и получилось, та же ситуация с третьей транзакцией.

            В запросе Hibernate из третьей транзакции видно, что 'where' имеет дополнение
            'for update':
            select
                    schoolboy0_.id as id1_4_0_,
                    schoolboy0_.first_name as first_na2_4_0_,
                    schoolboy0_.last_name as last_nam3_4_0_,
                    schoolboy0_.scholarship as scholars4_4_0_
                from
                    part_four_base.schoolboys schoolboy0_
                where
                    schoolboy0_.id=? for update

            'FOR UPDATE' будет блокировать UPDATE, DELETE, SELECT FOR UPDATE,
            SELECT FOR NO KEY UPDATE, SELECT FOR SHARE или SELECT FOR KEY SHARE
            см. DOC/Row-Level-Lock-Modes.txt. Снова блокировка.

            Проблема зависания или взаимной блокировки решается установкой TimeOut -
            времени ожидания транзакции.
            */
            sessionTwo.getTransaction().commit();
            sessionThree.getTransaction().commit();
            sessionOne.getTransaction().commit();
        }
    }
}
