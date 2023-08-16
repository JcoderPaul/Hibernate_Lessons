package oldboy.lesson_12;

import oldboy.Util.HibernateUtil;
import oldboy.lesson_12.MappingEntity.SimpleChat.SimpleChat;
import oldboy.lesson_12.MappingEntity.SimpleChat.Talker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ManyToManySimple {
    public static void main(String[] args) {
        /*
        Помним - 'chat_name VARCHAR(128) NOT NULL UNIQUE' -
        что значит, при совпадении имен чатов выбросится
        исключение.
        */
        SimpleChat chatOne = SimpleChat.
                builder().
                chatName("Second Chat").
                build();

        Talker talkerOne = Talker.
                builder().
                talkerName("Stalker").
                talkerAge(31).
                build();

        Talker talkerTwo = Talker.
                builder().
                talkerName("Gray worm").
                talkerAge(23).
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
                System.out.println("Статистика первой сессии : " + sessionOne.getStatistics());
                /* Статистика первой сессии : SessionStatistics[entity count=0,collection count=0] */
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                talkerOne.setChat(chatOne);
                talkerTwo.setChat(chatOne);

                sessionOne.save(chatOne);
                /*
                Hibernate:
                        insert
                        into
                            public.chats
                            (chat_name)
                        values
                            (?)
                Hibernate:
                        insert
                        into
                            public.talkers
                            (talker_age, talker_name)
                        values
                            (?, ?)
                Hibernate:
                        insert
                        into
                            public.talkers
                            (talker_age, talker_name)
                        values
                            (?, ?)
                */
                System.out.println("Статистика первой сессии перед commit : " + sessionOne.getStatistics());
                /*
                Статистика первой сессии перед commit : SessionStatistics[entity count=3,collection count=3]
                Hibernate:
                    insert
                    into
                        talkers_chats
                        (talker_id, chat_id)
                    values
                        (?, ?)
                Hibernate:
                    insert
                    into
                        talkers_chats
                        (talker_id, chat_id)
                    values
                        (?, ?)
                */
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
        }
    }
}
