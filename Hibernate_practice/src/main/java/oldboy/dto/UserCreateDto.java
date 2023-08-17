package oldboy.dto;
/*
Данный класс нам нужен для того, чтобы получить
данные от пользователя, т.е. пользователь нашего
web-приложения может передать только этот набор
данных, которые затем будут преобразованы в
сущность User и переданы в БД.
*/
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreateDto(@Valid
                            PersonalInfo personalInfo,
                            @NotNull
                            String username,
                            String info,
                            Role role,
                            Integer companyId) {
}
