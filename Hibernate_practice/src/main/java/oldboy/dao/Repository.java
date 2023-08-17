package oldboy.dao;
/* Наш CRUD интерфейс см. DOC/RepositoryAndDAO.txt */
import oldboy.entity.accessory.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

/*
Он параметризован K - key сущности, E - сущность Entity,
наследующая от BaseEntity, так же параметризованная
ключом K (в обоих случаях, K это ID - сущности в БД)
*/
public interface Repository<K extends Serializable,
                            E extends BaseEntity<K>> {

    E save(E entity);

    void delete(K id);

    void update(E entity);
    /*
    Данный метод сделаем дефолтным, что бы не переопределять два
    одноименных метода у наследников. Так же нам нужно подправить
    наш абстрактный класс WithEntityManagerRepository.ValidationAPIUnit.java
    */
    default Optional<E> findById(K id) {
        return findById(id, emptyMap());
    }

    Optional<E> findById(K id, Map<String, Object> properties);

    List<E> findAll();
}
