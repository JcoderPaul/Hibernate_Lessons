package oldboy.dao;
/*
Создав абстрактный класс с определенным CRUD функционалом
упрощает создание классов из слоя (DAO) Repository
*/
import oldboy.entity.User;

import javax.persistence.EntityManager;

public class UserRepository extends WithEntityManagerRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    /* Функционал зависящий от конкретной сущности добавляется по мере необходимости */
}
