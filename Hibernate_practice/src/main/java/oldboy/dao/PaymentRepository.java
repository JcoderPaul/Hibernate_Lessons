package oldboy.dao;

import com.querydsl.jpa.impl.JPAQuery;
import oldboy.entity.Payment;

import javax.persistence.EntityManager;
import java.util.List;

import static oldboy.entity.QPayment.payment;

public class PaymentRepository extends WithEntityManagerRepository<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }

    public List<Payment> findAllByReceiverId(Long receiverId) {
        return new JPAQuery<Payment>(getEntityManager()).
                select(payment).
                from(payment).
                where(payment.receiver.userId.eq(receiverId)).
                fetch();
    }
}