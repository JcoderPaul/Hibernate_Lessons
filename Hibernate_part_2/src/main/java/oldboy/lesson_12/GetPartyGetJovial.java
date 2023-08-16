package oldboy.lesson_12;
/* В данном примере показан только принцип, но не логика */
import oldboy.Util.HibernateUtil;
import oldboy.lesson_12.VirtualParty.Jovial;
import oldboy.lesson_12.VirtualParty.VirtualParty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GetPartyGetJovial {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            Jovial joviOne = null;
            VirtualParty partyOne = null;
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии : " + sessionOne.getStatistics());
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                joviOne = sessionOne.get(Jovial.class,3L);
                partyOne = sessionOne.get(VirtualParty.class, 2L);

                System.out.println(joviOne.getParties());
                System.out.println(partyOne.getJovial());

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
