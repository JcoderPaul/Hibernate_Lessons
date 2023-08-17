package oldboy.lesson_18;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_18.Entity_18.CarServiceSpecialist;
import oldboy.lesson_18.Entity_18.EngineRepairman;
import oldboy.lesson_18.Entity_18.Enums.EngineModel;
import oldboy.lesson_18.Entity_18.Enums.MachineBodyDetail;
import oldboy.lesson_18.Entity_18.Tinman;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class JoinedDemo {
    public static void main(String[] args) {
        /* Повторим:
           - Создаем фабрику сессий;
           - Открываем сессию.
        */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            /* Открываем транзакцию в текущей сессии */
            session.beginTransaction();

            Company topCarService = Company.
                    builder().
                    companyName("HighSky").
                    build();
            session.save(topCarService);
            /*
            Hibernate:
                insert
                into
                    training_base.company
                    (company_name)
                values
                    (?)
            */
            Tinman tinman = Tinman.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Vasya").
                            lastName("Molotkov").
                            birthDate(new Birthday(LocalDate.of(1957, 2,5))).
                            build()).
                    employeeEmail("perfect_body@highsky.ru").
                    machineBodyDetail(MachineBodyDetail.REAR_FENDER_LEFT).
                    company(topCarService).
                    build();
            session.save(tinman);
            /*
            Hibernate:
                insert
                into
                    training_base.service_specialist
                    (company_id, birth_date, first_name, last_name, specialist_email)
                values
                    (?, ?, ?, ?, ?)
            Hibernate:
                insert
                into
                    training_base.tinman
                    (machine_body_detail, id)
                values
                    (?, ?)
            */
            EngineRepairman engineRepairman = EngineRepairman.
                    builder().
                    personalInfo(PersonalInfo.
                            builder().
                            firstName("Vintik").
                            lastName("Shpuntikov").
                            birthDate(new Birthday(LocalDate.of(1949, 2,9))).
                            build()).
                    employeeEmail("vkrutim_kak_nado@highsky.ru").
                    company(topCarService).
                    engineModel(EngineModel.AUDI_EA111_1_2_TFSI_CBZB).
                    build();
            session.save(engineRepairman);
            /*
            Hibernate:
                insert
                into
                    training_base.service_specialist
                    (company_id, birth_date, first_name, last_name, specialist_email)
                values
                    (?, ?, ?, ?, ?)
            Hibernate:
                insert
                into
                    training_base.engine_repairman
                    (engine_model, id)
                values
                    (?, ?)
            */
            session.flush();
            session.clear();

            Tinman tinmanPrn = session.get(Tinman.class, 1L);
            System.out.println(tinmanPrn);
            /*
            Hibernate:
                select
                    tinman0_.id as id1_9_0_,
                    tinman0_1_.company_id as company_6_9_0_,
                    tinman0_1_.birth_date as birth_da2_9_0_,
                    tinman0_1_.first_name as first_na3_9_0_,
                    tinman0_1_.last_name as last_nam4_9_0_,
                    tinman0_1_.specialist_email as speciali5_9_0_,
                    tinman0_.machine_body_detail as machine_1_10_0_
                from
                    training_base.tinman tinman0_
                inner join
                    training_base.service_specialist tinman0_1_
                        on tinman0_.id=tinman0_1_.id
                where
                    tinman0_.id=?

            Tinman(super=CarServiceSpecialist(Id=1,
                                              personalInfo=PersonalInfo(firstName=Vasya,
                                                                        lastName=Molotkov,
                                                                        birthDate=Birthday[birthDate=1957-02-05]),
                                              specialistEmail=perfect_body@highsky.ru),
                                              machineBodyDetail=REAR_FENDER_LEFT)
            */

            EngineRepairman engineRepairmanPrn = session.get(EngineRepairman.class, 2L);
            System.out.println(engineRepairmanPrn);
            /*
            Hibernate:
                select
                    enginerepa0_.id as id1_9_0_,
                    enginerepa0_1_.company_id as company_6_9_0_,
                    enginerepa0_1_.birth_date as birth_da2_9_0_,
                    enginerepa0_1_.first_name as first_na3_9_0_,
                    enginerepa0_1_.last_name as last_nam4_9_0_,
                    enginerepa0_1_.specialist_email as speciali5_9_0_,
                    enginerepa0_.engine_model as engine_m1_4_0_
                from
                    training_base.engine_repairman enginerepa0_
                inner join
                    training_base.service_specialist enginerepa0_1_
                        on enginerepa0_.id=enginerepa0_1_.id
                where
                    enginerepa0_.id=?

            EngineRepairman(super=CarServiceSpecialist(Id=2,
                                                       personalInfo=PersonalInfo(firstName=Vintik,
                                                                                 lastName=Shpuntikov,
                                                                                 birthDate=Birthday[birthDate=1949-02-09]),
                                                       specialistEmail=vkrutim_kak_nado@highsky.ru),
                                                       engineModel=AUDI_EA111_1_2_TFSI_CBZB)
            */
            CarServiceSpecialist carServiceSpecialist = session.get(CarServiceSpecialist.class, 2L);
            System.out.println(carServiceSpecialist);

            session.getTransaction().commit();
        }
    }
}
