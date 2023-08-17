package oldboy.lesson_40;
/*
Демонстрация работы кэша 2-го уровня. Перед запуском данного кода мы пометили
аннотацией @Cache только сущность User и только ее, отсюда все особенности,
что будут наблюдаться при запуске данного кода. Об особенностях кэширования
читать в DOC/SecondLevelCache.txt и см. все DOC/*.JPG

В данном примере все будет работать согласно комментариям только если у нас
закешированна только одна сущность User и только она. Данная ремарка дана
поскольку в дальнейших уроках-примерах мы будем кэшировать и другие сущности
и коллекции внутри них, а значит поведение текущего приложения будет меняться,
без изменения приведенного ниже кода.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;

public class SecondLevelCacheDemo {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Для кэширования сущности USER применяем аннотацию над ней см. User.java */
            User userFromOneSession = null;
            try (Session sessionOne = sessionFactory.openSession()) {
                sessionOne.beginTransaction();
                System.out.println("\n1. --------- First session --------- First FIND method ---------\n");
                userFromOneSession = sessionOne.find(User.class, 1L);
                userFromOneSession.getCompany().getCompanyName();
                userFromOneSession.getUserChats().size();
                System.out.println("\n2. --------- First session --------- Second FIND method ---------\n");
                User sameUserFromOneSession = sessionOne.find(User.class, 1L);
                /*
                При выводе данных в консоль видно, что при первом обращении к базе *.find() для
                получения сущности и всего с ней связанного, Hibernate сформировал необходимые SQL
                запросы, однако при втором обращении к сущности с тем же ID, никаких SQL запросов
                не формировалось - т.к. идет обращение к кэшу 1-го уровня.
                */
                sessionOne.getTransaction().commit();
            }
            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.beginTransaction();
                System.out.println("\n3. --------- Second session --------- First FIND method ---------\n");
                User userFromTwoSession = sessionTwo.find(User.class, 1L);
                userFromTwoSession.getCompany().getCompanyName();
                /*
                !!! Коллекции в сущностях следует кешировать отдельно,
                НО ВМЕСТЕ С САМОЙ КОЛЛЕКЦИОНИРУЕМОЙ (СВЯЗАННОЙ) СУЩНОСТЮ !!!
                Иначе мы получим обратный эффект, т.е. закэшируем ID сущностей в коллекции
                и их все равно придется все вызывать, а это ухудшение производительности.

                Правильно кэшировать и поле коллекции и сущность см. DOC/CorrectEntityCaching.jpg
                */
                userFromTwoSession.getUserChats().size();
                /*
                А вот тут произошел интересный момент, мы снова получаем ряд SQL запросов,
                но только по связным объектам нашей сущности, т.к. они не кешируются, мы не
                увидели запрос:
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
                    users user0_
                where
                    user0_.user_id=?

                И тут все просто Hibernate обратился к кэшу второго уровня см. DOC/SecondLevelCache.jpg
                и подтянул данные оттуда, а вот остальные сущности (в данном примере) мы не помечали
                как кэшируемые: profile, company, userchat и запрос по ним снова пошел в БД.

                !!! ОДНАКО, если заглянуть в память при прогоне кода в дебаге получается, что в разных
                сессиях мы имеем разные объекты - т.е. данные из кэша 2-го уровня десериализовались в
                кэш первого уровня для каждого контекста свои (хоть и стартовые данные были одними и
                теми же) см. DOC/SecondLevelCache.jpg !!!

                Поэтому, для того чтобы сохранить в кэше наши связные сущности мы должны и их пометить,
                как кэшируемые, если это действительно нужно см. DOC/CorrectEntityCaching.jpg.
                */
                sessionTwo.getTransaction().commit();
            }
        }
    }
}
