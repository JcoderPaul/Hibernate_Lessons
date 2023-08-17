### Hibernate lessons part 9

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/resources/hibernate.cfg.xml) согласно настройкам БД.
------------------------------------------------------------------------------------

### Управление транзакциями в Hibernate
- CallBack в Hibernate - [DOC/CallBackInHibernate.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/CallBackInHibernate.txt);
- EventListener в Hibernate - [DOC/EventListener.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/EventListener.txt) ([DOC/EventListener.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/EventListener.jpg));
- Interceptor в Hibernate - [DOC/HibernateInterceptors.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/HibernateInterceptors.txt);

#### [Lesson 33 - CallBack](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9/src/main/java/oldboy/lesson_33)
Демонстрация работы 'обратных вызовов' - 'callbacks' (см. [DOC/CallBackInHibernate.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/CallBackInHibernate.txt)):
- [CallBacksDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/lesson_33/CallBacksDemo.java) - в данном случае обратные вызовы - методы выполняющие специальный функционал перехвата событий - принадлежат самой сущности (классу [MeetingRoom.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/MeetingRoom.java)) аннотированные @PrePersist и @PreUpdate (хотя их больше).

Результаты работы callback-ов отображаются в таблице 'meeting_rooms' в соответствующих полях (создание таблицы см. [DOC/SQL_Scripts/Make_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/SQL_Scripts/Make_tables.sql)) в виде даты и времени изменения.

#### [Lesson 34 - CallBackListener](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9/src/main/java/oldboy/lesson_34)
Демонстрация работы слушателей на базе обратных вызовов или CallBackListener-ы. В данном случае вся логика перехвата событий выносится в отдельный класс (см. [oldboy/listener/CreateUpdateListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/CreateUpdateListener.java)). 

Как и в случае, когда мы работали с callback-ми внутри класса, в случае выноса методов перехвата в отдельный класс (см.[oldboy/listener/CreateUpdateListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/CreateUpdateListener.java)) применяются те же аннотации, однако теперь мы 'универсальный и условно все событийный' слушатель прокидываем в качестве параметра в аннотации @EntityListeners абстрактного класса, от которого наследуются другие классы (сущности), событийный аудит которых мы планируем проводить (см. [AuditableEntity.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/accessory/AuditableEntity.java)), например сущность (см. [UserChat.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/UserChat.java)).

- [ListenerDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/lesson_34/ListenerDemo.java) - демонстрация работы слушателя обратных вызовов, мы создаем необходимые изменения в уже существующих записях БД и получаем соответствующие отклики-данные отданные CallBackListener-ом в полях 'created_at' и 'updated_at' таблицы 'users_chats';
- [CountListenerDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/lesson_34/CountListenerDemo.java) - демонстрация работы счетчика на базе CallBackListener-ов (при добавлении / удалении User-a в / из Chat-a счетчик в таблице 'chat' будет реагировать сообразно действию). 

В данном случае подразумевается следующий механизм: у нас уже существует таблица фиксирующая список чатов - 'chats', мы добавляем в нее поле 'count' для подсчета количества участников каждого чата (см. [DOC/SQL_Scripts/Make_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/SQL_Scripts/Make_tables.sql)). 
  
При этом мы не используем простой вызов списка участников 'userChats' и извлечение его размера методом *.size(). Мы используем механизм предоставленный JavaPersistenceAPI и знакомые нам аннотации:
  - @EntityListeners - аннотируем сущность UserChat, которая связанна с сущностью Chat и передаем в качестве параметра аннотации UserChatListener.class - наш слушатель событий (см. [UserChatListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/UserChatListener.java));
  - @PostPersist - аннотация отмечающая метод, который выполняется после операции сохранения сущности (в нашем случае код метода, при наступлении события, увеличивает счетчик и вносим данные в таблицу 'chat' нашей БД);
  - @PreRemove - аннотация отмечающая метод, который запускается перед операцией удаления или ее каскадным вариантом (в нашем случае код метода, при наступлении события, уменьшает счетчик и вносим данные в таблицу 'chat' нашей БД);

См. классы [Chat.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/Chat.java), [UserChat.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/UserChat.java) и [UserChatListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/UserChatListener.java) более подробно.

#### [Lesson 35 - EventListener](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9/src/main/java/oldboy/lesson_35)
Пример применения EventListener-ов - универсальных слушателей за всеми событиями в нашем приложении. 
- [AuditTableEventListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/lesson_35/AuditTableEventListener.java) - в данном примере, мы создали и внесли новую сущность Chat в БД, затем выбрали запись из таблицы 'chat' и удалил ее. Эти действия привели к реакции нашего класса [AuditTableListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/AuditTableListener.java) и его методов. 
- [AuditTableListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/AuditTableListener.java) - наш EventListener - класс реализующий методы интерфейсов PreDeleteEventListener, PreInsertEventListener. 

Он переопределяет только два метода: onPreDelete и onPreInsert, из названий которых понятно, на какие события они должны реагировать, нам лишь необходимо переопределить логику действий.

Для реализации всей логики работы механизма EventListener-а нам понадобится:

- [oldboy/entity/audit/Audit.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/audit/Audit.java) - класс (сущность) содержащий данные о том: 
  - над какой сущностью провели операцию;
  - какого рода совершили изменения (удалили, сохранили, изменили, создали);
  - содержание самих изменений (контент);
- таблица в которой будет фиксироваться весь аудит событий 'audit' см. [DOC/SQL_Scripts/Make_tables.sql](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/DOC/SQL_Scripts/Make_tables.sql);
- провести необходимые настройки в [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/Util/HibernateUtil.java) для регистрации нашего [AuditTableListener.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/listener/AuditTableListener.java), чтобы поместить его слушателей в соответствующие группы EventTypeListener;


    SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);

	EventListenerRegistry listenerRegistry = sessionFactoryImpl.
                                    getServiceRegistry().
                                    getService(EventListenerRegistry.class);

    AuditTableListener auditTableListener = new AuditTableListener();

    listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
    listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);

- там же в нашем [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/Util/HibernateUtil.java) мы должны добавить наш [Audit.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/entity/audit/Audit.java) как сущность; 

#### [Lesson 36 - Interceptors](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_9/src/main/java/oldboy/lesson_36)
Простая демонстрация создания и работы пользовательского 'перехватчика' событий - Interceptor-a.
- [GlobalInterceptor.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/interceptor/GlobalInterceptor.java) - наш 'самописный' перехватчик.
- [InterceptorDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/lesson_36/InterceptorDemo.java) - демонстрация нашего перехватчика (просто выводит на экран сообщение в том месте, где в работу внедряется interceptor)

Основная особенность - как и EventListener, Interceptor нужно регистрировать, а вернее передать в сессию, метод *.setInterceptor() в нашем [HibernateUtil.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_9/src/main/java/oldboy/Util/HibernateUtil.java).
