package oldboy.lesson_12.MappingEntity.VirtualParty;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "parties")
@EqualsAndHashCode(exclude = "parties")
@Builder
@Entity
@Table(name = "jovial", schema = "public")
public class Jovial {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "jovial_id")
        private Long jovialId;

        @Column(name = "jovial_name")
        private String jovialName;

        @Column(name = "jovial_age")
        private Integer jovialAge;

        @Builder.Default
        @OneToMany(mappedBy = "jovial")
        @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
        private List<JovialAndParty> parties = new ArrayList<>();
}
