package oldboy.lesson_6;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class EntityLifeCycle {
    public static void main(String[] args) {
        /*
        Мы создали сущность userComp и она не связанна ни
        с одной из сессий у нее пока состояние Transient
        */
        User userComp = new User("abit@formoza.ch",
                             "Abit",
                             "Brand",
                                     new Birthday(LocalDate.of(1965, 8,12)),
                                     Role.USER);
        User userVideo = new User("s3@trio.com",
                                  "s3",
                                  "LowGraph",
                                          new Birthday(LocalDate.of(1915, 3,23)),
                                          Role.USER);

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session2 = null;
            Session session1 = null;

            /* Сессия первая */
            System.out.println("------------ First session ------------");
            try {
                session1 = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + session1.getStatistics());

                session1.beginTransaction();
                /*
                А вот тут наш userComp уже в Persistent состоянии к первой
                сессии, но в состоянии Transient ко второй сессии.
                */
                session1.saveOrUpdate(userComp);
                session1.saveOrUpdate(userVideo);

                System.out.println("Статистика первой сессии " + session1.getStatistics());
                session1.getTransaction().commit();
            } finally {
                session1.close();
            }
                System.out.println("Статистика первой сессии " + session1.getStatistics());

            /* Сессия вторая */
            System.out.println("------------ Second session ------------");
            try {
                session2 = sessionFactory.openSession();
                System.out.println("Статистика второй сессии " + session2.getStatistics());

                session2.beginTransaction();

                userComp.setFirstName("Soltec");
                /*
                Повторно прочитать состояние данного экземпляра из БД, изменений в
                базу данных метод *.refresh() не вносит, примерный код:
                **************************************************************************
                    User freshUser = session2.get(User.class, user.getUsername());
                    user.setLastname(freshUser.getLastname());
                    user.setFirstname(freshUser.getFirstname());
                **************************************************************************
                */
                session2.refresh(userComp);

                userVideo.setFirstName("Voodoo");
                /* Метод *.merge() вносит изменения в БД, примерный код:
                **************************************************************************
                    User freshUser = session2.get(User.class, user.getUsername());
                    freshUser.setLastname(user.getLastname());
                    freshUser.setFirstname(user.getFirstname());
                **************************************************************************
                */
                session2.merge(userVideo);

                System.out.println("Статистика второй сессии " + session2.getStatistics());
                session2.getTransaction().commit();
            } finally {
                session2.close();
            }
                System.out.println("Статистика второй сессии " + session2.getStatistics());
        }
    }
}
