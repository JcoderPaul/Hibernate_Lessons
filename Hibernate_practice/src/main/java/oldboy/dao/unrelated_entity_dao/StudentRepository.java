package oldboy.dao.unrelated_entity_dao;
/*
Вот так мог бы выглядеть DAO(Repository) класс
для ни с чем несвязной единственной сущности.
*/
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import oldboy.entity.unrelated_entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

/*
@RequiredArgsConstructor - генерирует конструктор с 1 параметром для каждого поля,
которое требует специальной обработки. Все неинициализированные final поля получают
параметр, также как все остальные поля, помеченные @NonNull, которые не иницилизированы
при объявлении. Для этих случаев также генерируется явная проверка на null.

Конструктор бросает исключение NullPointerException, если какой-либо из параметров,
предназначенный для полей, помеченных @NonNull содержит null. Порядок этих параметров
совпадает с порядком появления полей в классе.
*/
@RequiredArgsConstructor
public class StudentRepository {
    private final SessionFactory sessionFactory;

    public Student save(Student entity) {
        /*
        @Cleanup - обеспечивает автоматическую очистку данного ресурса до того,
                   как путь выполнения кода покинет текущую область видимости.

                   Мы можем аннотировать таким образом любую объявлен локальную
                   переменную. Например:
                   *************************************************************
                   @Cleanup InputStream in = new FileInputStream("some/file");
                   *************************************************************
                   В результате в конце области, в которой мы находимся, будет
                   вызван метод - in.close(). Этот вызов гарантированно будет
                   выполнен посредством конструкции try/finally.
        */
        @Cleanup Session session = sessionFactory.openSession();
        session.save(entity);
        return entity;
    }

    public void delete(Long id) {
        @Cleanup Session session = sessionFactory.openSession();
        session.delete(session.find(Student.class, id));
        session.flush();
    }


    public void update(Student entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.merge(entity);
    }


    public Optional<Student> findById(Long id) {
        @Cleanup Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(Student.class, id));
    }


    public List<Student> findAll() {
        @Cleanup Session session = sessionFactory.openSession();
        CriteriaQuery<Student> criteria = session.getCriteriaBuilder().createQuery(Student.class);
        criteria.from(Student.class);

        return session.createQuery(criteria)
                .getResultList();
    }
}