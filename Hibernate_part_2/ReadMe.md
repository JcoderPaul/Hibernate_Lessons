### Hibernate lessons part 2

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле build.gradle добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы сделаны в предыдущих уроках (см. [DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1/DOC)).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/resources/hibernate.cfg.xml) согласно настройкам БД.
------------------------------------------------------------------------------------

#### [Lesson 7](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_7)
Применение логирования процессов, см. [Slf4j_and_Log4j.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Slf4j_and_Log4j.txt). Уровни логирования см. [LogLevels.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/LogLevels.jpg). Настройка и подключение логгеров к проекту см. [resources/log4j.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/resources/log4j.xml) и [LoggingDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_7/LoggingDemo.java). 

#### [Lesson 8](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_8)
- Применение аннотаций @Embeddable и @Embedded (см. [DOC/HibernateEmbeddableEmbedded.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/HibernateEmbeddableEmbedded.txt)). Практическая реализация встраиваемого класса и его 'встройка' см. [PersonalInfo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/entity/accessory/PersonalInfo.java) и [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/entity/User.java).
- Варианты генерации ключей в БД:
  - Identity - см. [Client.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/entity/Client.java) (это самый распространенный способ генерации ключей в БД).
  - Sequences - см. [GeneratorIdBySequences.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/GeneratorIdBySequences.java) и [Student.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/EntityDemo/Student.java) ([student_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_8_SQL_Scripts/student_table.sql));
  - Table - см. [GeneratorIdByTable.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/GeneratorIdByTable.java) и [Teacher.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/EntityDemo/Teacher.java) ([teacher_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_8_SQL_Scripts/teacher_table.sql));
  - @EmbeddedId - пример сложносоставного ключа см. [EmbeddedIdDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/EmbeddedIdDemo.java) и [Dean.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_8/EntityDemo/Dean.java) ([deans_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_8_SQL_Scripts/deans_table.sql)). 

#### [Lesson 9](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_9)
Пример отношения ManyToOne. Аннотации @ManyToOne и @JoinColumn в классе [CompanyUser.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_9/MappingEntity/CompanyUser.java). 

В данном примере показана зависимость одной таблицы company_users, от другой companies. Т.е. если в таблице companies нет ни одной записи, то мы вряд ли сможем добавить запись в таблицу company_users, т.к. в БД существует связь с внешним ключом.

#### [Lesson 10](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_10)
Пример отношения OneToMany. Аннотации @OneToMany и @Cascade, особенности такой взаимосвязи и аннотации @ToString(exclude = "...") и @EqualsAndHashCode(exclude = "...") при запросе сущностей из БД, см. [Firm.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_10/MappingEntity/Firm.java), [Worker.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_10/MappingEntity/Worker.java) и [OneToManyAddData.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_10/OneToManyAddData.java).  

#### [Lesson 11](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_11)
Пример отношения OneToOne. Аннотация @OneToOne. 

Пример нежелательного метода связки сущностей ([ShouldNotBeUsedMapping.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_11/ShouldNotBeUsedMapping.java)), когда внешний ключ в одной таблице БД и ее же ID - есть ID другой таблицы см. [сlient_profile.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_11_SQL_Scripts/%D1%81lient_profile.sql), [Profile.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/entity/Profile.java) и [Client.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/entity/Client.java).

Пример предпочтительного метода связки сущностей ([CorrectUseMapping.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_11/CorrectUseMapping.java)), когда в обеих таблицах БД ключ ID формируется автоматически самой БД, см. [Address.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_11/MappingEntity/Address.java), [Driver.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_11/MappingEntity/Driver.java) и [client_address.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_11_SQL_Scripts/client_address.sql), [drivers_table.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/DOC/Lasson_11_SQL_Scripts/drivers_table.sql).  

#### [Lesson 12](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_12)
Пример отношения ManyToMany. Аннотация @ManyToMany. Связь через таблицу, аннотация @JoinTable см. [Talker.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_12/MappingEntity/SimpleChat/Talker.java), маппинг на основную сущность см. [SimpleChat.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_12/MappingEntity/SimpleChat/SimpleChat.java). Пример взаимодействия см. [ManyToManySimple.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_12/ManyToManySimple.java). 

Так же в качестве примера рассмотрен вариант с объединяющей таблицей с авто-генерируемым ID см. папку VirtualParty и [ManyToManyVirtualParty.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_12/ManyToManyVirtualParty.java).

#### [Lesson 13](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_13)
Пример применения аннотаций @ElementCollection и @CollectionTable над полем класса, помеченным как @Entity. Аннотация @Embeddable.

#### [Lesson 14](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2/src/main/java/oldboy/lesson_14)
Примеры сортировки сущностей. Применение аннотаций:
- @OrderBy - см. пример [lesson_10/MappingEntity/Firm.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_10/MappingEntity/Firm.java) и [OrderByAnnotation.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/OrderByAnnotation.java);
- @OrderColumn - см. пример [lesson_14/MappingEntity/SecretService.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MappingEntity/SecretService.java) и [OrderColumnAnnotation.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/OrderColumnAnnotation.java);
- @SortNatural - применение implComparable см. пример [lesson_14/MappingEntity/SecretService.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MappingEntity/SecretService.java), [lesson_14/MappingEntity/Traitor.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MappingEntity/Guys/Traitor.java) и [SortNaturalAnnotation.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/SortNaturalAnnotation.java);
- @MapKey - см. пример [lesson_14/MappingEntity/SecretService.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MappingEntity/SecretService.java), [lesson_14/MappingEntity/Guys/DoubleAgent.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MappingEntity/Guys/DoubleAgent.java) и [MapKeyAnnotation.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_2/src/main/java/oldboy/lesson_14/MapKeyAnnotation.java).
