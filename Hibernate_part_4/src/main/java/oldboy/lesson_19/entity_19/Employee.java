package oldboy.lesson_19.entity_19;

import lombok.*;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;

import javax.persistence.*;

/*
В данном примере используются именованные запросы и соответствующие аннотации.
Естественно данная (ые) аннотация (ии) ставится над сущностью, которую планируется
возвращать по данному запросу (запросам).
*/
@NamedQuery(name = "findEmployeeByFirstNameAndOrderingByLastName",
            query = "select emp " +
                    "from Employee emp " +
                    "where emp.personalInfo.firstName = :firstName " +
                    "order by emp.personalInfo.lastName asc")
@NamedQuery(name = "findEmployeeByLastName",
        query = "select emp " +
                "from Employee emp " +
                "where emp.personalInfo.lastName = :lastName")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "employee", schema = "part_four_base")
public class Employee implements Comparable<Employee>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    @AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    private PersonalInfo personalInfo;

    @Column(name = "employee_email", unique = true)
    private String employeeEmail;

    @Override
    public int compareTo(Employee o) {
        return employeeEmail.compareTo(o.employeeEmail);
    }

    @Override
    public void setId(Long id) {
        this.Id = id;
    }

    @Override
    public Long getId() {
        return Id;
    }
}