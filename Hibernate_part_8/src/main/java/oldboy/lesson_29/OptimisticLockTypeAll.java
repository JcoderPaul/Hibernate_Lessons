package oldboy.lesson_29;
/*
Демонстрация работы аннотаций:
- @OptimisticLocking(type = OptimisticLockType.ALL)
- @DynamicUpdate
см. классы Student.java и Schoolboy.java.
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.Schoolboy;
import oldboy.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OptimisticLockTypeAll {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {

            sessionOne.beginTransaction();
            /*
            Динамика при создании запросов в Hibernate видна при вызове оных:
            - класс Student содержит настроечные аннотации (динамика);
            - класс Schoolboy не содержит аннотаций устанавливающих OptimisticLockType, но
              он, почти полная копия по структуре, класса Student;

            Сделаем два одинаковых запроса к этим сущностям и посмотрим результат.
            */
            Student student = sessionOne.get(Student.class, 1L);
            student.setScholarship(student.getScholarship() + 500);
            /*
            update
                    part_four_base.students
                set
                    scholarship=?
                where
                    stud_id=?
                    and first_name=?
                    and last_name=?
                    and scholarship=?
            */
            Schoolboy schoolboy = sessionOne.get(Schoolboy.class, 1L);
            schoolboy.setScholarship(schoolboy.getScholarship() + 500);
            /*
            update
                    part_four_base.schoolboys
                set
                    first_name=?,
                    last_name=?,
                    scholarship=?
                where
                    id=?
            */
            sessionOne.getTransaction().commit();
        }
    }
}
