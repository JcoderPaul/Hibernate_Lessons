package oldboy.dao.simple_repository_realisation;
/*
DAO класс 'единственной ни с чем несвязной' сущности понятен -
StudentRepository.ValidationAPIUnit.java. Однако, в реальной базе данных все не
так и связей много, используем абстрактный класс для того, чтобы
унифицировать работу с остальными сущностями (уменьшить количество
повторяющегося кода).
*/
import lombok.RequiredArgsConstructor;
import oldboy.dao.Repository;
import oldboy.entity.accessory.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
public abstract class SimpleRepository<K extends Serializable,
                                       E extends BaseEntity<K>>
                                              implements Repository<K, E> {
    /*
    Мы не можем из дженерика получить класс, а значит в
    методах *.findByAll, *.findById, поэтому укажем наш
    'условно универсальный класс' в качестве поля. А для
    работы с ним - формирование запросов - в этих методах,
    используем CriteriaAPI (см. Hibernate_part_5 данного
    репозитария).

    Можно было реализовать класс *.getEntity() или
    используя рефлексию вытащить класс переданный
    в дженерике 'E'.
    */
    private final Class<E> clazz;
    private final SessionFactory sessionFactory;

    @Override
    public E save(E entity) {
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

        И вот тут возникает проблемка - сессия после каждого вызова CRUD метода
        будет тут же закрываться и дополнительные запросы по другим связным
        сущностям не будут проходить, а значит мы получим -

        Exception in thread "main" org.hibernate.LazyInitializationException

        @Cleanup Session session = sessionFactory.openSession();

        Поэтому код применяемый в StudentRepository.ValidationAPIUnit.java поменяем на:
        */
        Session session = sessionFactory.getCurrentSession();
        session.save(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.find(clazz, id));
        session.flush();
    }

    @Override
    public void update(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id, Map<String, Object> properties) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.find(clazz, id, properties));
    }

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria)
                .getResultList();
    }
}