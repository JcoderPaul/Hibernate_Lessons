package oldboy.dao;
/*
Пример работы с QueryDSL.
*/

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oldboy.dto.PaymentFilter;
import oldboy.entity.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

import static oldboy.entity.QCompany.company;
import static oldboy.entity.QPayment.payment;
import static oldboy.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    /**
    Метод возвращает всех User-ов из БД
    **/
    public List<User> findAll(Session session) {
        /*
        Сравнение HQL вариантов запроса и работы с запросом Criteria API (HQL):
        -----------------------------------------------------------------------
        return session
                .createQuery("select user " +
                                      "from User as user", User.class)
                .list();
        -----------------------------------------------------------------------
        см. ниже (QueryDSL):
        */

        /* Шаг 1 - создаем новый EntityManager для формирования последующего запроса */
        JPAQuery<User> usersList = new JPAQuery<>(session);

        /* Шаг 2 - формируем запрос, который визуально повторяет логику SQL запроса */
        return usersList.
                    select(user).
                    from(user).
                    fetch();
    }

    /**
    Возвращает всех User-ов с указанным именем
    **/
    public List<User> findAllByFirstName(Session session, String firstName) {
        /*
        С использованием HQL был так:
        return session
                .createQuery("select user " +
                                      "from User as user " +
                                      "where user.personalInfo.firstName = :fName", User.class)
                .setParameter("fName", firstName)
                .list();
         Применяя QueryDSL:
        */

        /*
        JPAQuery в качестве параметра принимает EntityManager,
        а интерфейс Session в свою очередь, расширяет и его тоже,
        поэтому мы передаем его.

        'user' - это импорт 'oldboy.entity.QUser', как и другие
        классы используемые тут для упрощения написания запросов.

        !!! НО НУЖНО ПОМНИТЬ !!! Мы работаем с сущностями, а не с
        полями таблиц БД. И тут применяется синтаксис HQL, а не SQL.

        И тогда прочитать, написанный ниже QueryDSL код можно так:
        "извлечь всех user-ов из таблицы user-ов БД, где первое имя
        user-a эквивалентно заданному параметром firstName текущего
        метода".
        */
        return new JPAQuery<User>(session).
                        select(user).
                        from(user).
                        where(user.personalInfo.firstName.eq(firstName)).
                        fetch();

    }

    /**
    Возвращает первые {limit} User-ов, упорядоченных по дате рождения (в порядке возрастания)
    **/
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        /*
        С использованием HQL было так:
        return session
                .createQuery("select u " +
                                      "from User as u " +
                                      "order by u.personalInfo.birthDate", User.class)
                .setMaxResults(limit)
                .list();

        QueryDSL:
        */
        return new JPAQuery<User>(session).
                            select(user).
                            from(user).
                            orderBy(user.personalInfo.birthDate.asc()).
                            limit(limit).
                            fetch();
    }

    /**
    Возвращает всех сотрудников компании с указанным названием
    **/
    public List<User> findAllByCompanyName(Session session, String companyName) {
        /*
        Тут мы заходим 'со стороны компании' и объединяем данные двух таблиц БД.
        С использованием HQL было так:
        return session.
                createQuery("select u from Company as c " +
                                     "join c.users as u " +
                                     "where c.companyName = :companyName", User.class).
                setParameter("companyName", companyName).
                list();
        QueryDSL:
        */
        return new JPAQuery<User>(session).
                            select(user).
                            from(company).
                            /*
                            В данном случае синтаксис метода *.join(company.users, user)
                            идеологически полностью повторяет синтаксис HQL строки
                            'join c.users as u' - т.е. присвоение псевдонима (alias-a), с
                            которым и работает весь наш QueryDSL запрос.

                            Ниже этот принцип применяется в каждом запросе, для нескольких
                            сущностей сразу, см. ниже.
                            */
                            join(company.users, user).
                            where(company.companyName.eq(companyName)).
                            fetch();
    }

    /**
    Извлечь из БД все выплаты, полученные сотрудниками компании с
    указанными именем, упорядоченные по имени сотрудника, а затем
    по размеру выплаты.
    **/
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        /*
        HQL:
        return session.
                createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.companyName = :companyName " +
                        "order by u.personalInfo.firstName, p.amount", Payment.class).
                setParameter("companyName", companyName).
                list();

        QueryDSL:
        */
        return new JPAQuery<Payment>(session).
                            select(payment).
                            from(payment).
                            join(payment.receiver, user). // payment.receiver as user
                            join(user.company, company). // user.company as company
                            where(company.companyName.eq(companyName)).
                            orderBy(user.personalInfo.firstName.asc(),
                                     payment.amount.asc()).
                            fetch();
    }

    /**
    Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
    **/
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session,
                                                              String firstName,
                                                              String lastName) {
        /*
        HQL:
        return session.
                createQuery("select avg(pay.amount) from Payment as pay " +
                                     "join pay.receiver as user " +
                                     "where user.personalInfo.firstName = :firstName " +
                                     "and user.personalInfo.lastName = :lastName", Double.class).
                setParameter("firstName", firstName).
                setParameter("lastName", lastName).
                uniqueResult();

        QueryDSL:
        */
        return new JPAQuery<Double>(session).
                            select(payment.amount.avg()).
                            from(payment).
                            join(payment.receiver, user). // payment.receiver as user
                            where(user.personalInfo.firstName.eq(firstName).
                                    and(user.personalInfo.lastName.eq(lastName))).
                            /*
                            Если *.fetch() возвращает List, а у нас должно вернуться
                            единственное значение, то используем другой метод *.fetchOne()
                            */
                            fetchOne();
    }

    /**
    Возвращает для каждой компании:
    название, среднюю зарплату всех её сотрудников.
    Компании упорядочены по названию.
    **/

    /* Тут берется картеж из другой библиотеки: com.querydsl.core.Tuple */
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        /*
        HQL:
        return session.
                createQuery(
                        "select c.companyName, avg(p.amount) from Company as c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.companyName " +
                        "order by c.companyName ", Object[].class).
                list();
         QueryDSL:
         */
        return new JPAQuery<Tuple>(session).
                            select(company.companyName, payment.amount.avg()).
                            from(company).
                            join(company.users, user). // company.users as user
                            join(user.payments, payment). // user.payments as payment
                            groupBy(company.companyName).
                            orderBy(company.companyName.asc()).
                            fetch();
    }

    /**
    Возвращает список:
    - сотрудник (объект User),
    - средний размер выплат, но только для тех сотрудников, чей средний размер выплат
      больше среднего размера выплат всех сотрудников.
    Упорядоченный по имени сотрудника.
     */
    /* Тут берется картеж из другой библиотеки: com.querydsl.core.Tuple */
    public List<Tuple> findSomethingWithSomething(Session session) {
        /*
        HQL:
        return session.
                createQuery(
              "select u, avg(userPay.amount) from User as u " +
                       "join u.payments as userPay " +
                       "group by u " +
                       "having avg(userPay.amount) > (select avg(p.amount) from Payment p) " +
                       "order by u.personalInfo.firstName ", Object[].class).
                list();

        QueryDSL:
        */
        return new JPAQuery<Tuple>(session).
                            select(user, payment.amount.avg()).
                            from(user).
                            join(user.payments, payment). // user.payments as payment
                            groupBy(user.userId).
                            having(payment.amount.avg().gt(
                                    /* Формируем подзапрос */
                                    new JPAQuery<Double>(session).
                                            select(payment.amount.avg()).
                                            from(payment))).
                            orderBy(user.personalInfo.firstName.asc()).
                            fetch();
    }

    /* --- ПРИМЕР ИСПОЛЬЗОВАНИЯ 'ФИЛЬТРОВ' --- */
    /**
     Возвращает среднюю зарплату сотрудника с указанными именем и фамилией.
     Фильтрация параметров через предикаты.
    **/
    public Double findAveragePaymentAmountByNamesFilterHardCodePredicates(Session session,
                                                                  PaymentFilter payFilter) {
        /*
        HQL:
        return session.
                createQuery("select avg(pay.amount) from Payment as pay " +
                                     "join pay.receiver as user " +
                                     "where user.personalInfo.firstName = :firstName " +
                                     "and user.personalInfo.lastName = :lastName", Double.class).
                setParameter("firstName", firstName).
                setParameter("lastName", lastName).
                uniqueResult();

        QueryDSL:
        */

        List<Predicate> predicates = new ArrayList<>();
             if (payFilter.getFirstName() != null) {
                   predicates.add(user.personalInfo.firstName.eq(payFilter.getFirstName()));
             }
             if (payFilter.getLastName() != null) {
                     predicates.add(user.personalInfo.lastName.eq(payFilter.getLastName()));
             }

        return new JPAQuery<Double>(session).
                select(payment.amount.avg()).
                from(payment).
                join(payment.receiver, user). // payment.receiver as user
                        where(predicates.toArray(Predicate[]::new)).
                fetchOne();
    }

    public Double findAveragePaymentAmountByNamesFilterPredicatesMethods(Session session,
                                                                 PaymentFilter payFilter) {
        /*
        HQL:
        return session.
                createQuery("select avg(pay.amount) from Payment as pay " +
                                     "join pay.receiver as user " +
                                     "where user.personalInfo.firstName = :firstName " +
                                     "and user.personalInfo.lastName = :lastName", Double.class).
                setParameter("firstName", firstName).
                setParameter("lastName", lastName).
                uniqueResult();

        QueryDSL:
        */
        var predicate = QPredicate.
                builder().
                add(payFilter.getFirstName(), user.personalInfo.firstName::eq).
                add(payFilter.getLastName(), user.personalInfo.lastName::eq).
                buildAnd();

        return new JPAQuery<Double>(session).
                select(payment.amount.avg()).
                from(payment).
                join(payment.receiver, user). // payment.receiver as user
                        where(predicate).
                fetchOne();
    }
}