package oldboy.lesson_18.Entity_18;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_18.Entity_18.Enums.MachineBodyDetail;

import javax.persistence.*;

@Getter
@Setter
/*
Если мы хотим при выводе на печать видеть все поля,
включая и поля родителя применяем параметр callSuper
*/
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tinman", schema = "training_base")
@PrimaryKeyJoinColumn(name = "id")
public class Tinman extends CarServiceSpecialist {
    @Column(name = "machine_body_detail")
    private MachineBodyDetail machineBodyDetail;
    /*
    Для удобства создания переопределим конструктор, в котором
    будет и 'отличительное поле', аннотируем @Builder его для
    удобства работы.
    */
    @Builder
    public Tinman(Long Id,
                  PersonalInfo personalInfo,
                  String employeeEmail,
                  Company company,
                  MachineBodyDetail machineBodyDetail) {
        super(Id, personalInfo, employeeEmail, company);
        this.machineBodyDetail = machineBodyDetail;
    }
}
