package oldboy.lesson_12;
/* В данном примере показан только принцип, но не логика */
import oldboy.Util.HibernateUtil;
import oldboy.lesson_12.VirtualParty.Jovial;
import oldboy.lesson_12.VirtualParty.JovialAndParty;
import oldboy.lesson_12.VirtualParty.VirtualParty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.Instant;

public class ManyToManyVirtualParty {
    public static void main(String[] args) {

        JovialAndParty jap_1 = JovialAndParty.
                builder().
                created_time(Instant.now()).
                build();

        JovialAndParty jap_2 = JovialAndParty.
                builder().
                created_time(Instant.now()).
                build();

        VirtualParty partyOne = VirtualParty.
                builder().
                partyName("First Party").
                build();

        VirtualParty partyTwo = VirtualParty.
                builder().
                partyName("Star Party").
                build();

        Jovial jovialOne = Jovial.
                builder().
                jovialName("Stalker").
                jovialAge(31).
                build();

        Jovial jovialTwo = Jovial.
                builder().
                jovialName("Fighter").
                jovialAge(42).
                build();

        Jovial jovialThree = Jovial.
                builder().
                jovialName("Ashoka").
                jovialAge(142).
                build();

        Jovial jovialFour = Jovial.
                builder().
                jovialName("Annaken").
                jovialAge(242).
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
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                /* Сохраняем 'вечеринки' и 'весельчаков' */
                sessionOne.saveOrUpdate(partyOne);
                sessionOne.saveOrUpdate(partyTwo);

                sessionOne.saveOrUpdate(jovialOne);
                sessionOne.saveOrUpdate(jovialTwo);
                sessionOne.saveOrUpdate(jovialThree);
                sessionOne.saveOrUpdate(jovialFour);
                /*
                Задаем соответствие:
                - 'Первая вечеринка'
                - вечеринку создал jovialOne = Stalker
                - на вечеринку приглашен сам создатель и jovialTwo = Fighter
                */

                jap_1.setParty(partyOne);
                jap_1.setCreated_jovial(jovialOne.getJovialName());

                jovialOne.getParties().add(jap_1);
                jovialTwo.getParties().add(jap_1);
                jap_1.setJovial(jovialOne);
                jap_1.setJovial(jovialTwo);
                /*
                Задаем соответствие:
                - 'Вторая вечеринка'
                - вечеринку создал jovialThree = Ashoka
                - на вечеринку приглашен сам создатель и jovialFour = Annaken
                */
                jap_2.setParty(partyTwo);
                jap_2.setCreated_jovial(jovialThree.getJovialName());

                jovialThree.getParties().add(jap_1);
                jovialFour.getParties().add(jap_1);
                jap_2.setJovial(jovialThree);
                jap_2.setJovial(jovialFour);

                sessionOne.save(jap_1);
                sessionOne.save(jap_2);

                System.out.println("Статистика первой сессии перед commit : " + sessionOne.getStatistics());
                     sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
        }
    }
}
