package oldboy.entity.accessory;

import lombok.*;
import javax.persistence.Embeddable;
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
    private LocalDate birthDate;
}
