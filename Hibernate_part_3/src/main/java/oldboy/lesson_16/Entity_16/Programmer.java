package oldboy.lesson_16.Entity_16;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.ProgrammingLang;

import javax.persistence.*;

/* Первая сущность - Программист, наследуется от Employee */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
/* А вот у данной сущности есть своя таблица */
@Table(name = "programmers", schema = "training_base")
public class Programmer extends Employee {
    /*
    Сущность программиста отличается от других
    наследников User полем - programmingLanguage
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "programming_language")
    private ProgrammingLang programmingLanguage;
    /* Создаем полный конструктор с отличающимся полем и для удобства аннотируем @Builder */
    @Builder
    public Programmer(Long Id,
                      PersonalInfo personalInfo,
                      String employeeEmail,
                      Company company,
                      ProgrammingLang programmingLanguage) {
        super(Id, personalInfo, employeeEmail, company);
        this.programmingLanguage = programmingLanguage;
    }
}
