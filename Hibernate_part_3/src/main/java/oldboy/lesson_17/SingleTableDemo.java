package oldboy.lesson_17;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_17.Entity_17.Millwright;
import oldboy.lesson_17.Entity_17.DesignEngineer;
import oldboy.lesson_17.Entity_17.Enums.DesignSoftware;
import oldboy.lesson_17.Entity_17.Enums.MillwrightSpecialization;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class SingleTableDemo {
    public static void main(String[] args) {
        /* Повторим:
           - Создаем фабрику сессий;
           - Открываем сессию.
        */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            /* Открываем транзакцию в текущей сессии */
            session.beginTransaction();

            Company madeGoldStar = Company.
                    builder().
                    companyName("GoldStar").
                    build();
            session.save(madeGoldStar);
            /*
            Hibernate:
                insert
                into
                    training_base.company
                    (company_name)
                values
                    (?)
            */
            Millwright millOne = Millwright.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Tong Su").
                            lastName("Ma").
                            birthDate(new Birthday(LocalDate.of(2003, 6,12))).
                            build()).
                    employeeEmail("tsu.ma@goldstar.kr").
                    millwrightSpecialization(MillwrightSpecialization.BoardTester).
                    company(madeGoldStar).
                    build();
            session.saveOrUpdate(millOne);
            /*
            Не сложно заметить, как Hibernate самостоятельно подхватил и
            подставил соответствующий тип работника - type

            Hibernate:
                insert
                into
                    training_base.workers
                    (company_id, birth_date, first_name, last_name,
                    worker_email, millwright_specialization, type)
                values
                    (?, ?, ?, ?, ?, ?, 'millwright')
            */
            DesignEngineer deTwo = DesignEngineer.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Lung Te").
                            lastName("Wuo").
                            birthDate(new Birthday(LocalDate.of(2001, 11,6))).
                            build()).
                    employeeEmail("master_lung@goldstar.kr").
                    company(madeGoldStar).
                    designSoftware(DesignSoftware.P_CAD).
                    build();
            session.saveOrUpdate(deTwo);
            /*
            Естественно и тут Hibernate самостоятельно подхватил и
            подставил соответствующий тип работника - type

            Hibernate:
                insert
                into
                    training_base.workers
                    (company_id, birth_date, first_name, last_name,
                    worker_email, design_software, type)
                values
                    (?, ?, ?, ?, ?, ?, 'design_engineer')
            */
            session.flush();

            session.clear();

            Millwright millPrn = session.get(Millwright.class, 1L);
            System.out.println(millPrn);
            /*
            А вот как происходит выборка из БД по ID (обратить внимание на where...):

            Hibernate:
                select
                    millwright0_.id as id2_11_0_,
                    millwright0_.company_id as company_9_11_0_,
                    millwright0_.birth_date as birth_da3_11_0_,
                    millwright0_.first_name as first_na4_11_0_,
                    millwright0_.last_name as last_nam5_11_0_,
                    millwright0_.worker_email as worker_e6_11_0_,
                    millwright0_.millwright_specialization as millwrig7_11_0_
                from
                    training_base.workers millwright0_
                where
                    millwright0_.id=?
                    and millwright0_.type='millwright'

            Millwright(super=Worker(Id=1,
                                    personalInfo=PersonalInfo(firstName=Tong Su,
                                                              lastName=Ma,
                                                              birthDate=Birthday[birthDate=2003-06-12]),
                                    workerEmail=tsu.ma@goldstar.kr),
                                    millwrightSpecialization=BoardTester)
            */

            DesignEngineer dePrn = session.get(DesignEngineer.class, 2L);
            System.out.println(dePrn);
            /*
            Hibernate:
                select
                    designengi0_.id as id2_11_0_,
                    designengi0_.company_id as company_9_11_0_,
                    designengi0_.birth_date as birth_da3_11_0_,
                    designengi0_.first_name as first_na4_11_0_,
                    designengi0_.last_name as last_nam5_11_0_,
                    designengi0_.worker_email as worker_e6_11_0_,
                    designengi0_.design_software as design_s8_11_0_
                from
                    training_base.workers designengi0_
                where
                    designengi0_.id=?
                    and designengi0_.type='design_engineer'

            DesignEngineer(super=Worker(Id=2,
                                        personalInfo=PersonalInfo(firstName=Lung Te,
                                                                  lastName=Wuo,
                                                                  birthDate=Birthday[birthDate=2001-11-06]),
                                        workerEmail=master_lung@goldstar.kr),
                                        designSoftware=P_CAD)
            */
            session.getTransaction().commit();
        }
    }
}
