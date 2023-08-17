### Hibernate lessons part 10

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_10/DOC/SQL_Script) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На этот раз все манипуляции будем делать в новой БД (name base = 'part_ten_base' и schema = 'public')  
------------------------------------------------------------------------------------

В enterprise-разработке часто бывает необходимо отслеживать процесс редактирования каких-нибудь таблиц в БД.
Например, процесс редактирования документ в системе. Внесли данные паспорта в БД, потом кто-то поменял ему серию, затем номер, после сменил тип с 'паспорт РФ' на 'паспорт Зимбабве'. Возникает потребность отследить историю изменений и, скажем, настучать по шапке пользователю, который совершил ошибку или сознательно внес вредоносные изменения.

### Hibernate Envers - аудирование записей (отслеживание изменений записей в БД)  
Для работы с Hibernate Envers нам нужно подгрузить следующую зависимость (лучше, чтобы версия HibernateCore и Envers совпадали) в [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/build.gradle):
      
    implementation 'org.hibernate:hibernate-envers:5.6.15.Final'

Поскольку в процессе работы функционал Enver-a создает дополнительные таблицы, для аудита состояния наших сущностей, в настройках [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/resources/hibernate.cfg.xml) необходимо добавить:

    <property name="hibernate.hbm2ddl.auto">update</property>

Данная настройка позволит обновлять состояние базы при необходимости и соответственно позволит средствам Envers-a создавать и обновлять таблицы для аудита сущностей. 

#### [Lesson 37 - аннотации @Audited и @NotAudited.](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_10/src/main/java/oldboy/lesson_37)
@Audited - аннотация, ставится над классом. Состояние помеченной данным образом сущности будет отслеживаться и все изменения фиксироваться в соответствующих таблицах.

Например, если аннотировать таким образом класс (см. [Payment.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/entity/Payment.java)), то при внесении изменений через Hibernate приложение (см. [HibEnversDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/lesson_37/HibEnversDemo.java)), в соответствующих таблицах созданных средствами Envers-a (см. БД: 'payments_aud' и 'revinfo') мы увидим их.

Поскольку, практически все наши сущности связанны друг с другом, и в частности Payment имеет поле 'receiver' связанное с User, возникает вопрос - нужно или нет фиксировать изменения в этой сущности. 

Если мы не хотим использовать эту связь для фиксации изменений (аудирования) применяется аннотация - @NotAudited (см. [Payment.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/entity/Payment.java) или [Company.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/entity/Company.java)). Обычно эта аннотация ставится над коллекциями внутри сущностей.

Существует еще вариант, как не проводить аудит связных сущностей - это параметр в аннотации @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) и тогда аннотацию @NotAudited можно опустить. 

Но все определяется логикой нашего приложения, т.е. расстановку соответствующих аннотаций на практике придется тщательно продумывать.

#### [Lesson 38 - аннотации класса @RevisionEntity, и полей @RevisionNumber,@RevisionTimestamp](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_10/src/main/java/oldboy/lesson_38)
Создание собственной таблицы для фиксации изменений (аналог Revinfo в Hibernate Envers).

В ситуации когда мы хотим иметь некую видоизмененную таблицу 'revinfo' с дополнительными полями и т.п. Мы должны создать собственную сущность для обработки результатов работы Hibernate Envers и снабдить ее нужным функционалом. Однако тут есть ряд обязательных условий:
- Созданная сущность помечается, как @RevisionEntity (И ТАКАЯ СУЩНОСТЬ В ПРОЕКТЕ МОЖЕТ БЫТЬ ОДНА);
- У созданного класса должно присутствовать поле помеченное @RevisionNumber (ОБЯЗАТЕЛЬНО);
- У созданного класса должно присутствовать поле помеченное @RevisionTimestamp (ОБЯЗАТЕЛЬНО);
У нас это см. [RevisionRecorder.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/entity/revtable/RevisionRecorder.java). В базе данных мы создаем таблицу 'revision_records' см. [DOC/SQL_Script/Make_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/DOC/SQL_Script/Make_tables.sql);

Так же в данном классе ([RevisionRecorder.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/entity/revtable/RevisionRecorder.java)) добавлено поле в которое, 'по идее', должны вноситься данные о пользователе, который сделал изменения в записи БД. Для безоговорочной реакции на действия с помеченной @Audited сущностью мы написали слушатель наследующий от RevisionListener и переопределили метод newRevision (см. [MyFirstRevisionListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/listeners/MyFirstRevisionListener.java)). Теперь при каждом изменении интересующей нас сущности в таблице 'revision_records' будет появляться соответствующая запись о пользователе (хотя он у нас один, но тут показывается принцип, а не чудеса кодинга и архитектуры).

Естественно не забываем в [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/Util/HibernateUtil.java) добавить наш класс в конфигурацию Hibernate: configuration.addAnnotatedClass(RevisionRecorder.class)

[MyRevisionRecorderDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/lesson_38/MyRevisionRecorderDemo.java) - микро-приложение для демонстрации работы нашего самописного RevisionRecorder-а (все запросы от Hibernate Envers можно наблюдать из консоли, а записи в БД)  

#### [Lesson 39 - Машина времени AuditReaderFactory](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_10/src/main/java/oldboy/lesson_39)
Hibernate Envers предоставляет нам возможность не просто складировать изменения сущностей (записей) в соответствующие таблицы (..._AUD) БД. НО И ЗАПРАШИВАТЬ исторические данные аналогично запросу данных через Hibernate Criteria API. 

Мы можем получить доступ к истории аудита объекта с помощью интерфейса AuditReader , который мы можем получить с помощью открытого EntityManager или Session через AuditReaderFactory:
    
    AuditReader reader = AuditReaderFactory.get(session);

Envers предоставляет AuditQueryCreator (возвращенный AuditReader.createQuery() ) для создания запросов, специфичных для аудита. Следующая строка вернет все экземпляры сущности Bar, измененные в ревизии № 2 (где bar_AUDIT_LOG.REV = 2 ):

    AuditQuery query = reader.
                  createQuery().
                  forEntitiesAtRevision(Bar.class, 2);

Вот как мы можем запросить ревизии Bar. Это приведет к получению списка всех проверенных экземпляров Bar во всех их состояниях:

    AuditQuery query = reader.
                  createQuery().
                  forRevisionsOfEntity(Bar.class, true, true);

Если второй параметр равен false, результат объединяется с таблицей REVINFO . В противном случае возвращаются только экземпляры сущности. Последний параметр указывает, следует ли возвращать удаленные экземпляры Bar .

Затем мы можем указать ограничения, используя фабричный класс AuditEntity :

    query.addOrder(AuditEntity.revisionNumber().desc());

Практическое применение см. [AuditTimeTravelDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_10/src/main/java/oldboy/lesson_39/AuditTimeTravelDemo.java)

(Более подробно см. https://docs.jboss.org/hibernate/envers/3.6/reference/en-US/html/)