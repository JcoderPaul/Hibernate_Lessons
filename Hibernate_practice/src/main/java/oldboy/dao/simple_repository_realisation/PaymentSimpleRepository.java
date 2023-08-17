package oldboy.dao.simple_repository_realisation;

import oldboy.entity.Payment;
import org.hibernate.SessionFactory;
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
public class PaymentSimpleRepository extends SimpleRepository<Long, Payment> {

    public PaymentSimpleRepository(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
