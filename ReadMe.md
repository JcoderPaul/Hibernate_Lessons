### [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)
Конфигурирование hibernate.cfg.xml, подключение и настройка зависимостей.

Конфигурация SessionFactory, класс Session, понятие POJO (Entity), типы данных (пользовательские типы данных).

Применение методов: *.update(), *.saveOrUpdate(), *.del(), *.get(), *.evict(), *.clear(), *.close(), *.getStatistics().

Применение аннотаций: @Entity, @Table, @Id, @Column, @Enumerated, @Type, @TypeDef.

Жизненный цикл сущности.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_2](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_2)
Логирование процессов в приложении. Уровни логирования, настройка и подключение логеров.

Встраиваемые классы, применение аннотаций: @Embeddable, @Embedded и @EmbeddedId.

Варианты генерации основных ключей: Identity, Sequences, Table.

Маппинг сущностей (отношения): 
- ManyToOne - аннотации @ManyToOne и @JoinColumn;
- OneToMany - аннотации @OneToMany, @Cascade, @ToString, @EqualsAndHashCode;
- OneToOne - аннотация @OneToOne;
- ManyToMany - аннотация @ManyToMany, @JoinTable.

Применение аннотаций: @ElementCollection и @CollectionTable

Примеры сортировки сущностей. Применение аннотаций: @OrderBy, @OrderColumn, @SortNatural, @MapKey.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_3](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_3)
Применение родительских классов и интерфейсов при связке сущностей. 

Аннотация @MappedSuperclass.

Маппинг иерархий наследования:
- Table_Per_Class - специфика применения аннотаций при таком варианте наследования: @Inheritance, @Entity, @Builder, 
                                                                                    @Table, @GeneratedValue, 
                                                                                    @SequenceGenerator
- Single_Table - специфика применения аннотаций при таком варианте наследования: @Inheritance, @DiscriminatorColumn, 
                                                                                 @DiscriminatorValue
- Joined - специфика применения аннотаций при таком варианте наследования: @Inheritance, @PrimaryKeyJoinColumn

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_4](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_4)
Работа с HQL запросами, параметризованные запросы, применение позиционных и именованных параметров, применение 
нескольких именованных параметров при использовании 'JOIN', применение именованных запросов, применение 
классических SQL запросов в Hibernate.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_5](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_5)
Практическое применение Criteria API. 

Создание объектов: CriteriaBuilder, CriteriaQuery, Root.

Работа методов: *.select(), *.orderBy(), *.where(), *.avg(), *.orderBy(), *.having(). 

Использование 'Tuple - картежей' в запросах.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_6](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_6)
Практическое применение QueryDSL.

Создание менеджера сущностей JPAQuery средствами QueryDSL.

SQL-одноименные, методы: *.select(), *.from(), *.orderBy(), *.where(), *.avg(), *.orderBy(), *.having().

Применение alias - псевдонимов, отличие методов *.fetch(), *.fetchOne().

Практическое использование Predicate фильтров в QueryDSL запросах. Использование списка предикатов, интерфейс Function, 
класс ExpressionUtils библиотеки com.querydsl.core.types.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_7](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_7)
Проблема N + 1.

Решение N+1 проблемы, применение аннотаций: @BatchSize, @Fetch, @FetchProfile. 

Применение EntityGraph для решения проблемы N+1, применение аннотаций: @NamedEntityGraph.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_8](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_8)
Управление транзакциями в Hibernate. 

Уровни изолированности транзакций.

Применение аннотаций: @OptimisticLocking, @Version.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_9](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9)
Перехват транзакций в Hibernate: 
- обратные вызовы (CallBack);
- слушатели (Listeners) обратных вызовов (CallBack Listeners) и событий (Event Listeners);
- перехватчики (Interceptors).

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_10](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_10)
Hibernate Envers - аудирование записей (отслеживание изменений записей в БД).

Применение аннотации: @Audited, @NotAudited, @RevisionEntity, @RevisionNumber, @RevisionTimestamp.

Машина времени в Hibernate - AuditReaderFactory.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_part_11](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_11)
Hibernate - Second Level Cache. 

Настройка и подключение провайдеров кеша второго уровня.

Особенности применения аннотации: @Cache

Области кеширования. Кеширование сущностей. Кеширование запросов. 
Настройка времени жизни сущности в кеше второго уровня.

-----------------------------------------------------------------------------------------------------------------------
### [Hibernate_practice](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice)
Практическая работа с Hibernate, валидирование сущностей.