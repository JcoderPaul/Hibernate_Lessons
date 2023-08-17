package oldboy.lesson_44;

import oldboy.Util.HibernateUtil;
import oldboy.dao.unrelated_entity_dao.StudentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SimpleDaoDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                StudentRepository studentRepository = new StudentRepository(sessionFactory);

                studentRepository.findById(1L).ifPresent(System.out::println);
                /*
                select
                student0_.stud_id as stud_id1_5_0_,
                        student0_.first_name as first_na2_5_0_,
                student0_.last_name as last_nam3_5_0_,
                        student0_.scholarship as scholars4_5_0_
                from
                students student0_
                where
                student0_.stud_id=?

                Student(studId=1, firstName=Thor, lastName=Odinson, scholarship=2354)
                */

                session.getTransaction().commit();
            }
        }
    }
}
