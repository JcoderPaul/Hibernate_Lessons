### Hibernate lessons part 11

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На этот раз все манипуляции будем делать в новой БД (name base = 'part_ten_base' и schema = 'public')  
------------------------------------------------------------------------------------

### Hibernate - Second Level Cache
Одним из преимуществ уровней абстракции базы данных, таких как фреймворки ORM (объектно-реляционное сопоставление), является их способность прозрачно кэшировать данные, извлеченные из базового хранилища. Это помогает устранить затраты на доступ к базе данных для часто используемых данных.

Прирост производительности может быть значительным, если коэффициенты чтения/записи кэшированного содержимого высоки. Это особенно верно для сущностей, которые состоят из больших графов объектов, более подробно см. [DOC/SecondLevelCache.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/SecondLevelCache.txt).

Для подгрузки зависимостей в [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/build.gradle) добавляем:
    
    implementation 'org.hibernate:hibernate-jcache:5.6.15.Final'
    implementation 'org.ehcache:ehcache:3.9.7'

в файл [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/hibernate.cfg.xml):

    <property name="hibernate.cache.use_second_level_cache">true</property>
    <property name="hibernate.cache.region.factory_class">org.hibernate.cache.jcache.internal.JCacheRegionFactory</property>

#### [Lesson 40 - Second Level Cache](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11/src/main/java/oldboy/lesson_40)
После проведения предварительных настроек по подключению провайдера кэша второго уровня мы пробуем аннотировать наш первый класс User как кэшируемый - @Cache. 

Пока только его и посмотрим что будет: 

- [SecondLevelCacheDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/lesson_40/SecondLevelCacheDemo.java) - демонстрирует в консоли (в дебаге) как ведет себя единственная закешированная сущность, вернее как ведет себя Hibernate при обращении к единственной закешированной сущности при наличии у той незакешированных связанных с ней других сущностей (например, User и company, userchat).

Мы можем закешировать сущность аннотировав ее:

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public class User implements Comparable<User>, 
                                 BaseEntity<Long> {
    }

Так же мы можем аннотировать поля коллекций в той же сущности User: 

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<UserChat> userChats = new ArrayList<>();

При этом, важный момент заключается в том, что простое аннотирование коллекции (см. [DOC/OnlyCollectionsCached.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/OnlyCollectionsCached.jpg)) внутри сущности без аннотирования самой коллекционируемой сущности (или связанной @OneToMany) приведет к ухудшению производительности приложения за счет дополнительных запросов к БД.

Условно идеальным вариантом будет кешировать и коллекцию и коллекционируемые сущности см. [DOC/CorrectEntityCaching.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/CorrectEntityCaching.jpg). 

#### [Lesson 41](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11/src/main/java/oldboy/lesson_41) 
На данном этапе мы аннотировали необходимые сущности и поля, чтобы уменьшит количество запросов к БД.
- [AnnotatedAllEntitiesCollections.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/lesson_41/AnnotatedAllEntitiesCollections.java) - демонстрирует поведение Hibernate при использовании кеша второго уровня для связных сущностей и полей коллекций. См. описание в [DOC/SecondLevelCache.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/SecondLevelCache.txt) и самом демонстрационном микро-приложении (и связных классах [User](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/entity/User.java), [UserChat](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/entity/UserChat.java), [Company](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/entity/Company.java)).   


#### [Lesson 42 - Caching Regions](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11/src/main/java/oldboy/lesson_42)

--------------------------------------------------------------------------------------------------------------
Повторимся: Кэш второго уровня предназначен для уменьшения объема необходимого доступа к БД. Он находится между нашим приложением и БД, чтобы избежать частого обращения к базе данных.

При обращении к объектам из приложения (см. [DOC/SecondLevelCache.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/SecondLevelCache.jpg)):
- они сначала будут искаться в кеше первого уровня текущей (вызывающей) сессии;
- если требуемые сущности не будут найдены (в persistence context текущей сессии), будет запущен запрос к базе данных;
- затем полученные объекты будут сохранены в поставщике кеша (cache provider).

Доступны несколько вариантов кэширования (см. [DOC/SecondLevelCache.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/SecondLevelCache.txt)), но лучше кэшировать данные только для чтения или исходя из логики вашего приложения.

Нужно помнить, что кеши не знают об изменениях, внесенных в БД другим приложением. Однако их можно настроить на регулярное 'истечение срока действия' - 'тайминг' кэшированных данных.

*** Caching Regions - области кеширования ***

КЕШ ВТОРОГО УРОВНЯ НЕ ХРАНИТ ЭКЗЕМПЛЯРЫ ОБЪЕКТОВ (СУЩНОСТЕЙ), вместо этого ОН КЕШИРУЕТ ТОЛЬКО ИДЕНТИФИКАТОР ОБЪЕКТА (entity identifier) И ЗНАЧЕНИЕ (карту key - value, при этом value - СЕРИАЛИЗОВАННЫЙ ОБЪЕКТ) см. [DOC/StructureSecondLevelCache.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/StructureSecondLevelCache.jpg). КАЖДЫЙ класс сущности (Entity), ассоциативная коллекция (collection association) и запрос (Query) имеют свою область (Region), где хранятся значения каждого экземпляра (values of each instance).

Caching Regions (области кэширования) — это определенные области поставщика кеша (cache provider), в которых могут храниться сущности (entities), коллекции (collections) или запросы (queries) (см. [DOC/CorrectEntityCaching.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/CorrectEntityCaching.jpg)). Каждая область кэша (cache region) находится в определенном пространстве имен кэша и имеет собственную конфигурацию времени жизни (lifetime).

Нужно помнить, что при кэшировании коллекции (collections) и запросов (queries) сохраняются только идентификаторы. Значения объекта будут храниться в своем собственном регионе (region) см. [DOC/StructureSecondLevelCache.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/StructureSecondLevelCache.jpg).

--------------------------------------------------------------------------------------------------------------

- [SecondLevelCacheRegions.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/lesson_42/SecondLevelCacheRegions.java) - в данном коде мы изучаем некоторые аспекты жизни CACHE REGION и сущностей в нем сохраненных. Имена regions данные системой автоматически см. [DOC/CacheRegion/SecondLevelCacheRegions.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/CacheRegion/SecondLevelCacheRegions.jpg). Имена regions заданные нами через аннотации @Cache над основными сущностями см. [DOC/CacheRegion/ShortNamesRegions.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/CacheRegion/ShortNamesRegions.jpg).

Для настройки особых параметров для каждого region кеша второго уровня создается файл см. [resources/ehcache-config.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/ehcache-config.xml).

Далее мы прописываем наш конфигурационный файл для кеша второго уровня в настройки нашего Hibernate через см. [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/hibernate.cfg.xml)

    <property name="hibernate.javax.cache.uri">/ehcache-config.xml</property>

#### [Lesson 43 - Query Cache](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11/src/main/java/oldboy/lesson_43)
Часто используемые запросы тоже можно кешировать как это сказано выше и рассмотрено в [DOC/SecondLevelCache.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/SecondLevelCache.txt) структура см. [DOC/QueryCache.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/DOC/QueryCache.jpg). 

Мы явно должны указать системе, что используем кеш второго уровня для запросов в [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/hibernate.cfg.xml):

    <property name="hibernate.cache.use_query_cache">true</property>

Также одновременно в данном примере мы подключим и посмотрим на статистику работы кеша второго уровня. Для этого в [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/resources/hibernate.cfg.xml) добавим еще свойство:

    <property name="hibernate.generate_statistics">true</property>

Естественно сбор любой статистики замедляем работу приложения и рекомендуется использовать данное свойство только на этапе тестирования.

- [SecondLevelQueryCache.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_11/src/main/java/oldboy/lesson_43/SecondLevelQueryCache.java) - пример кеширования запросов.



