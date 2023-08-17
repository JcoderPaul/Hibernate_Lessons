package oldboy.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oldboy.entity.Payment;
import oldboy.entity.User;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

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
        return session
                .createQuery("select user " +
                                      "from User as user", User.class)
                .list();
    }
    /**
    Возвращает всех User-ов с указанным именем
    **/
    public List<User> findAllByFirstName(Session session, String firstName) {
        return session
                .createQuery("select user " +
                                      "from User as user " +
                                      "where user.personalInfo.firstName = :fName", User.class)
                .setParameter("fName", firstName)
                .list();
    }

    /**
    Возвращает первые {limit} User-ов, упорядоченных по дате рождения (в порядке возрастания)
    **/
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return session
                .createQuery("select u " +
                                      "from User as u " +
                                      "order by u.personalInfo.birthDate", User.class)
                /*
                В HQL мы не можем задать LIMIT, как в SQL,
                зато можем задать его через setMaxResults
                */
                .setMaxResults(limit)
                .list();

    }

    /**
    Возвращает всех сотрудников компании с указанным названием
    **/
    public List<User> findAllByCompanyName(Session session, String companyName) {
        /* Тут мы заходим 'со стороны компании' и объединяем данные двух таблиц БД */
        return session.
                createQuery("select u from Company as c " +
                                     "join c.users as u " +
                                     "where c.companyName = :companyName", User.class).
                setParameter("companyName", companyName).
                list();
    }

    /**
    Извлечь из БД все выплаты, полученные сотрудниками компании с
    указанными именем, упорядоченные по имени сотрудника, а затем
    по размеру выплаты.
    **/
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return session.
                /*
                Как и в SQL сначала извлекаем данные из связанных таблиц,
                а затем проводим упорядочивание, если бы мы работали в MySql,
                то это простое форматирование виртуальной таблицы на основе
                данных извлеченных из реальных таблиц, но тут мы работаем с
                объектами.
                */
                createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.companyName = :companyName " +
                        "order by u.personalInfo.firstName, p.amount", Payment.class).
                setParameter("companyName", companyName).
                list();
    }

    /**
    Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
    **/
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session,
                                                              String firstName,
                                                              String lastName) {
        /*
        В отличие от предыдущих методов, данный возвращает не список объектов,
        а некое значение в формате DOUBLE, значит метод извлечения данных *.list()
        нам не подходит. Нужно помнить, что при создании запроса, мы можем получить
        результат 'ничего' - null (вычислять не из чего), либо, более одного и
        следовательно, вызвать исключение. Отсюда - внимательнее составлять запрос.
        */
        return session.
                createQuery("select avg(pay.amount) from Payment as pay " +
                                     "join pay.receiver as user " +
                                     "where user.personalInfo.firstName = :firstName " +
                                     "and user.personalInfo.lastName = :lastName", Double.class).
                setParameter("firstName", firstName).
                setParameter("lastName", lastName).
                uniqueResult();
    }

    /**
    Возвращает для каждой компании:
    название, среднюю зарплату всех её сотрудников.
    Компании упорядочены по названию.
    **/
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        /*
        Если соблюдать SQL подобный синтаксис при написании HQL запроса,
        то понятно, что и откуда берется и, что с чем связано. Сразу понимаешь,
        что тщательно прописанный 'mapping' сущностей (и продуманная структура
        самой БД) сильно помогает при работе.

        Параметризуем наш запрос возвращаемым классом.
        */
        return session.
                createQuery(
                        "select c.companyName, avg(p.amount) from Company as c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.companyName " +
                        "order by c.companyName ", Object[].class).
                list();
    }

    /**
    Возвращает список:
    - сотрудник (объект User),
    - средний размер выплат, но только для тех сотрудников, чей средний размер выплат
      больше среднего размера выплат всех сотрудников.
    Упорядоченный по имени сотрудника.
     */
    public List<Object[]> findSomethingWithSomething(Session session) {
        return session.
                createQuery(
              "select u, avg(userPay.amount) from User as u " +
                       "join u.payments as userPay " +
                       "group by u " +
                       "having avg(userPay.amount) > (select avg(p.amount) from Payment p) " +
                       "order by u.personalInfo.firstName ", Object[].class).
                list();
    }
}