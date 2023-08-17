package oldboy.lesson_26;
/*
Проблема N+1 и ее решения.

В данном случае мы используем 'fetch' из арсенала CriteriaAPI.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FetchProfilesDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();
            /*
            Разрешаем использовать FetchProfile, указываем
            имя прописанного нами FetchProfile в классе User.
            */
            currentSession.enableFetchProfile("withCompanyAndPayment");
            /*
            !!! ОСОБЕННОСТЬ !!! FetchProfile-а в том, что его можно применять
            только к отдельным сущностям с методом *.get(). Т.е. если мы вдруг
            захотим использовать его в HQL запросе у нас ничего не выйдет.
            */
            User user = currentSession.get(User.class, 11L);
            /*
            select
                user0_.user_id as user_id1_4_0_,
                user0_.company_id as company_8_4_0_,
                user0_.info as info2_4_0_,
                user0_.birth_date as birth_da3_4_0_,
                user0_.first_name as first_na4_4_0_,
                user0_.last_name as last_nam5_4_0_,
                user0_.role as role6_4_0_,
                user0_.user_name as user_nam7_4_0_,
                company1_.company_id as company_1_1_1_,
                company1_.company_name as company_2_1_1_,
                payments2_.receiver_id as receiver3_2_2_,
                payments2_.id as id1_2_2_,
                payments2_.id as id1_2_3_,
                payments2_.amount as amount2_2_3_,
                payments2_.receiver_id as receiver3_2_3_
            from
                part_four_base.users user0_
            left outer join
                part_four_base.company company1_
                    on user0_.company_id=company1_.company_id
            left outer join
                part_four_base.payment payments2_
                    on user0_.user_id=payments2_.receiver_id
            where
                user0_.user_id=?
            */
            System.out.println(user.getPayments().size());
            System.out.println(user.getCompany().getCompanyName());

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
