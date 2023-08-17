package oldboy.lesson_41;
/*
В данном примере мы смотрим результат кеширования всех связных с User сущностей и их коллекций.
Теперь: User, UserChat, Company и коллекции внутри них проаннотированы, а значит будут помещены
в кэш второго уровня и обращений к БД будет меньше (или совсем не будет).
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;

public class AnnotatedAllEntitiesCollections {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            User userFromOneSession = null;
            try (Session sessionOne = sessionFactory.openSession()) {
                sessionOne.beginTransaction();
                System.out.println("\n1. --------- First session --------- First FIND method ---------\n");
                userFromOneSession = sessionOne.find(User.class, 1L);
                userFromOneSession.getCompany().getCompanyName();
                userFromOneSession.getUserChats().size();
                userFromOneSession.getProfile();
                System.out.println("\n2. --------- First session --------- Second FIND method ---------\n");
                User sameUserFromOneSession = sessionOne.find(User.class, 1L);
                /*
                При выводе данных в консоль видно, что при первом обращении к базе *.find() для
                получения сущности и всего с ней связанного, Hibernate сформировал необходимые SQL
                запросы, однако при втором обращении к сущности с тем же ID, никаких SQL запросов
                не формировалось - т.к. идет обращение к кэшу 1-го уровня. Пока все, как и ранее.
                */
                sessionOne.getTransaction().commit();
            }
            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.beginTransaction();
                System.out.println("\n3. --------- Second session --------- First FIND method ---------\n");
                User userFromTwoSession = sessionTwo.find(User.class, 1L);
                userFromTwoSession.getCompany().getCompanyName();
                /*
                И так мы закешировали все связные сущности и коллекции, и теперь,
                при обращении к ним из текущей сессии Hibernate не делает SQL запросы
                к БД, а воссоздает нужные нам сущности из кеша второго уровня.
                */
                userFromTwoSession.getUserChats().size();
                /*
                Т.е. как и говорилось, чтобы закешировать нужные нам сущности из коллекций,
                так же кешируются сами их классы см. DOC/CorrectEntityCaching.jpg.
                */
                userFromTwoSession.getProfile();
                /*
                А вот кешировать связь OneToOne, как мне показалось весьма экстремальное
                занятие, поскольку ссылки сущностей User и Profile перекрестные, тогда
                процесс кеширования оных принял бы форму сингулярности - вещь в себе -
                пожирающая всю доступную память. Возможно я ошибаюсь, т.к. объяснений я
                не нашел.

                Поэтому при обращении к сущности User из второй сессии снова происходит
                SQL запрос к БД за данными по Profile.
                */
                sessionTwo.getTransaction().commit();
            }
        }
    }
}
