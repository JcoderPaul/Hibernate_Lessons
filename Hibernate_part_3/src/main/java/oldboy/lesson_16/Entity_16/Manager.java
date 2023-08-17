package oldboy.lesson_16.Entity_16;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.PersonalInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/* Второй наследник класса Employee */
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
/* А вот у данной сущности есть своя таблица */
@Table(name = "managers", schema = "training_base")
public class Manager extends Employee {
    /* Отличается полем - projectName */
    @Column(name = "project_name")
    private String projectName;
    /*
    Для удобства создания переопределим конструктор, в котором
    будет и 'отличительное поле', аннотируем @Builder его для
    удобства работы.
    */
    @Builder
    public Manager(Long Id,
                   PersonalInfo personalInfo,
                   String employeeEmail,
                   Company company,
                   String projectName) {
        super(Id, personalInfo, employeeEmail, company);
        this.projectName = projectName;
    }
}
