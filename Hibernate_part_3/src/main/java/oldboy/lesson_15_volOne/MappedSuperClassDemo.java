package oldboy.lesson_15_volOne;

import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import oldboy.lesson_15_volOne.Entity_15_volOne.Driver;
import oldboy.lesson_15_volOne.Entity_15_volOne.Porter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MappedSuperClassDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
             sessionOne.beginTransaction();
             /*
             Если мы используем в качестве ID Long, то естественно,
             указываем это явно при использовании метода *.get(),
             главное не забыть, где используется Long, а где Integer
             */
             User usrPrn = sessionOne.get(User.class, 3L);
             System.out.println(usrPrn);
             /*
             Hibernate:
                select
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    training_base.users user0_
                where
                    user0_.user_id=?
            Hibernate:
                select
                    profile0_.profile_id as profile_1_4_0_,
                    profile0_.language as language2_4_0_,
                    profile0_.street as street3_4_0_,
                    profile0_.user_id as user_id4_4_0_
                from
                    training_base.profile profile0_
                where
                    profile0_.user_id=?
            User(userId=3, personalInfo=PersonalInfo(firstName=Jordy,
                                                     lastName=LaForge,
                                                     birthDate=Birthday[birthDate=2665-02-22]),
                                                     userName=vizor@enerprise.com, info={}, role=ADMIN)
             */
             Driver prnDrv = sessionOne.get(Driver.class, 1);
             System.out.println(prnDrv);
             /*
                Hibernate:
                    select
                        driver0_.id as id1_2_0_,
                        driver0_.car_model as car_mode2_2_0_,
                        driver0_.driver_age as driver_a3_2_0_,
                        driver0_.driver_name as driver_n4_2_0_
                    from
                        training_base.drivers driver0_
                    where
                        driver0_.id=?
                Driver(driverName=Ivad Drago, driverAge=24, carModel=International Harvester DCOF-405)
             */
             Porter prnPrt = sessionOne.get(Porter.class, 3L);
             System.out.println(prnPrt);
             /*
             Hibernate:
                    select
                        porter0_.id as id1_3_0_,
                        porter0_.porter_age as porter_a2_3_0_,
                        porter0_.porter_name as porter_n3_3_0_,
                        porter0_.storage_number as storage_4_3_0_
                    from
                        training_base.porters porter0_
                    where
                        porter0_.id=?
              Porter(porterName=Kseniya Onnatopp, porterAge=27, storageNumber=3)
              */
            sessionOne.getTransaction().commit();
        }
    }
}
