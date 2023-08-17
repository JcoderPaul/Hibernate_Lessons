package oldboy.lesson_31;
/*
Демонстрация работы ReadOnlyMode.
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.Schoolboy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ReadOnlyLockDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession();
             Session sessionTwo = sessionFactory.openSession()) {
            /*
            Режим только для чтения устанавливается средствами сессии:
            - вариант 'ТОЛЬКО для ЧТЕНИЯ' накладывается на все сущности;

            Существует метод sessionOne.setReadOnly(), который позволяет
            ограничить от изменений только одну сущность.
            */
            sessionOne.setDefaultReadOnly(true);
            sessionOne.beginTransaction();

            Schoolboy schoolboyOne = sessionOne.find(Schoolboy.class, 1L);

            System.out.println("До изменений: " + schoolboyOne.getScholarship());
            schoolboyOne.setScholarship(schoolboyOne.getScholarship() + 100);
            System.out.println("После изменений: " + schoolboyOne.getScholarship());
            /*
            Hibernate:
                select
                    schoolboy0_.id as id1_4_0_,
                    schoolboy0_.first_name as first_na2_4_0_,
                    schoolboy0_.last_name as last_nam3_4_0_,
                    schoolboy0_.scholarship as scholars4_4_0_
                from
                    part_four_base.schoolboys schoolboy0_
                where
                    schoolboy0_.id=?
            До изменений: 854
            После изменений: 954

            Выглядит так, будто данные все же изменились, хотя запроса на UPDATE нет.
            Т.е. мы провели оптимизацию 'ДЛЯ ЧТЕНИ ТОЛЬКО' на уровне приложения.

            Коммитим и закрываем сессию.
            */
            sessionOne.getTransaction().commit();
            sessionOne.close();
            /* Начинаем транзакцию из другой сессии и поучаем данные снова */
            sessionTwo.beginTransaction();

            Schoolboy schoolboyOneAgain = sessionTwo.find(Schoolboy.class, 1L);
            System.out.println("Данные из второй транзакции: " + schoolboyOneAgain.getScholarship());
            /*
            Hibernate:
                select
                    schoolboy0_.id as id1_4_0_,
                    schoolboy0_.first_name as first_na2_4_0_,
                    schoolboy0_.last_name as last_nam3_4_0_,
                    schoolboy0_.scholarship as scholars4_4_0_
                from
                    part_four_base.schoolboys schoolboy0_
                where
                    schoolboy0_.id=?
            Данные из второй транзакции: 854
            */
            sessionTwo.getTransaction().commit();
        }
    }
}
