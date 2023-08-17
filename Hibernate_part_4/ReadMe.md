### Hibernate lessons part 4

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_4/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле build.gradle добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

#### [Lesson 19](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_4/src/main/java/oldboy/lesson_19)
Примеры работы с HQL запросами:
- [HQL_DemoOne](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_DemoOne.java) - сравнение классического SQL запроса и HQL (применение alias) без параметров; 
- [HQL_DemoTwo](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_DemoTwo.java) - применение параметризированного запроса *.createQuery("... HQL query ... ", Clazz.class);
- [HQL_Named_Param](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_Named_Param.java) - применение позиционных и именованных параметров в HQL запросе (типа):
  - *.createQuery("select u from Clazz u where u.info = ?1", Clazz.class).setParameter(position: 1, value of u.info = "...");
  - *.createQuery("select u from Clazz as u where u.info = :findInfo", Clazz.class).setParameter("findInfo", value of u.info = "...");
- [HQL_Entity_Mapping](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_Entity_Mapping.java) - применение нескольких именованных параметров при использовании 'JOIN', сортировка результатов запросов 'ORDER BY';
- [HQL_Named_Query](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_Named_Query.java) - применение именованных запросов типа *.createNamedQuery("... NameOfQuery ...", Clazz.class), а так же аннотации @NamedQuery (см. oldboy/lesson_19/entity_19/Employee.java);  
- [HQL_Native_Query](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_19/HQL_Native_Query.java) - применение классического SQL запроса в Hibernate, метод *.createNativeQuery("...classic SQL query "). Пример параметризированного нативного HQL запроса.

#### [Lesson 20](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_4/src/main/java/oldboy/lesson_20)
Практическое применение HQL запросов различной сложности:
- [UserDao](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/dao/UserDao.java) - класс позволяющий взаимодействовать с БД посредствам заранее прописанных методов (содержащих HQL запросы);
- [PracticeTestDaoOne](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_20/PracticeTestDaoOne.java) - пример тестирования первых 4-х методов UserDao;
- [PracticeTestDaoTwo](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/main/java/oldboy/lesson_20/PracticeTestDaoTwo.java) - пример тестирования оставшихся методов UserDao;
- [UserDaoTest](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_4/src/test/java/oldboy/dao/UserDaoTest.java) - классический TEST класс, для проверки работоспособности методов UserDao, через тестирование (Junit 5, AsserJ);