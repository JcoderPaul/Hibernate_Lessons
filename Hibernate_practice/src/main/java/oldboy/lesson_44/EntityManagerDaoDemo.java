package oldboy.lesson_44;

import oldboy.Util.HibernateUtil;
import oldboy.dao.CompanyRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;

public class EntityManagerDaoDemo {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /*
            Классический вариант просто передать объект Session в наш CompanyRepository()
            в данном 'однопоточном' варианте сработает. Однако, в многопоточном формате
            мы получим проблемы. ThreadLocal не будет работать если мы используем
            неблокирующие вызовы (используя EntityManager мы используем его). Значит с
            многопоточкой будут вопросы, когда обращение к одному объекту идет из разных
            потоков.

            Решением для многопоточки будет использовать Proxy:
            */
            Session session =
                    (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                                                                new Class[]{Session.class},
                    (proxy, method, sessArgs) ->
                               method.invoke(sessionFactory.getCurrentSession(),sessArgs));

            session.beginTransaction();
            /*
            И тогда создав единственный экземпляр CompanyRepository
            мы сможем обращаться к его функционалу из разных потоков.
            */
            CompanyRepository companyRepository = new CompanyRepository(session);
            companyRepository.findById(1).ifPresent(System.out::println);

            session.getTransaction().commit();
        }
    }
}
