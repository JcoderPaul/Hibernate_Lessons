package oldboy.lesson_37;
/*
Демонстрация работы Hibernate Envers
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibEnversDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            Payment payment = sessionOne.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);
            /*
            При запуске текущего кода, первые два запроса генерируются Envers-ом,
            это создаются необходимые для аудита таблицы в нашей БД:
            Hibernate:
                create table payments_AUD (
                   id int8 not null,
                    REV int4 not null,
                    REVTYPE int2,
                    amount int4,
                    primary key (id, REV)
                )
            Hibernate:
                create table REVINFO (
                   REV int4 not null,
                    REVTSTMP int8,
                    primary key (REV)
                )
            результаты остальных запросов лучше см. в таблицах БД.
            */

            sessionOne.getTransaction().commit();
        }
    }
}
