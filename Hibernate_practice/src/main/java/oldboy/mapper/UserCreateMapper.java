package oldboy.mapper;
/* Маппер - преобразователь объекта DTO в сущность User */
import lombok.RequiredArgsConstructor;
import oldboy.dao.CompanyRepository;
import oldboy.dto.UserCreateDto;
import oldboy.entity.User;
/*
Данный маппер, как и все остальные у нас будет синглтон,
т.к. одного на все приложение будет достаточно
*/
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .userName(object.username())
                .info(object.info())
                .role(object.role())
                .company(companyRepository.
                        findById(object.companyId()).
                        orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
