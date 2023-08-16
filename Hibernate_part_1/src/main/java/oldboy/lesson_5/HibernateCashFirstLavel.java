package oldboy.lesson_5;
/* Удаление и поиск записей в БД */
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateCashFirstLavel {
    public static void main(String[] args) {
        Configuration myConfiguration = new Configuration();

        myConfiguration.addAnnotatedClass(User.class);
        myConfiguration.addAttributeConverter(new BirthdayConverter(), true);

        myConfiguration.configure();
        /* Открываем сессию связи с базой, ее нужно закрывать после каждого сеанса */
        try(SessionFactory mySessionFactory = myConfiguration.buildSessionFactory();
            /* Данный объект формирует SQL запрос к БД и вносит изменения используя Reflection API */
            Session myWorkSession = mySessionFactory.openSession()){
            /* Начали транзакцию */
            myWorkSession.beginTransaction();
            /*
            Создадим 3-и оригинальных User-a
            */
            User UserOne = new User("compac@userbase.com",
                                            "Compo",
                                            "Trad",
                                            new Birthday(LocalDate.of(1968, 5,8)),
                                            Role.USER);

            User UserTwo = new User("asus@userbase.com",
                                    "Asya",
                                    "Brand",
                                            new Birthday(LocalDate.of(1945, 1,22)),
                                            Role.USER);

            User UserThree = new User("farmoza@island.ch",
                                       "Taiwan",
                                       "China",
                                               new Birthday(LocalDate.of(1858, 11,23)),
                                               Role.USER);

            myWorkSession.saveOrUpdate(UserOne);
            myWorkSession.saveOrUpdate(UserTwo);
            myWorkSession.saveOrUpdate(UserThree);
            /* Вносим объекты в базу и коммитим транзакцию */
            myWorkSession.getTransaction().commit();

            myWorkSession.beginTransaction();
            /*
            Объекты извлеченные из БД помещаются в кэш персистент контекста.
            PersistenceContext - имеет структуру HashMap, где хранятся все
            данные о текущей сессии. Т.е. извлекая запись из БД мы получаем
            объект, а при повторном его извлечении, только ссылку на его
            копию в кэше (uOne и uTwo).
            */
                    System.out.println("------------ Состояние кэша ДО удаления uOne ------------");
                    User uOne = myWorkSession.get(User.class, "lito@arracis.spy");
                    User uTwo = myWorkSession.get(User.class, "lito@arracis.spy");
                    User uThree = myWorkSession.get(User.class, "asus@userbase.com");
                    /* Кэшь содержит данные объектов - true */
                    System.out.println(myWorkSession.contains(uOne));
                    System.out.println(myWorkSession.contains(uTwo));
                    System.out.println(myWorkSession.contains(uThree));
                    /* А теперь удалим только объект uOne */
                    myWorkSession.evict(uOne);
                    System.out.println("------------ Состояние кэша ПОСЛЕ удаления uOne ------------");
                    /* Вроде удалили один объект, а в итоге обоих в кэше нет false и только uTree остался */
                    System.out.println(myWorkSession.contains(uOne));
                    System.out.println(myWorkSession.contains(uTwo));
                    System.out.println(myWorkSession.contains(uThree));
                    /* И снова достанем записи из БД */
                    System.out.println("------------ Снова добавили uOne и uTwo в кэш ------------");
                    uOne = myWorkSession.get(User.class, "lito@arracis.spy");
                    uTwo = myWorkSession.get(User.class, "lito@arracis.spy");
                    System.out.println(myWorkSession.contains(uOne));
                    System.out.println(myWorkSession.contains(uTwo));
                    System.out.println(myWorkSession.contains(uThree));
                    /* Чистим весь кэш */
                    myWorkSession.clear();
                    System.out.println("------------ Состояние кэша ПОСЛЕ полной чистки ------------");
                    System.out.println(myWorkSession.contains(uOne));
                    System.out.println(myWorkSession.contains(uTwo));
                    System.out.println(myWorkSession.contains(uThree));



            myWorkSession.getTransaction().commit();


        }
    }
}
