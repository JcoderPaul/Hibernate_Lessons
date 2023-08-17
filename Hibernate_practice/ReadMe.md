### Hibernate lessons part 12 - Practice

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же Hibernate_part_1):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На этот раз все манипуляции будем делать в новой БД (name base = 'part_ten_base' и schema = 'public')  
------------------------------------------------------------------------------------

### Hibernate - DAO and Repository
Если посмотреть на схему WEB-приложения см. [DOC/MVC.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/DOC/MVC.jpg), то видно, что Hibernate как минимум задействован на двух уровнях DAO и Service. Однако, между указанными слоями может находиться еще один Repository (явно на схеме не представлен), который дополняет слой DAO см. [DOC/RepositoryAndDAO.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/DOC/RepositoryAndDAO.txt).   


#### [Lesson 44](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/lesson_44)  
Нам понадобится Repository интерфейс с CRUD методами. Создадим пока простой класс, чтобы посмотреть, какой примерно функционал должны будут реализовывать классы подписанные на Repository. Этот класс будет рассчитанный на единичную сущность.
- [StudentRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/unrelated_entity_dao/StudentRepository.java) - простой класс с CRUD функционалом, работающий с сущностью Student (см. [oldboy/entity/unrelated_entity/Student.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/entity/unrelated_entity/Student.java));
- [SimpleDaoDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_44/SimpleDaoDemo.java) - микро-приложение для демонстрации работы простого DAO(Repository) класса (см. [oldboy/dao/unrelated_entity_dao/StudentRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/unrelated_entity_dao/StudentRepository.java)).

Ознакомившись с примерной работой DAO(Repository) функционала мы хотим унифицировать большинство методов в нашем Repository слое:
- [Repository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/Repository.java) - интерфейс содержащий наши CRUD методы.
- [SimpleRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/simple_repository_realisation/SimpleRepository.java) - абстрактный класс реализующий методы интерфейса Repository. Поскольку мы стремимся к унификации методов (естественно, каждый наследник нашего абстрактного класса может дополнить свой функционал методами необходимыми только для его работы) нам, естественно, приходится прибегнуть к помощи дженериков.
- [PaymentSimpleRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/simple_repository_realisation/PaymentSimpleRepository.java) - класс наследник абстракции SimpleRepository из которого хорошо видно, что имея набор сущностей (у нас Chat, Company, Payment, User, UserChat) мы очень просто можем реализовать легко изменяемый и дополняемый CRUD функционал, не связанный напрямую с БД.

Несложно заметить, что реализации методов в [StudentRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/unrelated_entity_dao/StudentRepository.java) и [SimpleRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/simple_repository_realisation/SimpleRepository.java) очень похожи, основное отличие - способ получения и обращения с объектом интерфейса Session. Причину см. в комментариях в соответствующих классах.

В случае с [StudentRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/unrelated_entity_dao/StudentRepository.java) - мы открывали и закрывали сессию внутри метода, что в качестве демонстрации работы CRUD принципов для единственной сущности несвязанной с другими приемлемо. Однако, для более сложно-связных сущностей мы будем получать исключения, и эту проблему нужно решать. 

Как вариант решения вышеописанной проблемы в методах [SimpleRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/simple_repository_realisation/SimpleRepository.java) используется метод *.getCurrentSession() для получения текущей сессии. Но для использования текущей сессии, нам необходимо указать Hibernate где получать текущую сессию - основной интерфейс который отвечает за это CurrentSessionContext.

Существует три основных контекста: 
- JTASessionContext, 
- ManagedSessionContext, 
- ThreadLocalSessionContext. 

В принципе нас устроят два последних, но мы выберем ThreadLocalSessionContext, т.к. он автоматически при открытии транзакции создает сессию и закрывает ее при коммите или роллбэке. ManagedSessionContext - этого не делает, т.е. при его использовании, как обычно, нам самим придется открывать (или получать) сессию перед открытием транзакции и закрывать ее самим же после коммита (роллбэка).

И так, мы решили использовать ThreadLocalSessionContext для этого нам необходимо дополнить файл конфигурации [hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/resources/hibernate.cfg.xml) (явно указать, какой контекст мы используем):
    
    <property name="hibernate.current_session_context_class">thread</property>

В наших [StudentRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/unrelated_entity_dao/StudentRepository.java) и [SimpleRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/simple_repository_realisation/SimpleRepository.java) объект SessionFactory добавляется как зависимость, а так делать нельзя, у нас должна быть зависимость на конкретный объект типа Session (например сама Session или EntityManager):

- [WithEntityManagerRepository.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/dao/WithEntityManagerRepository.java) - данный абстрактный класс реализует вышеописанную задумку - мы используем EntityManager.
- [EntityManagerDaoDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_44/EntityManagerDaoDemo.java) - демонстрирует работу класса CompanyRepository с использованием Proxy (см. комментарии).

#### [Lesson 45 ](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/lesson_45)
Теперь напишем классы соответствующие слоям приложения выше (DAO) Repository. Информация движется от пользователя web-приложения к БД и обратно, для этого нам нужно реализовать ряд классов, которые займутся такой передачей.
- [UserService.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/service/UserService.java) - реализация слоя 'service' см. [DOC/MVC.jpg](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/DOC/MVC.jpg) в котором могут быть методы: delete, findById, create и т.д

Мы уже реализовали слой DAO и необходимые классы для работы с БД. Теперь нам нужно передать пользователю ответ на запрос и принять некую информацию для того, чтобы поместить в БД. В обоих случаях информация может быть строго дозирована, в зависимости от прав пользователя. Таким преобразованием займутся классы Mapper:
- [Mapper.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/mapper/Mapper.java) - общий интерфейс для всех мапперов-преобразователей;
- [UserReadMapper.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/mapper/UserReadMapper.java) - преобразователь-маппер полученных из базы данных в приемлемый набор информации для передачи на слой выше Service (DAO в DTO), т.е. ответ на запрос пользователя;
- [UserCreateMapper.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/mapper/UserCreateMapper.java) - преобразователь-маппер пришедших от пользователя информации (со слоя Controller в слой Service) в данные для базы (DTO в DAO), т.е. нечто для сохранения в БД;  
- [dto](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/dto) - папка для наших Data Transfer Object классов;

Как данные читаются из БД и сохраняются в ней можно посмотреть в:
- [UserServiceFindMethod.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_45/UserServiceFindMethod.java) - чтение User-ов;
- [UserServiceCreateMethod.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_45/UserServiceCreateMethod.java) - сохранение User-ов в БД; 

#### [Lesson 46](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/lesson_46)
В обоих демонстрационных файлах рассмотренных выше (получение User из базы, внесение User в БД) мы руками открываем и закрываем транзакцию. 

Однако, на уровне сервисов этот процесс должен быть автоматизирован. Как вариант, мы можем использовать аннотацию @Transactional над теми методами классов Service, где эта автоматизация должна быть обеспечена, и библиотеку ByteBuddy для создания прокси объекта, в который будет передан перехватчик состояния транзакций. Созданный [TransactionInterceptor](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/interceptor/TransactionInterceptor.java) будет отслеживать открыта или нет транзакция.

Byte Buddy — это библиотека для создания классов Java во время выполнения программы (at runtime) Java.

- [TransactionInterceptor.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/interceptor/TransactionInterceptor.java) - перехватчик, который следит за открытием и закрытием транзакции при запуске методов Service классов.
- [ByteBuddyProxyDemo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_46/ByteBuddyProxyDemo.java) - демонстрация возможностей библиотеки ByteBuddy, по созданию прокси классов.

#### [Lesson 47](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/lesson_47)
Валидация в Hibernate JSR303 - Bean Validation - это спецификация Java API для проверки JavaBean в Java EE и Java SE. Проще говоря, она обеспечивает гарантии, что свойства наших классов (сущностей) JavaBean (ов) будут иметь правильные значения. Для его подключения нужно прописать зависимости в build.gradle:

    implementation 'org.hibernate:hibernate-validator:6.0.22.Final'
    implementation 'javax.el:javax.el-api:3.0.0'
    implementation 'org.glassfish:javax.el:3.0.0'

Документация: [https://beanvalidation.org/1.0/spec/](https://beanvalidation.org/1.0/spec/)

Данная спецификация строится на основе аннотаций и интерфейсов. Например, мы хотим гарантировать ввод данных по персональной информации в [PersonalInfo.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/entity/accessory/PersonalInfo.java) - день рождения всегда должен быть, для этого используется соответствующая аннотация @NotNull см. класс. 

Но поскольку данный класс связан с классом [User.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/entity/User.java), то и в нем необходимо аннотировать поле PersonalInfo, как @Valid, чтобы система зашла внутрь сущности и проверила наличие других аннотаций для валидации (см. [DOC/JSR303ShortAnnotation.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/DOC/JSR303ShortAnnotation.txt) и [jsr303Example](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_practice/src/main/java/oldboy/jsr303Example)).

Важный момент в том, что валидацию лучше проводить, еще на этапе передачи информации c уровня Controller, т.е. при работе с DTO, до обращения к Hibernate и формировании запросов к БД.

У нас есть возможность писать собственный валидатор. Для примера возьмем наш DTO класс и умышленно внесем ошибку в данные, и создадим тестовый класс [Member.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/jsr303Example/Member.java).
- [jsr303MyOwnValidator.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_practice/src/main/java/oldboy/lesson_47/jsr303MyOwnValidator.java) - микро-приложение для демонстрации собственных валидаторов.

