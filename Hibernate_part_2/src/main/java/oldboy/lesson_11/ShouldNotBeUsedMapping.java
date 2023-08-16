package oldboy.lesson_11;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Client;
import oldboy.entity.Profile;
import oldboy.entity.accessory.Birthday;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

/*
Вариант связи Один-к-Одному который не рекомендуется
использовать - с сущностями со сложносоставным ключом.
*/
public class ShouldNotBeUsedMapping {
    public static void main(String[] args) {
        /* Создаем сущность без Id, который нам нужен */
        Client client = Client.
                builder().
                clientName("Shiban Tanu").
                bDay(new Birthday(LocalDate.of(1328,4,3))).
                info(""" 
                         {
                         "name":"Shiban",
                         "id":"343"
                         }
                     """).
                build();
        /*
        Недостаток связи Один-к-Одному, да и других тоже, в
        том, что дочерний объект не может быть помещенным в БД
        без существования родительского, поэтому в нашем случае
        сначала следует завести сохранить в БД сущность Client,
        а затем через метод *.setClient() класса Profile,
        произвести взаимную связку и только затем сохранение в БД.
        */
        Profile profile = Profile.
                builder().
                language("ru").
                street("3-st street bd.13").
                build();
        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                sessionOne.save(client);
                /*
                Статистика первой сессии:
                SessionStatistics[entity count=0,collection count=0]

                Hibernate:
                ******************************************************************************
                insert
                        into
                public.clients
                        (birth_data, client_name, info)
                values
                        (?, ?, ?)
                ******************************************************************************
                */
                profile.setClient(client);
                sessionOne.save(profile);

                System.out.println("Статистика первой сессии перед commit: " + sessionOne.getStatistics());
                /*
                Статистика первой сессии перед commit:
                SessionStatistics[entity count=2,collection count=0]
                ******************************************************************************
                Hibernate:
                    insert
                    into
                        profile
                        (language, street, profile_id)
                    values
                        (?, ?, ?)
                ******************************************************************************

                Если посмотреть в БД, то можно увидеть соответствие:
                Таблица Clients: client_id = 5
                Таблица Profile: profile_id = 5
                В таблице Profile, Id не генерируется автоматически, а связан с Id таблицы Clients,
                который генерируется автоматически. Именно такой способ связки Один-к-Одному и
                генерации ключа не сильно приветствуется при разработке БД и связей.
                */
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");

            Session sessionTwo = null;
            try {
                /* Открыли вторую сессию */
                sessionTwo = sessionFactory.openSession();
                sessionTwo.beginTransaction();
                /* Извлекаем сущность из БД по известному ID и имеющему связку с Profile */
                Client clnPrn = sessionTwo.get(Client.class, 9);
                System.out.println(clnPrn);
                /*
                Запрос Hibernate:
                    select
                        client0_.client_id as client_i1_0_0_,
                        client0_.birth_data as birth_da2_0_0_,
                        client0_.client_name as client_n3_0_0_,
                        client0_.info as info4_0_0_,
                        profile1_.profile_id as profile_1_9_1_,
                        profile1_.language as language2_9_1_,
                        profile1_.street as street3_9_1_
                    from
                        public.clients client0_
                    left outer join
                        profile profile1_
                            on client0_.client_id=profile1_.profile_id
                    where
                        client0_.client_id=?

                    Ответ БД:
                    Client(clientId=9,
                           clientName=Barzin Kartun,
                           bDay=Birthday[birthDate=1568-02-03],
                           info={"id": "534", "name": "Barzin"},
                           profile=Profile(profileId=9, street=1-st street bd.23, language=ru))
                    */
                sessionTwo.getTransaction().commit();
            } finally {
                sessionTwo.close();
            }
        }
    }
}
