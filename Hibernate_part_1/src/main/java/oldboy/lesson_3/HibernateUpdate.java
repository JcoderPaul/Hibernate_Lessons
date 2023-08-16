package oldboy.lesson_3;
/* Обновление записей и полей в БД */
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Birthday;
import oldboy.entity.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateUpdate {
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
            myFirstSession.beginTransaction();
            /*
            Не забываем если код запустить повторно с этими же параметрами хапнем исключение
            если используем метод *.persist() или устаревший метод .save(), т.е. добавить
            одного и того же пользователя, при правильных настройках БД, нельзя - все записи
            должны быть уникальны. Но если применить метод *.update() то картина изменится...
            */
            User firstHiberUser = new User("lito@arracis.spy",
                                            "Lito Two II",
                                            "Atridis",
                                            new Birthday(LocalDate.of(1998, 2,12)),
                                            Role.ADMIN);
            /*
            Подготовили данные для внесения изменений в БД. Если применить данный метод
            к уже существующей записи, то произойдет перезапись ее полей на соответствующие
            переданным.

            !!! Однако !!! Решающим фактором является ID поле записи. Т.е. вариант первый -
            мы точно знаем этот самый ID и по нему вносим в запись изменения, второй вариант -
            мы передаем методу *.update() данные, которые планируем изменить и метод сам
            извлекает необходимый ему ID, а затем по нему вносит изменения.

            В нашем случае ID это поле userName класса User т.е. именно по нему происходит
            поиск записи в БД, которую требуется изменить. Если по-данному userName(ID), если
            честно неудачное название, запись найдена, то она будет изменена, если записи нет,
            то выброс исключения.
            */
            myFirstSession.update(firstHiberUser);
            /*
            Нужно помнить, что Hibernate держит все изменения, которые мы подготовили для внесения,
            в подвешенном состоянии пока мы не закоммитим изменения или не закроем нашу транзакцию.
            */
            myFirstSession.saveOrUpdate(new User("usul@desert.duke",
                                                  "Ghost",
                                                  "Muadib",
                                                          new Birthday(LocalDate.of(2034,2,21)),
                                                          Role.ADMIN));
            /*
            Коммитим сразу две операции - изменение данных *.update() и метод *.saveOrUpdate(),
            который изменяет запись в БД если таковая есть, и просто сохраняет ее если таковую
            не нашел.
            */
            myFirstSession.getTransaction().commit();
        }
    }
}
