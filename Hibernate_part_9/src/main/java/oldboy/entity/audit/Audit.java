package oldboy.entity.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "audit", schema = "part_four_base")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id")
    private Serializable entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_content")
    private String entityContent;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private Operation operation;

    public enum Operation {
        SAVE, UPDATE, DELETE, INSERT
    }
}