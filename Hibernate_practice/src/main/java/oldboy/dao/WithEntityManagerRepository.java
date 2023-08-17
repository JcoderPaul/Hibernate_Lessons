package oldboy.dao;
/* Используем EntityManager в отличии от SimpleRepository.ValidationAPIUnit.java */
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import oldboy.entity.accessory.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class WithEntityManagerRepository<K extends Serializable,
                                                  E extends BaseEntity<K>>
                                                         implements Repository<K, E> {
    /*
    Мы не можем из дженерика получить класс, а значит в методах *.findByAll, *.findById,
    поэтому укажем наш 'условно универсальный класс' в качестве поля. А для работы с ним -
    формирование запросов - в этих методах, используем CriteriaAPI (см. Hibernate_part_5
    данного репозитария).
    */
    private final Class<E> clazz;
    /*
    Чтобы иметь возможность обратиться к методам EntityManager
    из наследников класса WithEntityManagerRepository, например
    UserRepository в методе *.findById() класса UserService,
    добавим аннотацию @Getter
    */
    @Getter
    private final EntityManager entityManager;

    @Override
    public E save(E entity) {
        /* Метод *.save() меняется на: */
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        entityManager.remove(entityManager.find(clazz, id));
        entityManager.flush();
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(K id, Map<String, Object> properties) {
        return Optional.ofNullable(entityManager.find(clazz, id, properties));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = entityManager.
                                          getCriteriaBuilder().
                                          createQuery(clazz);
        criteria.from(clazz);

        return entityManager.createQuery(criteria)
                .getResultList();
    }
}