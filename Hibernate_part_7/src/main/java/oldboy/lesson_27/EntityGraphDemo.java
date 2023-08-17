package oldboy.lesson_27;
/*
Проблема N+1 и ее решения.

Использование Entity Graph.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.Map;

public class EntityGraphDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();
            /*
            ШАГ 1. - Получаем наш граф сущностей прописанный в
                     классе User под аннотацией @NamedEntityGraph.
            */
            RootGraph<?> myFirstGraph = currentSession.
                    getEntityGraph("withCompanyAndChat");
            /*
            Теперь при получении нашей сущности мы должны задействовать наш Graph.
            ШАГ 2. - Используем коллекцию в которую передаем наш граф в качестве HINT.
                     Далее коллекция свойств передается в метод *.find() текущей сессии.
            */
            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), myFirstGraph);

            User user = currentSession.find(User.class, 11L, properties);
            System.out.println(user.getCompany().getCompanyName());
            System.out.println(user.getUserChats().size());
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
                    userchats2_.user_id as user_id5_5_2_,
                    userchats2_.users_chats_id as users_ch1_5_2_,
                    userchats2_.users_chats_id as users_ch1_5_3_,
                    userchats2_.created_at as created_2_5_3_,
                    userchats2_.created_by as created_3_5_3_,
                    userchats2_.chat_id as chat_id4_5_3_,
                    userchats2_.user_id as user_id5_5_3_,
                    chat3_.chat_id as chat_id1_0_4_,
                    chat3_.chat_name as chat_nam2_0_4_
                from
                    part_four_base.users user0_
                left outer join
                    part_four_base.company company1_
                        on user0_.company_id=company1_.company_id
                left outer join
                    part_four_base.users_chats userchats2_
                        on user0_.user_id=userchats2_.user_id
                left outer join
                    part_four_base.chats chat3_
                        on userchats2_.chat_id=chat3_.chat_id
                where
                    user0_.user_id=?
            */

            System.out.println("\n--------------------- Classic HQL Query ---------------------");
            /*
            Созданный нами граф сущностей работает и в классическом
            HQL запросе, если передать в него HINT с нужными параметрами
            */
            User userHQLQuery = currentSession.createQuery(
                            "select us " +
                                     "from User as us " +
                                     "where us.userId = 11L", User.class)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(), myFirstGraph)
                    .getSingleResult();

            System.out.println(userHQLQuery.getUserChats().size());
            System.out.println(userHQLQuery.getCompany().getCompanyName());
            /*
                select
                    user0_.user_id as user_id1_4_0_,
                    userchats1_.users_chats_id as users_ch1_5_1_,
                    chat2_.chat_id as chat_id1_0_2_,
                    company3_.company_id as company_1_1_3_,
                    user0_.company_id as company_8_4_0_,
                    user0_.info as info2_4_0_,
                    user0_.birth_date as birth_da3_4_0_,
                    user0_.first_name as first_na4_4_0_,
                    user0_.last_name as last_nam5_4_0_,
                    user0_.role as role6_4_0_,
                    user0_.user_name as user_nam7_4_0_,
                    userchats1_.created_at as created_2_5_1_,
                    userchats1_.created_by as created_3_5_1_,
                    userchats1_.chat_id as chat_id4_5_1_,
                    userchats1_.user_id as user_id5_5_1_,
                    userchats1_.user_id as user_id5_5_0__,
                    userchats1_.users_chats_id as users_ch1_5_0__,
                    chat2_.chat_name as chat_nam2_0_2_,
                    company3_.company_name as company_2_1_3_
                from
                    part_four_base.users user0_
                left outer join
                    part_four_base.users_chats userchats1_
                        on user0_.user_id=userchats1_.user_id
                left outer join
                    part_four_base.chats chat2_
                        on userchats1_.chat_id=chat2_.chat_id
                left outer join
                    part_four_base.company company3_
                        on user0_.company_id=company3_.company_id
                where
                    user0_.user_id=11
            */

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
