package oldboy.lesson_15_volOne.Entity_15_volOne;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "porters", schema = "training_base")
public class Porter extends BaseEntity<Long>{

    @Column(name = "porter_name")
    private String porterName;
    @Column(name = "porter_age")
    private Integer porterAge;
    @Column(name = "storage_number")
    private Integer storageNumber;
}
