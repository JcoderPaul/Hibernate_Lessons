### Hibernate lessons part 5

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_5/DOC) sql-скрипты и др. полезные файлы (документация, ссылки, jpg-схемы).

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

#### [Lesson 21 - Criteria API](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_5/src/main/java/oldboy/lesson_21)
Практическое применение [Criteria API](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaAPIShortView.txt) запросов различной сложности:
- [UserDaoWithCriteriaAPI](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/main/java/oldboy/dao/UserDaoWithCriteriaAPI.java) - класс позволяющий взаимодействовать с БД посредствам заранее прописанных методов (содержащих Criteria API методы); 
  - Создание объектов [CriteriaBuilder](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaBuilderInterface.txt), [CriteriaQuery](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaQueryInterface.txt), [Root](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/RootInterface.txt) и их значение в создании запроса оп средствам Criteria API (см. [CriteriaAPIShortView.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaAPIShortView.txt));
  - Классические одноименные методы *.select(), *.orderBy(), *.where(), *.avg(), *.orderBy(), *.having() в Criteria API (см. [CriteriaBuilderInterface.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaBuilderInterface.txt) и [CriteriaQueryInterface.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/CriteriaQueryInterface.txt));
  - Использование 'Tuple - картежей' в запросах (см. [TupleInterface.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/TupleInterface.txt) и ознакомительная статья [Tuple.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/DOC/Tuple.txt)), а также получение информации из кортежей при тестировании;
  - Пример использования DTO класса при работе с Criteria API запросами (см. [CompanyDto.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/main/java/oldboy/dto/CompanyDto.java));
- [DaoWithCriteriaOne](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/main/java/oldboy/lesson_21/DaoWithCriteriaOne.java) - пример тестирования первых 4-х методов UserDaoWithCriteriaAPI;
- [DaoWithCriteriaTwo](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/main/java/oldboy/lesson_21/DaoWithCriteriaTwo.java) - пример тестирования оставшихся методов UserDaoWithCriteriaAPI;
- [UserDaoTest](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_5/src/test/java/oldboy/dao/UserDaoTest.java) - классический TEST класс, для проверки работоспособности методов UserDaoWithCriteriaAPI, через тестирование (Junit 5, AsserJ);