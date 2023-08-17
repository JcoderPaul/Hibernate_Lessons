package oldboy.lesson_25;
/*
Проблема N+1 и ее решения.

В данном случае мы используем 'fetch' из арсенала CriteriaAPI.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import oldboy.entity.Payment;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class UseFetchInCriteriaAPIDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();

            String companyName = "Google";

            /* CriteriaAPI запрос */
            CriteriaBuilder crBuilder = currentSession.getCriteriaBuilder();
            CriteriaQuery<Payment> criteria = crBuilder.createQuery(Payment.class);
            /* Возвращаем корневой объект, поэтому Payment - Root */
            Root<Payment> payment = criteria.from(Payment.class);

            Join<Payment, User> user = payment.join("receiver");
                                       payment.fetch("receiver");

            Join<User, Company> company = user.join("company");
            /* Формируем критериа селектор */
            criteria.
                    select(payment).
                    where(crBuilder.equal(company.get("companyName"), companyName)).
                    orderBy(crBuilder.asc(user.get("personalInfo").get("firstName")),
                            crBuilder.asc(payment.get("amount")));
            /* Формируем Hibernate запрос */
            List<Payment> paymentList =
                                    currentSession.
                                    createQuery(criteria).
                                    list();

            System.out.println(paymentList.size());

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
