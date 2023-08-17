package oldboy.lesson_46;
/*
Использование библиотеки ByteBuddy для создания экземпляра класса.
Применение перехватчика для отслеживания состояния транзакций, а
так же для открытия оной и закрытия ее.
*/
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import oldboy.Util.HibernateUtil;
import oldboy.dao.CompanyRepository;
import oldboy.dao.UserRepository;
import oldboy.dto.UserCreateDto;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
import oldboy.interceptor.TransactionInterceptor;
import oldboy.mapper.CompanyReadMapper;
import oldboy.mapper.UserCreateMapper;
import oldboy.mapper.UserReadMapper;
import oldboy.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;


public class ByteBuddyProxyDemo {

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException,
                                                  InvocationTargetException,
                                                  InstantiationException,
                                                  IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session =
                    (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                            new Class[]{Session.class},
                            (proxy, method, sessArgs) ->
                                    method.invoke(sessionFactory.getCurrentSession(),sessArgs));

                var companyReadMapper = new CompanyReadMapper();
                var userReadMapper = new UserReadMapper(companyReadMapper);

                var companyRepository = new CompanyRepository(session);
                var userCreateMapper = new UserCreateMapper(companyRepository);

                var userRepository = new UserRepository(session);

                var transactionInterceptor = new TransactionInterceptor(sessionFactory);
                /* Создаем наследника UserService (это новый класс, еще раз - наследник) и мы его делаем на лету */
                var userService = new ByteBuddy()
                    /* Создаем подкласс */
                    .subclass(UserService.class)
                    /* Проверяем все ли методы класса аннотированы @Transactional */
                    .method(ElementMatchers.any())
                    /* Все перехваты наших методов делегируются нашему TransactionInterceptor */
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    /* Поскольку динамически создается новый класс, мы должны явно создать и загрузить в JVM */
                    .make()
                    /* И вот мы загружаем его в память */
                    .load(UserService.class.getClassLoader())
                    /* Теперь получаем наш новый класс, который расширяет наш UserService */
                    .getLoaded()
                    /* Вызываем конструктор наследника с параметрами, что и у родителя */
                    .getDeclaredConstructor(UserRepository.class,
                                            UserReadMapper.class,
                                            UserCreateMapper.class)
                    /* Создаем новый экземпляр объекта - очень похоже на new UserService() */
                    .newInstance(userRepository,
                                 userReadMapper,
                                 userCreateMapper);
                /* Теперь применяем этот созданный класс для получения данных и внесения их в БД */
                userService.findById(1L).ifPresent(System.out::println);

                UserCreateDto userCreateDto = new UserCreateDto(
                        PersonalInfo.builder()
                                .firstName("Malcolm")
                                .lastName("Stone")
                                .birthDate(LocalDate.of(934, 11, 21))
                                .build(),
                        "melstone@swordwing.sky",
                        null,
                        Role.ADMIN,
                        2
                );

                userService.create(userCreateDto);
        }
    }
}
