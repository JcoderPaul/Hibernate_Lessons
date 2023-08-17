package oldboy.mapper;
/*
В нашем случае мы все делаем руками, но существуют
специализированные фреймворки, например MapStruct
*/
import lombok.RequiredArgsConstructor;
import oldboy.dto.UserReadDto;
import oldboy.entity.User;

import java.util.Optional;
/*
Нам нужно преобразовать User в UserReadDto - 'F rom' в 'T o'
Все мапперы - преобразователи должны быть потокобезопасными
и для работы всего приложения достаточно одного экземпляра.
*/
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {
    /*
    Прописываем зависимость на CompanyReadMapper,
    т.е. конструктор данного класса будет принимать
    CompanyReadMapper в качестве параметра.
    */
    private final CompanyReadMapper companyReadMapper;
    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getPersonalInfo(),
                object.getUserName(),
                object.getInfo(),
                object.getRole(),
                /*
                Нужно помнить, что Company тоже класс (сущность) и, возможно, не все его
                поля мы хотим передавать вверх по слоям. Значит, нам нужен еще DTO класс
                для Company, отсюда вытекает, что нам нужен еще один преобразователь:
                'Company' to 'CompanyReadDto' - создаем CompanyReadMapper.

                НО, и это еще не все! У нас может быть User без Company и тогда вероятен
                выброс исключения, поэтому снова применяем Optional. И если компания есть
                мы вернем ее DTO, если же ее нет просто - NULL.

                Есть конечно еще выход из ситуации в БД поле 'company_id' в таблице 'users'
                должно иметь ограничение - NOT NULL. Тогда эти проверки излишни.
                */
                Optional.ofNullable(object.getCompany())
                        .map(companyToDto -> companyReadMapper.mapFrom(companyToDto))
                        .orElse(null)
        );
    }
}
