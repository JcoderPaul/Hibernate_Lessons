package oldboy.lesson_1;
/* Установка связи с БД и отправка данных */
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
/* В тесты вынесен код создания SQL запроса для наглядности */
public class HibernateDemo {
    public static void main(String[] args) {
        /* Создаем нашу фабрику сессий и подключаем конфигурационный файл */
        Configuration myFirstConfig = new Configuration();
        /*
        Добавляем сущность через метод, *.addAnnotatedClass(), чтобы Hibernate
        отслеживала ее (так же можно конфигурировать этот процесс через *.XML
        файл свойств, говорят, длинный и страшный). Так же можно добавить mapping
        в hibernate.cfg.xml, например: <mapping class="oldboy.entity.User"/>.
        Без одной из этих настроек мы будем ловить исключения. Первый метод, через
        метод, наиболее удобен и динамичен.
        */
        myFirstConfig.addAnnotatedClass(User.class);
        /*
        Если в наших POJO объектах используются самописные классы и мы хотим,
        чтобы они автоматически конвертировались в SQL классы и обратно при
        работе с БД, нам необходимо объявить это Hibernate через метод
        приведенный ниже. Если второй параметр не установить в true, то в
        классе Entity (POJO) придется соответственно аннотировать нужное поле
        @Convert(converter = BirthdayConverter.class) см. User.java.
        */
        myFirstConfig.addAttributeConverter(new BirthdayConverter(), true);
        /*
        Если явно не прописан путь к hibernate.cfg.xml, то обращение по умолчанию
        идет к \src\main\resources, где и должен он находится.
        */
        myFirstConfig.configure();
        /* Открываем сессию связи с базой, ее нужно закрывать после каждого сеанса */
        try(SessionFactory myFirstSessionFactory = myFirstConfig.buildSessionFactory();
            /* Данный объект формирует SQL запрос к БД и вносит изменения используя Reflection API */
            Session myFirstSession = myFirstSessionFactory.openSession()){
            /* Начали транзакцию */
            myFirstSession.beginTransaction();
            /* Не забываем если код запустить повторно с этими же параметрами хапнем исключение */
            User firstHiberUser = new User("lito@arracis.spy",
                                            "Lito",
                                            "Atridis",
                                            new Birthday(LocalDate.of(1988, 2,12)),
                                            Role.ADMIN);
            /* Подготовили данные для БД */
            myFirstSession.persist(firstHiberUser);
            /* Закоммитили изменения в БД */
            myFirstSession.getTransaction().commit();
        }
    }
}
