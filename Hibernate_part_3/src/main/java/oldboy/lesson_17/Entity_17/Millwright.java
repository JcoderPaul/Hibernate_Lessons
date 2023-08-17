package oldboy.lesson_17.Entity_17;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_17.Entity_17.Enums.MillwrightSpecialization;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("millwright")
public class Millwright extends Worker {
    @Column(name = "millwright_specialization")
    private MillwrightSpecialization millwrightSpecialization;
    /*
    Для удобства создания переопределим конструктор, в котором
    будет и 'отличительное поле', аннотируем @Builder его для
    удобства работы.
    */
    @Builder
    public Millwright(Long Id,
                      PersonalInfo personalInfo,
                      String employeeEmail,
                      Company company,
                      MillwrightSpecialization millwrightSpecialization) {
        super(Id, personalInfo, employeeEmail, company);
        this.millwrightSpecialization = millwrightSpecialization;
    }
}
