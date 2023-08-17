package oldboy.lesson_45;

import oldboy.Util.HibernateUtil;
import oldboy.dao.CompanyRepository;
import oldboy.dao.UserRepository;
import oldboy.dto.UserCreateDto;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
import oldboy.mapper.CompanyReadMapper;
import oldboy.mapper.UserCreateMapper;
import oldboy.mapper.UserReadMapper;
import oldboy.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;
import java.time.LocalDate;

import static java.time.Month.OCTOBER;

public class UserServiceCreateMethod {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session =
                    (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                            new Class[]{Session.class},
                            (proxy, method, sessArgs) ->
                                    method.invoke(sessionFactory.getCurrentSession(),sessArgs));

                session.beginTransaction();

                var companyReadMapper = new CompanyReadMapper();
                var userReadMapper = new UserReadMapper(companyReadMapper);

                var companyRepository = new CompanyRepository(session);
                var userCreateMapper = new UserCreateMapper(companyRepository);

                var userRepository = new UserRepository(session);

                var userService = new UserService(userRepository, userReadMapper, userCreateMapper);

                UserCreateDto userCreateDto = new UserCreateDto(
                            PersonalInfo.builder().
                                    firstName("Hicaru").
                                    lastName("Sulu").
                                    birthDate(LocalDate.of(2167, OCTOBER, 14)).
                                    build(),
                            "suludriver@enterprise.star",
                            null,
                            Role.USER,
                            1
                );

                userService.create(userCreateDto);

                session.getTransaction().commit();

        }
    }
}
