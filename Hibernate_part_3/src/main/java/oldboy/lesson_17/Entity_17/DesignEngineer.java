package oldboy.lesson_17.Entity_17;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_17.Entity_17.Enums.DesignSoftware;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("design_engineer")
public class DesignEngineer extends Worker {
    @Enumerated(EnumType.STRING)
    @Column(name = "design_software")
    private DesignSoftware designSoftware;
    /* Создаем полный конструктор с отличающимся полем и для удобства аннотируем @Builder */
    @Builder
    public DesignEngineer(Long Id,
                          PersonalInfo personalInfo,
                          String employeeEmail,
                          Company company,
                          DesignSoftware designSoftware) {
        super(Id, personalInfo, employeeEmail, company);
        this.designSoftware = designSoftware;
    }
}
