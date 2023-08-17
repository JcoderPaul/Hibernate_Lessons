package oldboy.dao;

import oldboy.entity.Company;

import javax.persistence.EntityManager;

/*
Поскольку абстрактный класс RepositoryBase<K extends Serializable,
                                           E extends BaseEntity<K>>
параметризован как <K - key, E - Entity>, наш DAO для всех сущностей
и в частности для Company будет выглядеть так, как приведено ниже, см.
класс Company, где companyId - Integer, ну а сама сущность естественно
Company.

Естественно, что наш класс Company должен имплементировать BaseEntity<K>,
как и любой другой класс, который будет иметь DAO ...Repository реализацию.

По аналогии будут выстроены остальные DAO классы, для оставшихся
сущностей проекта. Естественно дополнительный функционал, т.е.
специфические методы для каждой конкретной сущности добавляется
по мере необходимости.
*/
public class CompanyRepository extends WithEntityManagerRepository<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}
