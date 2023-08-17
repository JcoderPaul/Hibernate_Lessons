package oldboy.lesson_45;

import oldboy.Util.HibernateUtil;
import oldboy.dao.CompanyRepository;
import oldboy.dao.UserRepository;
import oldboy.mapper.CompanyReadMapper;
import oldboy.mapper.UserCreateMapper;
import oldboy.mapper.UserReadMapper;
import oldboy.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.lang.reflect.Proxy;


public class UserServiceFindMethod {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session =
                    (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                            new Class[]{Session.class},
                            (proxy, method, sessArgs) ->
                                    method.invoke(sessionFactory.getCurrentSession(),sessArgs));

                //session.beginTransaction();

                var companyReadMapper = new CompanyReadMapper();
                var userReadMapper = new UserReadMapper(companyReadMapper);

                var companyRepository = new CompanyRepository(session);
                var userCreateMapper = new UserCreateMapper(companyRepository);

                var userRepository = new UserRepository(session);

                var userService = new UserService(userRepository, userReadMapper, userCreateMapper);

                userService.findById(1L).ifPresent(System.out::println);

                //session.getTransaction().commit();

        }
    }
}
