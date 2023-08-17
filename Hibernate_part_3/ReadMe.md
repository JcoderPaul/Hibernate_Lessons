### Hibernate lessons part 3

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же Hibernate_part_1):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

#### Lesson 15 
Применение родительских классов и интерфейсов при связке сущностей:
- [volOne](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/src/main/java/oldboy/lesson_15_volOne): Пример применения маппинга Суперкласса (сущность 'хранитель сквозного ID'), аннотация @MappedSuperclass над абстрактным родительским классом. Сущности наследники и общие поля (см. [Driver.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_15_volOne/Entity_15_volOne/Driver.java) и [Porter.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_15_volOne/Entity_15_volOne/Porter.java)). SQL скрипты для таблиц БД - [DOC/SQL_scripts_lesson_15/VolumeOne](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/DOC/SQL_scripts_lesson_15/VolumeOne).
- [vplTwo](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/src/main/java/oldboy/lesson_15_volTwo): Пример применения маппинга Суперкласса (сущность 'аудитор') и интерфейса (см. [oldboy/entity/accessory/AuditableEntity.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/entity/accessory/AuditableEntity.java) и [oldboy/entity/accessory/BaseEntity.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/entity/accessory/BaseEntity.java). Каскадирование запросов в связных сущностях (см. [oldboy/lesson_15_volTwo/Entity_15_volTwo/DistributionCenter.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_15_volTwo/Entity_15_volTwo/DistributionCenter.java))

#### [Маппинг иерархий наследования ч.1 - Lesson 16](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/src/main/java/oldboy/lesson_16)
Вариант наследования Table_Per_Class. 

Особенность данного варианта - взаимосвязь нескольких несвязных таблиц БД по общим полям (столбцам) см. поля общего АБСТРАКТНОГО класса - [Employee.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_16/Entity_16/Employee.java). 

Класс [Employee.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_16/Entity_16/Employee.java) НЕ ИМЕЕТ СВОЕЙ таблицы в БД, но при этом логически дополняет дочерние классы нужными полями см. [Manager.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_16/Entity_16/Manager.java) и [Programmer.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_16/Entity_16/Programmer.java), которые имеют отличные друг от друга поля. Абстрактный класс родитель помечается специальной аннотацией @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) с указанием стратегии взаимосвязи (коих 3-и см. [InheritanceOptions.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/DOC/InheritanceOptions.jpg)). 

Основные хитрости при настройке классов, таблиц БД, последовательностей руками (пример для PostgreSQL):
- Расстановка соответствующих аннотаций над родительским классом, например: @Inheritance, @ENTITY;
- Если мы пользуемся аннотациями, то абстрактная родительская сущность НЕ ИМЕЕТ таблицы в БД и ЛИШАЕТСЯ аннотаций: @BUILDER, @TABLE;
- Родительская сущность над общим полем ID получает аннотации с соответствующими параметрами (см. [Employee.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_16/Entity_16/Employee.java)):
  - @GeneratedValue(generator = "..name..", strategy = GenerationType.SEQUENCE)
  - @SequenceGenerator((name = "..gen_name..", sequenceName = "...name_of_seq_in_base...", allocationSize = n)
- Для условной простоты настройки взаимосвязи (наследования) ключевое поле ID во всех сущностях (и таблицах), желательно назвать одинаково;
- Для связки всех сущностей (родителей и наследников) и таблиц БД необходимо создать отдельную последовательность (SEQUENCE) НЕ СВЯЗНУЮ НЕ С ОДНОЙ ТАБЛИЦЕЙ ЯВНО см. [lesson_16_create_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/DOC/SQL_scripts_lesson_16/lesson_16_create_tables.sql);
- И КОНЧНО не забываем прописать все необходимые сущности в [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/resources/hibernate.cfg.xml) или использовать configuration.addAnnotatedClass(Clazz.class) в утиллитном классе, если он есть (см. [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/Util/HibernateUtil.java)); 
- Остальные настройки могут зависеть от сложности взаимосвязи, в нашем примере все максимально упрощено.

#### [Маппинг иерархий наследования ч.2 - Lesson 17](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/src/main/java/oldboy/lesson_17)
Вариант наследования Single_Table.

Особенность данного варианта в том, что разные сущности НЕ ИМЕЮТ СВОЕЙ ОТДЕЛЬНОЙ таблицы в БД, они связаны родителем и их данные хранятся в одной общей таблице и они имеют единый сквозной ID. Естественно, эти сущности имеют поля с общим названием и смыслом см. поля родительского класса - [Worker.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_17/Entity_17/Worker.java).

Необходимо отметить, что в данном случае, родительский класс может быть, как абстрактным, так и обычным.

Особенности взаимодействия сущностей между собой и БД определяются аннотациями:
- У класса родителя @Inheritance(strategy = InheritanceType.SINGLE_TABLE) - определяем тип наследования;
- У класса родителя @DiscriminatorColumn(name = "type") - именно 'она определяет' в какое поле (или в поле с каким названием, у нас это - 'type') нашей единой таблицы работников - workers (см. [lesson_17_create_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/DOC/SQL_scripts_lesson_17/lesson_17_create_table.sql)) будет размещен 'функциональный определитель' нашего работника, естественно она работает в паре с другой аннотацией;
- У класса наследника @DiscriminatorValue("... name of type ...") содержит 'значение', название, value, которое будет помещено в таблицу БД в поле 'type';

Данный способ взаимосвязи сущностей между собой и БД считается наиболее простой и понятной для новичка, и особых хитростей при ручной настройке таковых, как например, с вариантом TABLE_PER_CLASS, тут вроде бы нет. И конечно же НЕ ЗАБЫВАЕМ прописать все необходимые сущности в [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/resources/hibernate.cfg.xml) или использовать configuration.addAnnotatedClass(Clazz.class) в утиллитном классе, если он есть (см. [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/Util/HibernateUtil.java)), а также, внимательно сопоставляем названия полей классов и полей в таблицах БД;

#### [Маппинг иерархий наследования ч.3 - Lesson 18](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3/src/main/java/oldboy/lesson_18)
Вариант наследования JOINED.

Основное достоинство такого типа наследования сущностей и построения взаимосвязи таблиц БД - это нормализация (каждая таблица БД содержит только те данные, которые ей принадлежат и ничего лишнего). 

И вроде бы, все в данном случае сконструировано по канонам SQL, однако, такой способ расстановки зависимостей влияет на производительность приложения которое будет обращаться к БД. Т.к. при добавлении, удалении, обновлении записей приходится обращаться к нескольким таблицам одновременно (т.е. формировать каскад запросов).

Особенности взаимодействия сущностей и настройки таблиц БД:
- У класса родителя параметр аннотации @Inheritance(strategy = InheritanceType.JOINED) см. [CarServiceSpecialist.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_18/Entity_18/CarServiceSpecialist.java);
- У класса наследника появляется аннотация @PrimaryKeyJoinColumn(name = "id"), параметр ссылается на поле родительского класса (см. [EngineRepairman.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_18/Entity_18/EngineRepairman.java) или [Tinman.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/src/main/java/oldboy/lesson_18/Entity_18/Tinman.java)).
- У таблицы подчиненного класса основной ключ, является внешним ключом на основной ключ родительской таблицы (см. [lesson_17_create_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_3/DOC/SQL_scripts_lesson_17/lesson_17_create_table.sql)).

