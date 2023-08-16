package oldboy.Util;
/*
Выделим постоянно повторяющийся код в отдельный метод:
конфигурирование связи с БД, маппинг Entity сущностей,
возврат фабрики сессий.
*/
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Client;
import oldboy.entity.Profile;
import oldboy.entity.User;
import oldboy.lesson_10.MappingEntity.Firm;
import oldboy.lesson_10.MappingEntity.Worker;
import oldboy.lesson_11.MappingEntity.Address;
import oldboy.lesson_11.MappingEntity.Driver;
import oldboy.lesson_12.MappingEntity.SimpleChat.SimpleChat;
import oldboy.lesson_12.MappingEntity.SimpleChat.Talker;
import oldboy.lesson_12.VirtualParty.Jovial;
import oldboy.lesson_12.VirtualParty.JovialAndParty;
import oldboy.lesson_12.VirtualParty.VirtualParty;
import oldboy.lesson_13.MappingEntity.Book;
import oldboy.lesson_14.MappingEntity.Guys.DoubleAgent;
import oldboy.lesson_14.MappingEntity.SecretService;
import oldboy.lesson_14.MappingEntity.Guys.Spy;
import oldboy.lesson_14.MappingEntity.Guys.Traitor;
import oldboy.lesson_8.EntityDemo.Dean;
import oldboy.lesson_8.EntityDemo.Student;
import oldboy.lesson_8.EntityDemo.Teacher;
import oldboy.lesson_9.MappingEntity.Company;
import oldboy.lesson_9.MappingEntity.CompanyUser;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
Аннотация @UtilityСlass преобразует существующий класс в утилитный,
делая его окончательным и создавая приватный конструктор по умолчанию.
Она также изменяет существующий метод и переменные, делая их статическими.
*/
@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);

        /* Мапим наши классы раскиданные по разным папкам, незабываем об этом... */
        configuration.addAnnotatedClass(Student.class); // lesson_8/EntityDemo/Student.java
        configuration.addAnnotatedClass(Client.class); // oldboy/entity/Client.java
        configuration.addAnnotatedClass(Teacher.class); // lesson_8/EntityDemo/Teacher.java
        configuration.addAnnotatedClass(Dean.class); // lesson_8/EntityDemo/Dean.java

        configuration.addAnnotatedClass(Company.class); // lesson_9/MappingEntity/Company.java
        configuration.addAnnotatedClass(CompanyUser.class); // lesson_9/MappingEntity/CompanyUser.java

        configuration.addAnnotatedClass(Firm.class); // lesson_10/MappingEntity/Firm.java
        configuration.addAnnotatedClass(Worker.class); // lesson_10/MappingEntity/Worker.java

        configuration.addAnnotatedClass(Profile.class); // oldboy/entity/Profile.java

        configuration.addAnnotatedClass(Driver.class); // lesson_11/MappingEntity/Driver.java
        configuration.addAnnotatedClass(Address.class); // lesson_11/MappingEntity/Address.java

        configuration.addAnnotatedClass(Talker.class); // lesson_12/MappingEntity/Talker.java
        configuration.addAnnotatedClass(SimpleChat.class); // lesson_12/MappingEntity/Chat.java
        configuration.addAnnotatedClass(Jovial.class); // lesson_12/MappingEntity/Jovial.java
        configuration.addAnnotatedClass(VirtualParty.class); // lesson_12/MappingEntity/VirtualParty.java
        configuration.addAnnotatedClass(JovialAndParty.class); // lesson_12/MappingEntity/JovialAndParty.java

        configuration.addAnnotatedClass(Book.class); // lesson_13/MappingEntity/Book.java

        configuration.addAnnotatedClass(SecretService.class); // lesson_14/MappingEntity/SecretService.java
        configuration.addAnnotatedClass(Spy.class); // lesson_14/MappingEntity/Guys/Spy.java
        configuration.addAnnotatedClass(Traitor.class); // lesson_14/MappingEntity/Guys/Traitor.java
        configuration.addAnnotatedClass(DoubleAgent.class); // lesson_14/MappingEntity/Guys/DoubleAgent.java

        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
