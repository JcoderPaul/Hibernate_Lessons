package oldboy.lesson_2;
/* Установка связи с БД и отправка специфических данных - JSON строка */
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Birthday;
import oldboy.entity.Client;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

/* В тесты вынесен код создания SQL запроса для наглядности */
public class HibernateUserTypeDemo {
    public static void main(String[] args) {
        Configuration myFirstConfig = new Configuration();
        /* Добавляем в mapping наши классы */
        myFirstConfig.addAnnotatedClass(User.class);
        myFirstConfig.addAnnotatedClass(Client.class);
        myFirstConfig.addAttributeConverter(new BirthdayConverter(), true);
        /* Передаем (регистрируем) UserType класс из библиотеки com.vladmihalcea.hibernate.type */
        myFirstConfig.registerTypeOverride(new JsonBinaryType());

        myFirstConfig.configure();
        try(SessionFactory myFirstSessionFactory = myFirstConfig.buildSessionFactory();
            Session myFirstSession = myFirstSessionFactory.openSession()){
            myFirstSession.beginTransaction();

            Client secondHiberClient = Client.builder().
                    clientName("irulan@kaityn.emp").
                    bDay(new Birthday(LocalDate.of(1988, 2,12))).
                    info("""
                         {
                         "name":"Irulan",
                         "id":"231"
                          }
                     """).
                    build();
            /* Формируем персистентный объект */
            myFirstSession.persist(secondHiberClient);
            /* Проводим транзакцию и коммитим ее */
            myFirstSession.getTransaction().commit();
        }
    }
}
