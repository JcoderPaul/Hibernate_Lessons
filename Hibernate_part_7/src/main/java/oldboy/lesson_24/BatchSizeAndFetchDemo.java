package oldboy.lesson_24;
/*
Проблема N+1 и ее решения.

Применение аннотации @ButchSize(size = .. n ..) позволяет облегчить процесс
загрузки данных из БД. Установив размер size - butch - a мы ограничиваем
количество получаемых записей.

!!! Действует простое правило установки аннотации @BatchSize(size = ..n..)!!!
- При отношениях ONE-TO-MANY, т.е. когда идет связка с коллекцией, аннотируется
поле внутри класса (см. класс 'User' поле 'payments');
- При отношениях Many-To-One, когда мы имеем поле связанное с другой сущностью
через @JoinColumn, аннотируется класс целиком (см. класс Company)
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class BatchSizeAndFetchDemo {
    public static void main(String[] args) {
        /* И повторим - создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();

            List<User> myUsers =
                    currentSession.
                    createQuery("select user " +
                                         "from User as user", User.class).
                    list();
            /*
            При таком запросе мы вытащим не только пользователей, но и связанные
            с ними данные из таблиц Payment и Profile, у нас 15 пользователей в БД,
            и если с каждым связанно 2-е таблицы, будет сформировано как минимум
            30 запросов, с учетом инициирующего запроса = 31 (это хорошо видно в
            консоли). Отсюда и возникает феномен и его название - 'N + 1'.

            Если же в классе User над полем 'payments' установить аннотацию
            @BatchSize и задать ее параметр (size = 3), то количество запросов
            сократится до 21.
            */
            myUsers.forEach(user -> System.out.println(user.getPayments().size()));
            /*
            Применение аннотации @Fetch(FetchMode.SUBSELECT) над полями коллекций
            позволило в нашем случае еще сократить количество запросов до 17 шт.
            Это основной запрос + 1 шт.:
                 select
                    user0_.user_id as user_id1_4_,
                    user0_.company_id as company_8_4_,
                    user0_.info as info2_4_,
                    user0_.birth_date as birth_da3_4_,
                    user0_.first_name as first_na4_4_,
                    user0_.last_name as last_nam5_4_,
                    user0_.role as role6_4_,
                    user0_.user_name as user_nam7_4_
                from
                    part_four_base.users user0_

            Запросы для вызова каждого user-a +15 шт.:
                   select
                        profile0_.profile_id as profile_1_3_0_,
                        profile0_.language as language2_3_0_,
                        profile0_.street as street3_3_0_,
                        profile0_.user_id as user_id4_3_0_
                    from
                        part_four_base.profile profile0_
                    where
                        profile0_.user_id=?

            И наконец запрос с подзапросом + 1 шт. :
                    select
                        payments0_.receiver_id as receiver3_2_1_,
                        payments0_.id as id1_2_1_,
                        payments0_.id as id1_2_0_,
                        payments0_.amount as amount2_2_0_,
                        payments0_.receiver_id as receiver3_2_0_
                    from
                        part_four_base.payment payments0_
                    where
                        payments0_.receiver_id in (
                            select
                                user0_.user_id
                            from
                                part_four_base.users user0_
                        )
            */
            System.out.println("--------------------- Get single user ---------------------");
            /*
            Естественно, при получении сущности по ID смысл наших манипуляций пропадает, т.к.
            запрос идет напрямую к конкретной таблице, а не через связные сущности (таблицы),
            и конечно не вызывает перекрестные запросы.
            */
            User oneUser =
                    currentSession.
                            createQuery("select user " +
                                                 "from User as user where userId = : Id ", User.class).
                            setParameter("Id", 2L).
                            getSingleResult();

            System.out.println(oneUser);
            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
