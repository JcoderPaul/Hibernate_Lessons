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

public class CashFirstLavelVarTwo {
    public static void main(String[] args) {
        Configuration myConfiguration = new Configuration();

        myConfiguration.addAnnotatedClass(User.class);
        myConfiguration.addAttributeConverter(new BirthdayConverter(), true);

        myConfiguration.configure();
        /* Не используем блок try-with-resources */
        SessionFactory mySessionFactory = myConfiguration.buildSessionFactory();
        Session myWorkSession = mySessionFactory.openSession();
        /* Подготовим переменные */
        User uOne;
        User uTwo;
        User uThree;

        try {
             myWorkSession.beginTransaction();
            /*
            Чтобы ассоциировать сущность с текущей сессией достаточно метода *.get() или любого
            другого, переводящего сущность в персистентное состояние, например *..saveOrUpdate().
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
            /* Начинаем новую транзакцию в пределах текущей myWorkSession сессии */
            myWorkSession.beginTransaction();
            /*
            Объекты извлеченные из БД помещаются в кэш персистент контекста.
            PersistenceContext - имеет структуру HashMap, где хранятся все
            данные о текущей сессии. Т.е. извлекая запись из БД мы получаем
            объект, а при повторном его извлечении, только ссылку на его
            копию в кэше (uOne и uTwo).
            */
                    System.out.println("------------ Состояние кэша сессии myWorkSession ------------");
                    uOne = myWorkSession.get(User.class, "lito@arracis.spy");
                    uTwo = myWorkSession.get(User.class, "farmoza@island.ch");
                    uThree = myWorkSession.get(User.class, "farmoza@island.ch");
                    /* Кэшь содержит данные объектов - true */
                    System.out.println(myWorkSession.contains(uOne));
                    System.out.println(myWorkSession.contains(uTwo));
                    System.out.println(myWorkSession.contains(uThree));
                    /*
                    В файле HibernateCashFirstLavel.java были показаны методы
                    *.evict(), *.clear() для чистки элементов кэша PersistenceContext-а,
                    а теперь проверим статистику открытой сессии (entity count=4):
                    */
                    System.out.println(myWorkSession.getStatistics());

        } finally {
            /* А это третий и самый грубый - закрыть сессию, что естественно повлечет "алескапут" кэша */
            myWorkSession.close();
        }
        System.out.println("------------ Статистика закрытой сессии myWorkSession ------------");
        /* Несложно заметить, что количество Entity объектов = 0 */
        System.out.println(myWorkSession.getStatistics());
        /* Если попытаться вызвать myWorkSession.contains(....) хапнем исключение */
    }
}
