### Hibernate lessons part 6

[В папке DOC](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_6/DOC) sql-скрипты и др. полезные файлы.

------------------------------------------------------------------------------------
Для начала проведем предварительную подготовку (см. так же [Hibernate_part_1](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_1)):
- Шаг 1 - в файле [build.gradle](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/build.gradle) добавим необходимые нам зависимости (с указанием этапов разработки, на которых нам может понадобиться та или иная библиотека):
    - подключим Hibernate;
    - подключим Postgresql;
    - подключим Lombok;
    - на этапе создания проекта IDE сама подключила Junit 5;
- Шаг 2 - устанавливаем и подключаем БД (PostgreSQL или MySql, у меня стоят и работают обе).
- Шаг 3 - необходимые таблицы созданы и заполнены (см. SQL скрипты в DOC).
- Шаг 4 - сконфигурируем наш [resources/hibernate.cfg.xml](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/resources/hibernate.cfg.xml) согласно настройкам БД. На это раз все сущности будем добавлять именно сюда.
------------------------------------------------------------------------------------

Для изучения QueryDSL, кроме уже присутствующих, нам нужны дополнительные плагины, конфигурации, задачи (tasks) и зависимости в build.gradle:

    sourceSets {
    main {
        java {
            srcDirs += "$buildDir/generated/sources/annotationProcessor/java/main"
        }
      }
    }
    configurations {
      querydsl.extendsFrom implementation, runtimeOnly, compileOnly
    }    

    plugins {
      id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
    }

    dependencies {
        implementation 'com.querydsl:querydsl-jpa:5.0.0'
        annotationProcessor 'com.querydsl:querydsl-apt:5.0.0'

        implementation 'javax.annotation:javax.annotation-api:1.3.2'
    }

    querydsl {
      jpa = true
      querydslSourcesDir = "$buildDir/generated/sources/annotationProcessor/java/main"
    }
    
    compileQuerydsl {
      options.annotationProcessorPath = configurations.querydsl
    }

    compileQuerydsl.dependsOn(clean)

- Источники: https://plugins.gradle.org/plugin/com.ewerk.gradle.plugins.querydsl
- Git исходники: https://github.com/ewerk/gradle-plugins
------------------------------------------------------------------------------------

Перед изучением особенностей библиотеки QueryDsl билдим средствами Gradle нужные ресурсы в папку: ...\Hibernate_part_6\build\generated\sources\annotationProcessor\java\main и видим файлы начинающиеся с буквы 'Q'. 

Это полный аналог того что делала библиотека JpaModelGen, только там сгенерированные библиотекой файлы заканчивались на '_' - нижним подчеркиванием.

#### [Lesson 22 - QueryDSL](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_6/src/main/java/oldboy/lesson_22)
Практическое применение QueryDSL запросов различной сложности:
- [UserDaoWithQueryDsl](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/UserDaoWithQueryDsl.java) - класс позволяющий взаимодействовать с БД посредствам заранее прописанных методов (содержащих QueryDSL методы);
  - Создание менеджера сущностей JPAQuery средствами QueryDSL;
  - Классические, SQL-одноименные, методы *.select(), *.from(), *.orderBy(), *.where(), *.avg(), *.orderBy(), *.having() в QueryDSL;
  - Применение alias - псевдонимов в QueryDSL методах;
  - Отличие финализирующего метода *.fetch() от *.fetchOne();
  - Использование 'Tuple - картежей' в запросах QueryDSL запросах;
  - Пример использования DTO классов при работе с QueryDSL;
- [DaoWithQueryDslOne](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/lesson_22/DaoWithQueryDslOne.java) - пример тестирования первых 4-х методов [UserDaoWithQueryDsl](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/UserDaoWithQueryDsl.java);
- [DaoWithQueryDslTwo](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/lesson_22/DaoWithQueryDslTwo.java) - пример тестирования оставшихся методов [UserDaoWithQueryDsl](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/UserDaoWithQueryDsl.java);
- [UserDaoTest](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/test/java/oldboy/dao/UserDaoTest.java) - классический TEST класс, для проверки работоспособности методов [UserDaoWithQueryDsl](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/UserDaoWithQueryDsl.java), через тестирование (Junit 5, AsserJ);

#### [Lesson 23 - Фильтрация в QueryDSL](https://github.com/JcoderPaul/Hibernate_Lessons/tree/master/Hibernate_part_6/src/main/java/oldboy/lesson_23)
Практическое использование Predicate фильтров в QueryDSL запросах (два последних метода в классе [UserDaoWithQueryDsl.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/UserDaoWithQueryDsl.java)):
- метод public Double findAveragePaymentAmountByNamesFilterHardCodePredicates(Session session, PaymentFilter payFilter) - пример классического IF с Predicate в теле самого метода;
- метод public Double findAveragePaymentAmountByNamesFilterPredicatesMethods(Session session, PaymentFilter payFilter) - пример выноса условий фильтрации в отдельный класс (см. [QPredicate.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/QPredicate.java));

[QPredicate.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/dao/QPredicate.java) - пример выделения отдельного класса для фильтрации нужных нам параметров запроса (использование списка предикатов, интерфейс Function, класс ExpressionUtils библиотеки com.querydsl.core.types):
- [Package_java_util_function.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/DOC/FunctionPackageDoc/Package_java_util_function.txt) - краткое описание пакета java.util.function;
- [InterfaceFunction.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/DOC/FunctionPackageDoc/InterfaceFunction.txt) - краткое описание интерфейса Function;
- [InterfacePredicate.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/DOC/FunctionPackageDoc/InterfacePredicate.txt) - краткое описание интерфейса Predicate;
- [ExpressionUtilsClass.txt](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/DOC/QueryDslCoreTypes/ExpressionUtilsClass.txt) - краткое описание класса ExpressionUtils;

[DaoWithQueryDslThree.java](https://github.com/JcoderPaul/Hibernate_Lessons/blob/master/Hibernate_part_6/src/main/java/oldboy/lesson_23/DaoWithQueryDslThree.java) - пример использования приведенных выше методов (тестирование в работе). 




