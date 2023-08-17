package oldboy.dao;

import lombok.Cleanup;
import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.Tuple;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class UserDaoTest {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final UserDaoWithCriteriaAPI userDao = UserDaoWithCriteriaAPI.getInstance();

    @AfterAll
    public void finish() {
        sessionFactory.close();
    }

    @Test
    void findAllTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> results = userDao.findAll(session);
        /*
        На момент тестирования базы данных было
        уже 10 записей в таблице users и они
        указаны ниже.
        */
        assertThat(results).hasSize(10);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(
                "Tasha Yar", "Jordy LaForge", "Garibo Coply",
                        "Aerdol T-Qute", "Star Lord", "Willy Wonka",
                        "Bary Gudkayn", "Willy Bushew", "Willy Ambush",
                        "Willy Bushew");

        session.getTransaction().commit();
    }

    @Test
    void findAllByFirstNameTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> results = userDao.findAllByFirstName(session, "Willy");

        assertThat(results).hasSize(4);
        assertThat(results.get(0).fullName()).isEqualTo("Willy Wonka");

        session.getTransaction().commit();
    }

    @Test
    void findLimitedUsersOrderedByBirthdayTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        int limit = 3;
        List<User> results = userDao.findLimitedUsersOrderedByBirthday(session, limit);
        assertThat(results).hasSize(limit);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).contains("Aerdol T-Qute", "Bary Gudkayn", "Willy Wonka");

        session.getTransaction().commit();
    }

    @Test
    void findAllByCompanyNameTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> results = userDao.findAllByCompanyName(session, "Google");
        assertThat(results).hasSize(6);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder(
                "Tasha Yar", "Jordy LaForge", "Garibo Coply",
                "Star Lord", "Bary Gudkayn", "Willy Bushew");

        session.getTransaction().commit();
    }

    @Test
    void findAllPaymentsByCompanyNameTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Payment> applePayments = userDao.
                findAllPaymentsByCompanyName(session, "Facebook");
        assertThat(applePayments).hasSize(2);

        List<Integer> amounts = applePayments.stream().map(Payment::getAmount).collect(toList());
        assertThat(amounts).contains(500, 300);

        session.getTransaction().commit();
    }

    @Test
    void findAveragePaymentAmountByFirstAndLastNamesTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Double averagePaymentAmount = userDao.
                findAveragePaymentAmountByFirstAndLastNames(session, "Willy", "Bushew");
        assertThat(averagePaymentAmount).isEqualTo(400.0);

        session.getTransaction().commit();
    }

    @Test
    void findCompanyNamesWithAvgUserPaymentsOrderedByCompanyNameTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        /* Выгребаем всю таблицу на предмет поставленных условий */
        List<Object[]> results =
                userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session);
        /*
        В таблице 'company' 5-ть записей, но только по 4-м из них есть соответствие в
        таблицах 'users' и 'payment', т.е. не один 'user' не связан с компанией 'Sony'
        под ID = 5 из таблицы 'company' (в нашей текущей БД). Отсюда ожидания = 4.
        */
        assertThat(results).hasSize(4);
        /* И так 'Sony' в таблице есть, но зарплату не платит и значит в списке ее нет */
        List<String> orgNames = results.stream().map(a -> (String) a[0]).collect(toList());
        assertThat(orgNames).contains("Yandex", "Google", "Facebook", "Polus");

        List<Double> orgAvgPayments = results.stream().map(a -> (Double) a[1]).collect(toList());
        /* ["Facebook", 400.0], ["Google", 500.0], ["Polus", 300.0], ["Yandex", 600.0] */
        assertThat(orgAvgPayments).contains(400.0, 500.0, 300.0, 600.0);

        session.getTransaction().commit();
    }

    @Test
    void findSomethingWithSomethingTest() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Tuple> results = userDao.findSomethingWithSomething(session);
        assertThat(results).hasSize(6);
        /*
        Наш список картежей 'result' содержит, как это не странно картежи -
        те в свою очередь содержат по два объекта каждый (User и Double),
        причем объекты User в картеже имеют индекс - 0, а Double - 1.

        Мы сами поместили их в таком порядке используя метод *.tuple()
        примененный к объекту CriteriaBuilder (см. UserDaoWithCriteriaAPI.java,
        метод *.findSomethingWithSomething(), который мы тут тестируем,
        собственно.

        Извлекаем нужные нам классы картежным методом *.get() см. DOC/TupleInterface.txt
        */
        List<String> names = results.stream().map(r -> r.get(0, User.class).fullName()).collect(toList());
        assertThat(names).contains(
                "Bary Gudkayn", "Aerdol T-Qute", "Jordy LaForge",
                "Tasha Yar", "Willy Bushew", "Willy Wonka");

        List<Double> averagePayments = results.stream().map(r -> r.get(1, Double.class)).collect(toList());
        assertThat(averagePayments).contains(700.0, 600.0, 500.0);

        session.getTransaction().commit();
    }
}