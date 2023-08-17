package oldboy.service;
/*
В данном классе будут все необходимые зависимости
для взаимодействия класса User с текущим слоем
приложения - service
*/
import lombok.RequiredArgsConstructor;
import oldboy.dao.UserRepository;
import oldboy.dto.UserCreateDto;
import oldboy.dto.UserReadDto;
import oldboy.entity.User;
import oldboy.mapper.Mapper;
import oldboy.mapper.UserCreateMapper;
import oldboy.mapper.UserReadMapper;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

/*
@RequiredArgsConstructor - генерирует конструктор с 1 параметром для каждого поля,
которое требует специальной обработки. Все неинициализированные final поля получают
параметр, также как все остальные поля, помеченные @NonNull, которые не иницилизированы
при объявлении. Для этих случаев также генерируется явная проверка на null.

Конструктор бросает исключение NullPointerException, если какой-либо из параметров,
предназначенный для полей, помеченных @NonNull содержит null. Порядок этих параметров
совпадает с порядком появления полей в классе.
*/
@RequiredArgsConstructor
public class UserService {
    /* Естественно UserRepository основной из него можно получить данные о User-ах */
    private final UserRepository userRepository;
    /*
    Нам нужен объект преобразованный из User в UserReadDto, чтобы возвращать
    данные по запросу ID вверх по уровням приложения пользователю.
    */
    private final UserReadMapper userReadMapper;
    /*
    Нам нужен объект преобразованный из UserCreateDto в User, чтобы сохранять
    данные полученные с верхних уровней приложения от пользователя в БД.
    */
    private final UserCreateMapper userCreateMapper;

    /*
    Мы хотим вернуть true/false в результате работы текущего метода, однако,
    метод *.delete из UserRepository ничего не возвращает, поэтому реализуем
    двухходовочку из методов UserRepository класса.
    */
    @Transactional
    public boolean delete(Long id) {
        /* Существует вариант, что юзера с конкретным ID в БД нет - отсюда Optional */
        Optional<User> maybeUser = userRepository.findById(id);
        /* Если он есть, то мы используем метод *.delete() из UserRepository - просто удаляем */
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        /* Возвращаем результат его наличия */
        return maybeUser.isPresent();
    }

    /*
    Метод, который возвращает информацию о пользователе не обязан возвращать абсолютно все данные.
    Информация со слоя service передается на слой controller только в заранее 'отфильтрованном' -
    представленном для конкретного запроса виде. Т.е. каждый пользователь приложения получает ответ
    сообразно его доступу. См. DOC/MVC.jpg. Поэтому нам нужен возвращаемый DTO класс с определенным
    набором полей.
    */
    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        /*
        Теперь нам нужно вернуть 'полученную' / 'неполученную' сущность, при этом,
        нам нужно вернуть не саму сущность, а ее сообразное представление - UserReadDto
        Т.е. нам нужно преобразовать один объект в другой.
        */
        return findById(id, userReadMapper);
    }
    /*
    Чтобы уменьшить количество запросов к БД (N+1) добавим
    нашему User-у дополнительный граф с атрибутом 'company'
    */
    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(),
                userRepository.getEntityManager().getEntityGraph("WithCompany")
        );
        return userRepository.
                findById(id, properties).
                map(mapper::mapFrom);
    }
    /*
    Метод для сознания сущности принимает с верхнего уровня DTO
    user-a, далее должен превратить его в сущность и передать на
    нижний уровень в БД. После того как данные попадут в базу мы
    должны вернуть ID внесенного User-a
    */
    @Transactional
    public Long create(UserCreateDto userDto) {
        /* Используя UserCreateDto создаем сущность User */
        User userEntity = userCreateMapper.mapFrom(userDto);
        /* Сохраняем и возвращаем ID */
        return userRepository.save(userEntity).getId();
    }


}