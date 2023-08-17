package oldboy.lesson_19;
/* Краткие примеры работы с HQL */
import oldboy.Util.HibernateUtil;
import oldboy.lesson_19.entity_19.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HQL_Named_Query {
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            session.beginTransaction();
            /*
            Используем именованный запрос сущности Employee
            (см. oldboy/lesson_19/entity_19/Employee.java):
            */
            List<Employee> listHQLQuery = session
                    .createNamedQuery("findEmployeeByFirstNameAndOrderingByLastName", Employee.class)
                    .setParameter("firstName","Aleksander")
                    .list();

            listHQLQuery.forEach(System.out::println);
            /*
            Hibernate:
                select
                    employee0_.Id as id1_2_,
                    employee0_.employee_email as employee2_2_,
                    employee0_.birth_date as birth_da3_2_,
                    employee0_.first_name as first_na4_2_,
                    employee0_.last_name as last_nam5_2_
                from
                    part_four_base.employee employee0_
                where
                    employee0_.first_name=?
                order by
                    employee0_.last_name asc

            Employee(Id=6, personalInfo=PersonalInfo(firstName=Aleksander, lastName=Fifsoff, ...)
            Employee(Id=1, personalInfo=PersonalInfo(firstName=Aleksander, lastName=Kochev, ...)
            Employee(Id=3, personalInfo=PersonalInfo(firstName=Aleksander, lastName=Valdau-Coster, ...)

            */

            System.out.println("\n----------------------- Next Query Result -----------------------");

            String nameToInsert = "Nikitin";
            List<Employee> listFromQuery = session
                            .createNamedQuery("findEmployeeByLastName", Employee.class)
                            .setParameter("lastName",nameToInsert)
                            .list();

            listFromQuery.forEach(System.out::println);
            /*
            Hibernate:
                select
                    employee0_.Id as id1_2_,
                    employee0_.employee_email as employee2_2_,
                    employee0_.birth_date as birth_da3_2_,
                    employee0_.first_name as first_na4_2_,
                    employee0_.last_name as last_nam5_2_
                from
                    part_four_base.employee employee0_
                where
                    employee0_.last_name=?

            Employee(Id=5, personalInfo=PersonalInfo(firstName=Sergey,
                                                     lastName=Nikitin,
                                                     birthDate=Birthday[birthDate=1998-07-21]),
                                                     employeeEmail=grey_boss@enerprise.com)
            */

            session.getTransaction().commit();
        }
    }
}