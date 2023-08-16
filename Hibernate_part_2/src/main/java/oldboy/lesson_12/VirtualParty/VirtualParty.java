package oldboy.lesson_12.VirtualParty;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "jovial")
@EqualsAndHashCode(exclude = "jovial")
@Builder
@Entity
@Table(name = "virtual_parties", schema = "public")
public class VirtualParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "party_name")
    private String partyName;

    @Builder.Default
    @OneToMany(mappedBy = "party")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<JovialAndParty> jovial = new ArrayList<>();
}
