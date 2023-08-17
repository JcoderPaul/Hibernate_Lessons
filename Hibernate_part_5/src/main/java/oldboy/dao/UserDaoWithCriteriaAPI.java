package oldboy.dao;
/*
Логика действий во всех случаях следующая:
- Сначала мы получаем объект CriteriaBuilder.
- Затем, с его помощью, создаем объект CriteriaQuery.
- Затем, начинаем добавлять, остальные части с помощью
  CriteriaQuery и CriteriaBuilder.

Необходимо четко усвоить следующее:

- Ключевые операторы типа SELECT, FROM, WHERE вызываются у объекта CriteriaQuery
  (см. DOC/CriteriaQueryInterface.txt).
- Вспомогательные операторы типа AND, OR, DESC вызываются у объекта CriteriaBuilder
  (см. DOC/CriteriaBuilderInterface.txt).
- Имена полей берутся через get() у объекта Root
  (см. DOC/RootInterface.txt).

В некоторых методах используется вспомогательная библиотека:
annotationProcessor 'org.hibernate:hibernate-jpamodelgen:5.6.15.Final'
на момент изучения и тестирования ее возможностей уже вышла 6.2.7.Final.
*/
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oldboy.dto.CompanyDto;
import oldboy.entity.*;
import oldboy.entity.accessory.PersonalInfo_;
import org.hibernate.Session;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoWithCriteriaAPI {

    private static final UserDaoWithCriteriaAPI INSTANCE = new UserDaoWithCriteriaAPI();

    public static UserDaoWithCriteriaAPI getInstance() {
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
        см. ниже (Criteria API):
        */

        /* Шаг 1 - получаем CriteriaBuilder */
        CriteriaBuilder myFirstCriteriaBuilder = session.getCriteriaBuilder();
        /*
        Шаг 2 - создаем объект CriteriaQuery.
        И передаем в CriteriaQuery класс, который
        планируется возвращать из запроса
        (но это еще не запрос).
        */
        CriteriaQuery<User> myFirstCriteriaQuery =
                myFirstCriteriaBuilder.createQuery(User.class);
        /*
        Шаг 3. Получаем корневой объект.
        Через него мы сможем получать имена полей объекта.
        */
        Root<User> userRoot = myFirstCriteriaQuery.from(User.class);
        /*
        Через метод *.select() указываем элемент, который
        должен быть возвращен в результате запроса.
        */
        myFirstCriteriaQuery.select(userRoot);
        /*
        Формально строки выше можно переписать:
        myFirstCriteriaQuery.select(myFirstCriteriaQuery.from(User.class));
        Формируем окончательный запрос и получаем результат
        */
        return session.
                createQuery(myFirstCriteriaQuery).
                list();
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
         Применяя Criteria API:
        */

        /* Получаем доступ к объектам Criteria */
        CriteriaBuilder crBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> crQuery = crBuilder.createQuery(User.class);
        /* Выбираем корневой объект */
        Root<User> userRoot = crQuery.from(User.class);
        /*
        И так вспоминаем, что операторы типа SELECT, FROM, WHERE вызываются у объекта CriteriaQuery.
        Зато оператор equal(Expression<?> x, Expression<?> y), который проверяет аргументы на
        равенство, определен у CriteriaBuilder.

        В данном случае происходит работа с полями класса User, а значит для извлечения данных
        применяются классические методы *.get(). Основное неудобство - это вероятность написать
        название поля неверно. т.к. IDE не подсвечивает, и не проверяет на лету их.

        crQuery.select(
        crQuery.from(User.class)).
                where(crBuilder.equal(userRoot.get("personalInfo").get("firstName"), firstName)
        */
        crQuery.select(userRoot).
                where(crBuilder.equal(userRoot.get("personalInfo").get("firstName"), firstName)
        );

        return session.
                createQuery(crQuery).
                list();
    }

    /**
    Возвращает первые {limit} User-ов, упорядоченных по дате рождения (в порядке возрастания)
    **/
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        /*
        return session
                .createQuery("select u " +
                                      "from User as u " +
                                      "order by u.personalInfo.birthDate", User.class)
                .setMaxResults(limit)
                .list();

        Для удобства работы подключаем зависимость (теперь мы можем обращаться к полям
        объектов через, назовем их 'прокси' классы, которые имеют те же названия, что и
        наши Entity классы но со знаком нижнего подчеркивания Entity_):
        annotationProcessor 'org.hibernate:hibernate-jpamodelgen:5.6.15.Final'
        */
        CriteriaBuilder crBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> crQuery = crBuilder.createQuery(User.class);
        Root<User> userRoot = crQuery.from(User.class);

        /* После получения 3-х самых важных объектов для работы с запросами, формируем оный */
        crQuery.select(userRoot).
                orderBy(crBuilder.asc(userRoot.
                                get(User_.personalInfo).
                                get(PersonalInfo_.birthDate)));
        /* Для ограничения количества выводимых объектов используем классический *.setMaxResults() */
        return session.
                createQuery(crQuery).
                setMaxResults(limit).
                list();

    }

    /**
    Возвращает всех сотрудников компании с указанным названием
    **/
    public List<User> findAllByCompanyName(Session session, String companyName) {
        /* Тут мы заходим 'со стороны компании' и объединяем данные двух таблиц БД
        return session.
                createQuery("select u from Company as c " +
                                     "join c.users as u " +
                                     "where c.companyName = :companyName", User.class).
                setParameter("companyName", companyName).
                list();
        Посмотрим, как это выглядит с применением CriteriaAPI (пока все похоже, т.к. мы
        должны вернуть список объектов User):
        */
        CriteriaBuilder crBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> crQuery = crBuilder.createQuery(User.class);
        /*
        Тут начинаются отличия от предыдущих методов, т.к. мы заходим со стороны
        Company, то объект именно этого класса будет 'корневым', и к нему будет
        применен методы *.from() и *.join() для извлечения всех User-ов:
        */
        Root<Company> company = crQuery.from(Company.class);
        MapJoin<Company,String,User> users = company.join(Company_.users);
        /* Фильтруем выбранных User-ов по названию компании */
        crQuery.select(users).
                where(crBuilder.equal(company.get(Company_.companyName), companyName)
        );
        /*
        Выборка сделана (критериа запрос сформирован), все
        объединения и фильтрации заданы, отправляем запрос
        в БД.
        */
        return session.
                createQuery(crQuery).
                list();
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

        CriteriaAPI (как и ранее получаем 3-и критериа объекта):
        */
        CriteriaBuilder crBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = crBuilder.createQuery(Payment.class);
        /* Возвращаем корневой объект, поэтому Payment - Root */
        Root<Payment> payment = criteria.from(Payment.class);
        /*
        Тут мы применяем 2-а JOIN-а:
        - таблица 'payment' связана с таблицей 'user' через поле 'receiver_id',
          однако сам класс 'Payment' имеет поле 'receiver', мы же работаем с
          ОБЪЕКТАМИ.
        - таблица 'company' связанна с таблицей 'user' через поле 'company_id',
          та же картина с классом 'User' и его полем 'company'. НЕ ПУТАЕМ -
          аннотация над полем картирует поле сущности и поле таблицы БД с ней
          связанной.
        И тут, сразу, по красоте, цепочкой, мы видим связь - кого из кого извлекаем:
        */
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);
        /* Формируем критериа селектор */
        criteria.
                select(payment).
                where(crBuilder.equal(company.get(Company_.companyName), companyName)).
                orderBy(crBuilder.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstName)),
                        crBuilder.asc(payment.get(Payment_.amount)));
        /* Формируем Hibernate запрос */
        return session.
                createQuery(criteria).
                list();
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

        CriteriaAPI :
        */
        CriteriaBuilder crtBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = crtBuilder.createQuery(Double.class);
        /*
        Хотя мы возвращаем Double, данные о средней зарплате, без особого труда,
        мы можем получить только из таблицы 'payment', т.е. корневой сущностью
        из которой мы будем все извлекать - это Payment.
        */
        Root<Payment> payment = criteria.from(Payment.class);
        /* Но у нас фильтр по имени и фамилии User */
        Join<Payment, User> user = payment.join(Payment_.receiver);
        /* Формируем динамический список, в который попадет(ут) наш(и) предикат(ы) */
        List<Predicate> predicates = new ArrayList<>();
        /* Есть выполнение условия - добавили в список пердикат, нет - пролет */
        if (firstName != null) {
            predicates.add(crtBuilder.equal(user.get(User_.personalInfo).
                                                 get(PersonalInfo_.firstName), firstName));
        }
        if (lastName != null) {
            predicates.add(crtBuilder.equal(user.get(User_.personalInfo).
                                                 get(PersonalInfo_.lastName), lastName));
        }
        /* Селектор запроса с условиями */
        criteria.select(crtBuilder.avg(payment.get(Payment_.amount))).
                 where(predicates.toArray(Predicate[]::new)
        );

        return session.
                createQuery(criteria).
                uniqueResult();
    }

    /**
    Возвращает для каждой компании:
    название, среднюю зарплату всех её сотрудников.
    Компании упорядочены по названию.
    **/
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
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
         CriteriaAPI:
         */

        /*
        Шаг 1 - получаем CriteriaBuilder;
        Шаг 2 - объект CriteriaQuery будет возвращать Object[].class;
        Шаг 3 - корневой сущностью будет Company;
        */
        CriteriaBuilder crtBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = crtBuilder.createQuery(Object[].class);
        Root<Company> company = criteria.from(Company.class);
        /*
        Через 'цепочку JOIN-ов' получаем:
        - user-ов связанных с конкретными company-ями;
        - у каждого user-а получаем его payment-ы;
        */
        MapJoin<Company,String,User> user = company.join(Company_.users, JoinType.INNER);
        ListJoin<User,Payment> payment = user.join(User_.payments);
        /*
        Метод *.multiselect() - позволяет задать (указать) элементы,
        которые должны быть возвращены в результате запроса. Нам нужно
        вернуть 'название компании' и 'среднюю по компании зарплату'.
        */
        criteria.multiselect(company.get(Company_.companyName),
                             crtBuilder.avg(payment.get(Payment_.amount)))
                .groupBy(company.get(Company_.companyName))
                .orderBy(crtBuilder.asc(company.get(Company_.companyName)));

        return session.
                createQuery(criteria).
                list();
    }

    /**
     Повторим логику предыдущего метода, но будем использовать
     DTO класс. И так - возвращаем для каждой компании:
     название, среднюю зарплату всех её сотрудников.
     Компании упорядочены по названию. Но ведущим (возвращаемым)
     объектом будет список CompanyDto (см. oldboy/dto/CompanyDto.java).
    **/
    public List<CompanyDto> useTheLogicOfThePreviousMethodUsingTheCompanyDto(Session session) {
        /*
        Шаг 1 - получаем CriteriaBuilder;
        Шаг 2 - объект CriteriaQuery будет возвращать CompanyDto;
        Шаг 3 - корневой сущностью будет Company;
        */
        CriteriaBuilder crtBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CompanyDto> criteria = crtBuilder.createQuery(CompanyDto.class);
        Root<Company> company = criteria.from(Company.class);
        /*
        Через 'цепочку JOIN-ов' получаем:
        - user-ов связанных с конкретными company-ями;
        - у каждого user-а получаем его payment-ы;
        */
        MapJoin<Company,String,User> user = company.join(Company_.users, JoinType.INNER);
        ListJoin<User,Payment> payment = user.join(User_.payments);
        /*
        Метод *.construct(Class<Y> resultClass, Selection<?>... selections) -
        создает 'resultClass', соответствующий конструктору оного с набором
        его же полей см. oldboy/dto/CompanyDto.java.

        В данном случае CriteriaAPI через ReflectionAPI вызовет конструктор
        CompanyDto содержащий набор указанных нами через запятую параметров.
        */
        criteria.select(crtBuilder.construct(CompanyDto.class,
                        company.get(Company_.companyName),
                        crtBuilder.avg(payment.get(Payment_.amount)))).
                groupBy(company.get(Company_.companyName)).
                orderBy(crtBuilder.asc(company.get(Company_.companyName)));
        /*
        Сконструировали CompanyDto, определили параметры,
        сгруппировали, отсортировали - возвращаем.
        */
        return session.
                createQuery(criteria).
                list();
    }

    /**
    Возвращает список:
    - сотрудник (объект User),
    - средний размер выплат, но только для тех сотрудников, чей средний размер выплат
      больше среднего размера выплат всех сотрудников.
    Упорядоченный по имени сотрудника.
     */
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

        CriteriaAPI:
        */

        /* Получаем нужные классы основного запроса и первый корневой элемент */
        CriteriaBuilder crtBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = crtBuilder.createQuery(Tuple.class);
        Root<User> user = criteria.from(User.class);
        ListJoin<User, Payment> payment = user.join(User_.payments);
        /* Получаем дополнительные классы для подзапроса и второй корневой элемент */
        Subquery<Double> subQuery = criteria.subquery(Double.class);
        Root<Payment> paymentSubQuery = subQuery.from(Payment.class);

        criteria.
                select(crtBuilder.tuple(user, crtBuilder.avg(payment.get(Payment_.amount)))).
                groupBy(user.get(User_.userId)).
                having(crtBuilder.gt(crtBuilder.avg(payment.get(Payment_.amount)),
                        subQuery.select(crtBuilder.avg(paymentSubQuery.get(Payment_.amount))))).
                orderBy(crtBuilder.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstName)));

        return session.
                createQuery(criteria).
                list();
    }
}