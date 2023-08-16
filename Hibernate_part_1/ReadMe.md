### Hibernate lessons part 1

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку:
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - создаем первую таблицу (см. [DOC/users_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/DOC/users_table.sql)).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/resources/hibernate.cfg.xml) согласно настройкам БД.
------------------------------------------------------------------------------------

#### Lesson 1
- Создание POJO (Entity) объекта (см. [oldboy/entity/User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/entity/User.java)). Расстановка всех необходимых аннотаций (@Entity, @Table, @Id, @Column, @Enumerated), варианты mapping-а сущностей тремя способами, рассмотрены два (метод *.addAnnotatedClass() и конфигурирование [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/resources/hibernate.cfg.xml)). Создание соединения с БД, подготовка данных, отправка данных (см. [oldboy/lesson_1/HibernateDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_1/HibernateDemo.java));
- Рассмотрение, упрощенное, того как Hibernate взаимодействует с объектами POJO и добавляет данные в БД (см. [src\test\java\oldboy\lesson_1\HibernateDemoTest.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/test/java/oldboy/lesson_1/HibernateDemoTest.java)).

#### Lesson 2
Бывают ситуации когда Hibernate не знает, как конвертировать одни данные в другие при взаимодействии с БД. Для этого придуманы интерфейсы позволяющие пользователю самому писать подобные конверторы. 

Так же может возникнуть ситуация когда мы хотим внести в БД (в одно из полей записи), некие свои 'кастомные' типы данных, у нас в примере это JSON объект, что-нибудь типа:

    {
    "query": "Виктор Иван",
    "count": 7
    }

- Пример создания и интеграции собственного класса ([oldboy/entity/Birthday.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/entity/Birthday.java)) в Entity объекты ([oldboy/entity/Client.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/entity/Client.java)) при помощи конвертора, аннотации @Convert и @Converter. Работа метода *.addAttributeConverter(), методы автоприменения самописных конверторов при помощи параметра самого метода и при помощи аннотации над классом конвертором (см. [oldboy/converter/BirthdayConverter.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/converter/BirthdayConverter.java));
- Пример добавления пользовательских типов данных в БД. Существует несколько интерфейсов являющихся базовыми точками расширения hibernate-функциональности: UserType, CompositeUserType, UserCollectionType, EnhancedUserType, UserVersionType, ParametrizedType. Мы рассмотрим использование UserType из сторонней библиотеки (com.vladmihalcea:hibernate-types-52). Пример аннотаций  @Type, @TypeDef (см. [oldboy/entity/Client.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/entity/Client.java));
- Пример регистрации самописных типов в Hibernate, метод *.registerTypeOverride() см.[ oldboy/lesson_2/HibernateUserTypeDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_2/HibernateUserTypeDemo.java).

#### Lesson 3
Пример использования методов *.update() и *.saveOrUpdate() см. комментарии в [oldboy/lesson_3/HibernateUpdate.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_3/HibernateUpdate.java).
#### Lesson 4
Пример использования методов *.del() и *.get() см. комментарии в [oldboy/lesson_4/HibernateDelAndGet.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_4/HibernateDelAndGet.java).
#### Lesson 5
Исследование сессий (см. [SessionFactory.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/DOC/SessionFactory.jpg)). Любой запрос к БД помещает сущность в Persistence Context из которого мы можем ее удалить, работа методов *.evict(), *.clear() см [HibernateCashFirstLevel.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_5/HibernateCashFirstLavel.java). Состояние сессии и Entity объектов после закрытия сессии методы *.close() и *.getStatistics() см. [CashFirstLevelVarTwo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_5/CashFirstLavelVarTwo.java) и Persistence Context см. [Sessions.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/DOC/Sessions.jpg).
#### Lesson 6
Жизненный цикл сущностей в Hibernate см. [LifeCycleOfEntity.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/lesson_6/EntityLifeCycle.java). Создадим утилитный класс для работы с БД (выделим рутинный код) см. [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_1/src/main/java/oldboy/Util/HibernateUtil.java). 