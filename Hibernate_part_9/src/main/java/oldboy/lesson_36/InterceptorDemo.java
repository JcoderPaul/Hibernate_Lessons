package oldboy.lesson_36;
/*
Демонстрация принципа работы слушателей,
использующих в своей структуре callback - и
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class InterceptorDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            /*
            Существует второй способ подключения перехватчика:
            ******************************************************
            Session session = sessionFactory
                     .withOptions()
                     .interceptor(new GlobalInterceptor())
                     .openSession()
            ******************************************************

            с виду не похожий на:

            ******************************************************
            configuration.setInterceptor(new GlobalInterceptor())
            ******************************************************

            в любом случае нам необходимо передать наш 'самописный'
            перехватчик в Session.
            */
            sessionOne.beginTransaction();
            /* Получаем из базы User-a */
            User takeUser = sessionOne.get(User.class, 5L);
            takeUser.getPersonalInfo().setLastName("Dragon");
            sessionOne.save(takeUser);
            /*
            Очень грубая демонстрация работы Interceptor-a (внедрение между запросами):

            Hibernate:
                select
                    profile0_.profile_id as profile_1_5_0_,
                    profile0_.language as language2_5_0_,
                    profile0_.street as street3_5_0_,
                    profile0_.user_id as user_id4_5_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?

            ************************
            OnFlushDirty Interceptor start!
            ************************

            Hibernate:
                update
                    part_four_base.users
                set
                    company_id=?,
                    info=?,
                    birth_date=?,
                    first_name=?,
                    last_name=?,
                    role=?,
                    user_name=?
                where
                    user_id=?
            */

            sessionOne.getTransaction().commit();
        }
    }
}
