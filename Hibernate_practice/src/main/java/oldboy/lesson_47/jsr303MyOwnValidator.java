package oldboy.lesson_47;

import oldboy.dto.UserCreateDto;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
import oldboy.jsr303Example.Member;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

public class jsr303MyOwnValidator {

    public static void main(String[] args) {
        /* Пустой конструктор */
        Member member = new Member();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator memberValidator = validatorFactory.getValidator();
        var violations = memberValidator.validate(member);
        System.out.println("Количество нарушений: " + violations.size());
        /* Количество нарушений: 8 */

        UserCreateDto createDto =
                new UserCreateDto(PersonalInfo.builder()
                .firstName("Sanara")
                .lastName("Questa")
                .birthDate(LocalDate.of(2001, 10, 11))
                .build(),
                null,
                null,
                Role.ADMIN,
                3);

        var userValidator = validatorFactory.getValidator();
        var validationResult = userValidator.validate(createDto);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
        /*
        Exception in thread "main" javax.validation.ConstraintViolationException
        */
    }
}
