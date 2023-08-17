package oldboy.lesson_25;
/*
Проблема N+1 и ее решения.

В данном случае мы используем 'fetch' из арсенала QueryDSL.
*/
import com.querydsl.jpa.impl.JPAQuery;
import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static oldboy.entity.QCompany.company;
import static oldboy.entity.QPayment.payment;
import static oldboy.entity.QUser.user;

public class UseFetchInQueryDslDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();

            String companyName = "Google";

            List<Payment> paymentList =  new JPAQuery<Payment>(currentSession).
                    select(payment).
                    from(payment).
                        /* payment.receiver as user */
                        join(payment.receiver, user).
                        fetchJoin().
                        /* user.company as company */
                        join(user.company, company).
                    where(company.companyName.eq(companyName)).
                    orderBy(user.personalInfo.firstName.asc(), payment.amount.asc()).
                    fetch();

            System.out.println(paymentList.size());

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
