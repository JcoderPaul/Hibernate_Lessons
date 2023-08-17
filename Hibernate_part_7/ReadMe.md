### Hibernate lessons part 7

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7/DOC/SQL_Scripts) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

### Проблема n + 1

Проблема N + 1 не специфична для JPA и Hibernate, с ней вы можете столкнуться и при использовании других технологий доступа к данным.

Проблема N + 1 возникает, когда фреймворк доступа к данным выполняет N дополнительных SQL-запросов для получения тех же данных, которые можно получить при выполнении одного SQL-запроса.

Чем больше значение N, тем больше запросов будет выполнено и тем больше влияние на производительность. 
Проблема заключается в выполнении множества дополнительных запросов, которые в сумме выполняются уже существенное время, влияющее на быстродействие.

Проблема n + 1 может возникнуть в случае, когда одна сущность (таблица) ссылается на другую сущность (таблицу).

В такой ситуации получается, что для получения значения зависимой сущности выполняется n избыточных запросов, в то время как достаточно одного.

Никого не нужно убеждать, что это негативно влияет на производительность системы и создает ненужную нагрузку на базу данных. Особенно то, что количество запросов увеличивается с ростом n.

Сама проблема часто представляется как возникающая только в отношениях “один ко многим” (javax.persistence.OneToMany) или только в случае ленивой загрузки данных (javax.persistence.FetchType.LAZY). Это не так, и следует помнить, что эта проблема также может возникнуть в отношениях один-к-одному и при “жадной” загрузке зависимых сущностей.

Ниже рассмотрены варианты решения проблемы N + 1 (очень плохие, плохие и хорошие):

#### [Lesson 24](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7/src/main/java/oldboy/lesson_24)
Решение N+1 проблемы в данном разделе состоит в применении аннотаций @BatchSize и @Fetch, над полями и классами сущностей. К сожалению, понять и почувствовать эффект (увидеть в консоли) от работы данных аннотаций (вместе и по отдельности) можно только при поочередном применении их (см. [BatchSizeAndFetchDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_24/BatchSizeAndFetchDemo.java) и [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/entity/User.java)).

Однако, аннотация @Fetch хоть и решает проблему N+1, но в очень узком диапазоне задач, например, как в нашем случае работа с коллекцией внутри сущности. Другие методы ее использования будут менее эффективны. 

!!! Так же мы получаем интересный эффект при настройке аннотации @Fetch на формирование подзапроса (FetchMode.SUBSELECT) - это формирование оного в любом случае, даже, когда он нам и не нужен. Т.е. мы всегда получаем аннотированную @Fetch коллекцию (см. 'payments' в [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/entity/User.java)) отдельным подзапросом, хотя и не делали этого специально !!!    
 
#### [Lesson 25](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7/src/main/java/oldboy/lesson_25)
Применение fetch внутри запросов (как вариант решения проблемы N + 1):
- [UseFetchInsideHqlQueryDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_25/UseFetchInsideHqlQueryDemo.java) - методика применения fetch в классических HQL запросах;
- [UseFetchInCriteriaAPIDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_25/UseFetchInCriteriaAPIDemo.java) - методика использования fetch в CriteriaAPI запросах;
- [UseFetchInQueryDslDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_25/UseFetchInQueryDslDemo.java) - методика применения fetch в QueryDSL;

#### [Lesson 26](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7/src/main/java/oldboy/lesson_26)
Аннотация @FetchProfile (см. [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/entity/User.java)) как относительно эффективный способ решения проблемы N + 1. Демонстрация применения и особенности см. [FetchProfilesDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_26/FetchProfilesDemo.java).

#### [Lesson 27](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7/src/main/java/oldboy/lesson_27)
Применение EntityGraph как наиболее эффективного и универсального способа борьбы с каскадом запросов и проблемой N + 1:

- Применение аннотации @NamedEntityGraph, настройка и особенности (см. [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/entity/User.java)) применение в работе см. [EntityGraphDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_27/EntityGraphDemo.java).
- Построение EntityGraph средствами класса реализующего интерфейс Session, настройка и использование см. [ProgramEntityGraphDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_7/src/main/java/oldboy/lesson_27/ProgramEntityGraphDemo.java). 

### Основные рекомендации по решению проблемы N + 1

- По возможности избегать One-To-One BiDirectional связи сущностей, тем более если одна из них обладает синтетическим ключом;
- При возможности, всегда и везде использовать FETCH TYPE LAZY;
- Не стоит надеяться (предпочитать) на абсолютную эффективность @BatchSize и @Fetch;
- Лучше использовать ключевое слово fetch (метод) внутри запросов как в HQL, так и в CriteriaAPI и QueryDSL (ограничения - отсутствие данной возможности при getById);
- Рекомендуется использовать средства EntityGraph API (позволяет вынести весь нужный код во внешний утиллитный класс и многое другое), реже @FetchProfile (т.к. настройка параметров аннотации может быть весьма емкой, т.е. сравнимой с количеством строк кода в самой сущности);