package oldboy.lesson_4;
/* Удаление и поиск записей в БД */
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateDelAndGet {
    public static void main(String[] args) {
        Configuration myFirstConfig = new Configuration();

        myFirstConfig.addAnnotatedClass(User.class);
        myFirstConfig.addAttributeConverter(new BirthdayConverter(), true);

        myFirstConfig.configure();
        /* Открываем сессию связи с базой, ее нужно закрывать после каждого сеанса */
        try(SessionFactory myFirstSessionFactory = myFirstConfig.buildSessionFactory();
            /* Данный объект формирует SQL запрос к БД и вносит изменения используя Reflection API */
            Session myFirstSession = myFirstSessionFactory.openSession()){
            /* Начали транзакцию */
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Начало первой транзакции :");
            myFirstSession.beginTransaction();
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
            myFirstSession.saveOrUpdate(new User("usul@desert.duke",
                                                 "Ghost",
                                                 "Muadib",
                                                         new Birthday(LocalDate.of(2034,2,21)),
                                                         Role.ADMIN));
            /*
            Применим метод *.get(), который возвращает объект из
            БД по ID, если таких записей в БД нет, то добавим их.
            */
            if (myFirstSession.get(User.class, "compac@userbase.com") == null ||
                    myFirstSession.get(User.class, "asus@userbase.com") == null ||
                        myFirstSession.get(User.class, "farmoza@island.ch") == null) {

            myFirstSession.saveOrUpdate(UserOne);
            myFirstSession.saveOrUpdate(UserTwo);
            myFirstSession.saveOrUpdate(UserThree);
            /* Добавим / изменим тот объект, что планируем удалять */
            myFirstSession.saveOrUpdate(new User("usul@desert.duke",
                                                 "Ghost",
                                                 "Muadib",
                                                         new Birthday(LocalDate.of(2034,2,21)),
                                                         Role.ADMIN));
            }
            /* Вносим объекты в базу и коммитим транзакцию */
            myFirstSession.getTransaction().commit();
            /*
            После запуска предыдущих уроков и предыдущих 3-х манипуляций с БД
            у нас, как минимум три записи в базе есть, а то и больше:
            ------------------------------------------------------------------
            username            | firstname   |  lastname | birth_data | role
            ------------------------------------------------------------------
            usul@desert.duke,   | Ghost,      |  Muadib,  | 2034-02-21,| ADMIN
            lito@arracis.spy,   | Lito Two II,|  Atridis, | 1998-02-12,| ADMIN
            compac@userbase.com,| Compo,      |  Trad,    | 1968-05-08,| USER
            asus@userbase.com,  | Asya,       |  Brand,   | 1945-01-22,| USER
            farmoza@island.ch,  | Taiwan,     |  China,   | 1858-11-23,| USER
            ------------------------------------------------------------------
            Удалим одну из них, сначала методом *.get() получим объект, а затем
            передадим его в метод *.delete():
            */
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("Начало второй транзакции :");
            /*
            Если делать в режиме отладки, и поставить перед началом транзакции точку останова, то видно, как
            в базе появляется в будущем удаляемая запись, а при продолжении отладки запись в базе пропадает.
            */
            myFirstSession.beginTransaction();
            User userForDelete = myFirstSession.get(User.class, "usul@desert.duke");
            myFirstSession.delete(userForDelete);
            myFirstSession.getTransaction().commit();
            /*
            Логика действий на одну транзакцию пока простая:
            - получаем текущую конфигурацию;
            - получаем структуру соответствия POJO и БД (мапинг) и прочие свойства из hibernate.cfg.xml;
            - создаем фабрику сессий и получаем из нее сессию;
            - из сессии получаем транзакцию;
            - подготавливаем персистентные объекты и действия с ними внутри транзакции;
            - коммитим транзакцию, тем самым вносим изменения в базу.

            Если не используется try-with-resources, то сессию нужно закрывать.
            */
        }
    }
}
