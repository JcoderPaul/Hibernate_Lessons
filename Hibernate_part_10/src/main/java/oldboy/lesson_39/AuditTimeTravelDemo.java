package oldboy.lesson_39;
/*
Методика работы с таблицами ...my_entity..._aud.
Мы имеем прямой доступ к сущностям (классам) созданным
нами и можем обращаться к БД и ее таблицам через Session,
однако у нас нет возможности напрямую обратиться к
таблицам созданным функционалом Hibernate Envers.

В данном приложении продемонстрируем метод получения
данных из аудит-таблиц сгенерированных Hibernate Envers.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import oldboy.entity.Payment;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

import javax.transaction.Transactional;
import java.util.List;

public class AuditTimeTravelDemo {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (Session sessionOne = sessionFactory.openSession()) {

                sessionOne.beginTransaction();

                Payment payment = sessionOne.find(Payment.class, 1L);
                System.out.println("\nТекущее состояние сущности: " + payment + "\n");

                sessionOne.getTransaction().commit();
            }

            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.beginTransaction();
                /*
                AuditReaderFactory предоставляет нам возможность достучаться
                до Envers аудит-таблиц, однако НУЖНО ЧЕТКО ПОНИМАТЬ, что эти
                таблицы не зеркало наших сущностей, а некий слепок состояний
                наших сущностей.

                Тут мы передаем нашу сессию в качестве параметра метода *.get()
                и получаем
                */
                AuditReader auditReader = AuditReaderFactory.get(sessionTwo);
                /*
                Тут мы получаем функционал подобный *.find() классической Session,
                однако данный вариант расширен до возможности передать в параметрах
                (на выбор):
                - Date date - дата ревизии;
                - Number revision - номер ревизии;
                Как раз поля в нашей таблице revision_records или в revinfo, которую
                генерирует Hibernate Envers.
                */
                Long revision = 11L;
                Payment oldPayment = auditReader.find(Payment.class, 1L, 11L);
                /*
                Тут возникает интересный момент, обращение идет в контекст состояний?
                в котором находятся 'прокси-объект' Payment_AUD - ревизионная копия.
                При этом !!! Сколь бы большим ни был номер ревизии, даже больше
                существующей мы получим результат последней !!!
                см. подзапрос (payment_au1_.REV<=?):

                select
                    payment_au0_.id as id1_5_,
                    payment_au0_.REV as rev2_5_,
                    payment_au0_.REVTYPE as revtype3_5_,
                    payment_au0_.amount as amount4_5_
                from
                    payments_AUD payment_au0_
                where
                    payment_au0_.REV=(
                        select
                            max(payment_au1_.REV)
                        from
                            payments_AUD payment_au1_
                        where
                            payment_au1_.REV<=?
                            and payment_au0_.id=payment_au1_.id
                    )
                    and payment_au0_.REVTYPE<>?
                    and payment_au0_.id=?

                !!! И это не полный функционал предоставленный AuditReaderFactory !!!
                */
                System.out.println("Данные из " + revision + " rev: " + oldPayment + "\n");
                /*
                Получим все сущности Payment с revision меньше 20, если заглянуть в
                'payments_aud', то можно увидеть, что там 3-и сущности и 6-ть ревизий.
                (актуально, ТОЛЬКО в нашем случае, на момент тестирования данного кода,
                естественно записей могло быть меньше или больше в зависимости он
                проводимых ранее операций)
                */
                List<Payment> allRevisionOfPayment = auditReader.createQuery().
                        forEntitiesAtRevision(Payment.class, 20L).
                        getResultList();
                allRevisionOfPayment.forEach(System.out::println);
                /*
                Payment(id=2, amount=590, receiver=null)
                Payment(id=1, amount=660, receiver=null)
                Payment(id=3, amount=420, receiver=null)

                Поскольку поле 'receiver' помечено @NotAudited то и результат null.
                */

                /*
                Еще более сложный CriteriaAPI-подобный запрос из функционала
                AuditReaderFactory:
                */
                var resForPrint = auditReader.createQuery().
                        /* Выбираем всех Payment с revision < 100 */
                        forEntitiesAtRevision(Payment.class, 100L).
                        /* Cо значением 'ammount' более 450 */
                        add(AuditEntity.property("amount").ge(450)).
                        /* Со значением 'id' более 4 */
                        add(AuditEntity.property("id").ge(2L)).
                        /* Получить поле ID и AMOUNT */
                        addProjection(AuditEntity.property("amount")).
                        addProjection(AuditEntity.id()).
                        getResultList();
                /*
                Самый мощный функционал, который позволяет используя старые данные
                из аудит-таблиц получить их и перезаписать ими текущие значения
                записей в БД.
                */
                Company backToPastCompany = auditReader.find(Company.class, 5, 5L);
                /*
                Находим 5-ю ревизию названия компании с ID = 5
                -----------------------------------------------
                company_id | "rev" | "revtype" | "company_name"
                -----------------------------------------------
                     5     |   5   |     1     | "BadRobot"
                -----------------------------------------------
                И заменяем им текущее название компании используя *.replicate():
                ---------------------------
                company_id | "company_name"
                ---------------------------
                     5     | "Miramax"
                ---------------------------
                В итоге текущее значение "Miramax" будет заменено на "BadRobot"
                */
                sessionTwo.replicate(backToPastCompany, ReplicationMode.OVERWRITE);

                sessionTwo.getTransaction().commit();
            }
        }
    }
}
