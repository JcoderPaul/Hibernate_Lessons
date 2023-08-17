### Hibernate lessons part 8

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

### Управление транзакциями в Hibernate
Установку уровня изолированности транзакций можно реализовать через файл свойств: [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/resources/hibernate.cfg.xml)

    <property name="hibernate.connection.isolation">2</property>

Всего их 4 - и, как мы уже знаем см. [DOC/TransactionIsolationLevel.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/DOC/TransactionIsolationLevel.txt) (для PostgreSQL default = 2):
- 1 - TRANSACTION_READ_UNCOMMITTED;
- 2 - TRANSACTION_READ_COMMITTED;
- 4 - TRANSACTION_REPEATABLE_READ;
- 8 - TRANSACTION_SERIALIZABLE;

#### [Lesson 28](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/src/main/java/oldboy/lesson_28)
- [TransactionsDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_28/TransactionsDemo.java) - пример классического обращения с транзакциями (все что рассмотрено ранее).

#### [Lesson 29](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/src/main/java/oldboy/lesson_29)
- [VersionOptimisticLock.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_29/VersionOptimisticLock.java) и [Worker.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/entity/Worker.java) - пример применения аннотаций @OptimisticLocking с параметром type = OptimisticLockType.VERSION и @Version, для контроля изменений в сущности в ходе транзакций.
- [ExceptionEmulation.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_29/ExceptionEmulation.java) - пример генерации исключения при одновременном изменении одной записи в БД из двух сессий при использовании параметров LockModeType.OPTIMISTIC в запросе и OptimisticLockType.VERSION при настройке сущности (кто первый встал того и тапки).
- [OptimisticLockTypeAll.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_29/OptimisticLockTypeAll.java) - применение аннотации @OptimisticLocking(type = OptimisticLockType.ALL) над классом и отличие взаимодействия Hibernate - a с сущностями аннотированными подобным образом и нет (методика формирования динамических запросов).

#### [Lesson 30](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/src/main/java/oldboy/lesson_30)
- [PessimisticLockDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_30/PessimisticLockDemo.java) - демонстрация работы PessimisticLock, в данном случае сущность с которой производят манипуляции в основном коде приложения, И ТОЛЬКО В НЕМ, НЕ ИМЕЕТ специальных аннотаций и дополнительных настроек. Все управление процессом формирования транзакций ПРОИСХОДИТ В ОСНОВНОМ КОДЕ ПРИЛОЖЕНИЯ и нигде более (см. параметры: LockModeType.PESSIMISTIC_READ и LockModeType.PESSIMISTIC_WRITE метода *.find(), а так же [DOC/Row-Level-Lock-Modes.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/DOC/Row-Level-Lock-Modes.txt)).  

#### [Lesson 31](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/src/main/java/oldboy/lesson_31)
Управление уровнем изолированности транзакций из основного кода приложения:
- [ReadOnlyLockDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_31/ReadOnlyLockDemo.java) - пример использования сессионных методов *.setDefaultReadOnly() и *.setReadOnly() для контроля над возможностью изменения записей в БД.
- [SetReadOnlyHQLQuery.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_31/SetReadOnlyHQLQuery.java) - пример использования метода *.setReadOnly() в HQL запросах.
- [ReadOnlyNativeQuery.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_31/ReadOnlyNativeQuery.java) - пример использования нативных запросов к БД для установки ограничений в самой базе. 

#### [Lesson 32](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8/src/main/java/oldboy/lesson_32)
- [NonTransactionalDataAccess.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/src/main/java/oldboy/lesson_32/NonTransactionalDataAccess.java) - пример использования безтранзакционных запросов к БД (См. [DOC/NonTransactionalDataAccess.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_8/DOC/NonTransactionalDataAccess.txt)).
