package oldboy.lesson_16;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.ProgrammingLang;
import oldboy.lesson_16.Entity_16.Manager;
import oldboy.lesson_16.Entity_16.Programmer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class TablePerClassDemo {
    public static void main(String[] args) {
        /* Повторим:
           - Создаем фабрику сессий;
           - Открываем сессию.
        */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            /* Открываем транзакцию в текущей сессии */
            session.beginTransaction();

            Company madePuma = Company.
                    builder().
                    companyName("Samsung").
                    build();
            session.saveOrUpdate(madePuma);
            /*
            Hibernate:
                insert
                into
                    training_base.company
                    (company_name)
                values
                    (?)
            */
            Programmer prgOne = Programmer.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Symon").
                            lastName("Toryany").
                            birthDate(new Birthday(LocalDate.of(1999, 8,12))).
                            build()).
                    employeeEmail("dably@varazyk.com").
                    programmingLanguage(ProgrammingLang.JAVA).
                    company(madePuma).
                    build();
            session.saveOrUpdate(prgOne);

            Manager mngOne = Manager.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Teya").
                            lastName("Enery").
                            birthDate(new Birthday(LocalDate.of(2001, 11,6))).
                            build()).
                    employeeEmail("raort.re@dodly.ru").
                    company(madePuma).
                    projectName("Second Table Per Class").
                    build();
            session.saveOrUpdate(mngOne);
            session.flush();
            /*
            Hibernate:
                select
                    nextval ('training_base.employee_id_seq')
            Hibernate:
                select
                    nextval ('training_base.employee_id_seq')

            Hibernate:
                insert
                into
                    training_base.programmers
                    (company_id, employee_email, birth_date,
                    first_name, last_name, programming_language, Id)
                values
                    (?, ?, ?, ?, ?, ?, ?)
            Hibernate:
                insert
                into
                    training_base.managers
                    (company_id, employee_email, birth_date,
                    first_name, last_name, project_name, Id)
                values
                    (?, ?, ?, ?, ?, ?, ?)
             */

            session.clear();

            Programmer prgPrn = session.get(Programmer.class, 1L);
            System.out.println(prgPrn);
            /*
            Hibernate:
            select
                programmer0_.Id as id1_12_0_,
                programmer0_.company_id as company_6_12_0_,
                programmer0_.employee_email as employee2_12_0_,
                programmer0_.birth_date as birth_da3_12_0_,
                programmer0_.first_name as first_na4_12_0_,
                programmer0_.last_name as last_nam5_12_0_,
                programmer0_.programming_language as programm1_7_0_
            from
                training_base.programmers programmer0_
            where
                programmer0_.Id=?

            Programmer(super=Employee(Id=1,
                                      personalInfo=PersonalInfo(firstName=Symon,
                                                                lastName=Toryany,
                                                                birthDate=Birthday[birthDate=1999-08-12]),
                                      employeeEmail=dably@varazyk.com),
                                      programmingLanguage=JAVA)
            */
            Manager prnMng = session.get(Manager.class, 2L);
            System.out.println(prnMng);
            /*
            Hibernate:
                select
                    manager0_.Id as id1_12_0_,
                    manager0_.company_id as company_6_12_0_,
                    manager0_.employee_email as employee2_12_0_,
                    manager0_.birth_date as birth_da3_12_0_,
                    manager0_.first_name as first_na4_12_0_,
                    manager0_.last_name as last_nam5_12_0_,
                    manager0_.project_name as project_1_4_0_
                from
                    training_base.managers manager0_
                where
                    manager0_.Id=?

            Manager(super=Employee(Id=2,
                                   personalInfo=PersonalInfo(firstName=Teya,
                                                             lastName=Enery,
                                                             birthDate=Birthday[birthDate=2001-11-06]),
                                   employeeEmail=raort.re@dodly.ru),
                                   projectName=Second Table Per Class)
            */
            
            /* Коммитим транзакцию в текущей сессии */
            session.getTransaction().commit();
        }
    }
}
