package oldboy.entity.accessory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {

    private String firstName;
    private String lastName;
    /*
    Способ валидации данных с использованием аннотации,
    в данном случае дата рождения не может быть null
    */
    @NotNull
    private LocalDate birthDate;
}
