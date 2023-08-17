package oldboy.lesson_42;
/*
В данном примере мы рассмотрим области кеширования 2-го уровня (regions).
Имена region до именования их нами см. DOC/CacheRegion/SecondLevelCacheRegions.jpg
в данном случае имена и настройки кеша дает сама система.

Далее мы сами переименовали regions для пяти наших сущностей, коллекции оставили без
изменения результат можно увидеть см. DOC/CacheRegion/ShortNamesRegions.jpg. В файле
resources/ehcache-config.xml мы настроили время жизни сущностей в regions и емкость
кеша.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;

import static java.lang.Thread.sleep;

public class SecondLevelCacheRegions {

    @Transactional
    public static void main(String[] args) throws InterruptedException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (Session sessionOne = sessionFactory.openSession()) {
                sessionOne.beginTransaction();
                System.out.println("\n --------- First session --------- \n");

                sessionOne.get(User.class, 1L);

                System.out.println(sessionFactory.
                                           getCache().
                                           containsEntity(User.class, 1L));
                /* true - время жизни сущности в кеше не вышло */
                System.out.println(sessionFactory.
                                            getCache().
                                            containsEntity(User.class, 3L));
                /* true - время жизни сущности в кеше не вышло*/
                sessionOne.getTransaction().commit();
            }

            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.beginTransaction();
                System.out.println("\n --------- Second session --------- \n");
                /* Формируем задержку */
                sleep(5000);

                System.out.println(sessionFactory.
                                       getCache().
                                       containsEntity(User.class, 1L));
                /*
                Все еще - true - время жизни сущности в кеше не
                вышло, но мы формируем более длительную задержку.
                */
                sleep(11000);
                System.out.println(sessionFactory.
                                        getCache().
                                        containsEntity(User.class, 1L));
                /*
                Более 10 сек., и теперь - false, согласно настройкам в ehcache-config.xml,
                наши сущности User могут жить в кеше не более 10 сек.
                */
                sessionTwo.getTransaction().commit();
            }
        }
    }
}
