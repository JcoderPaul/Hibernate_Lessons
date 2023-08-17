package oldboy.lesson_18.Entity_18;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_18.Entity_18.Enums.EngineModel;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "engine_repairman", schema = "training_base")
@PrimaryKeyJoinColumn(name = "id")
public class EngineRepairman extends CarServiceSpecialist {
    @Enumerated(EnumType.STRING)
    @Column(name = "engine_model")
    private EngineModel engineModel;
    /* Создаем полный конструктор с отличающимся полем и для удобства аннотируем @Builder */
    @Builder
    public EngineRepairman(Long Id,
                           PersonalInfo personalInfo,
                           String employeeEmail,
                           Company company,
                           EngineModel engineModel) {
        super(Id, personalInfo, employeeEmail, company);
        this.engineModel = engineModel;
    }
}
