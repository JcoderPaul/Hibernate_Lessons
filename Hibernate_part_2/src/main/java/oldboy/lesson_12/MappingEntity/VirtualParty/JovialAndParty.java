package oldboy.lesson_12.MappingEntity.VirtualParty;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "jovial_parties", schema = "public")
public class JovialAndParty {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long Id;

        /* Множество вечеринок к одному нашему весельчаку */
        @ManyToOne
        /*
        Если имя поля в соответствующей таблице БД имеет
        такое же имя, то можно и не определять его специально
        */
        @JoinColumn(name = "jovial_id")
        private Jovial jovial;

        /* Множество весельчаков к одной нашей вечеринке */
        @ManyToOne
        @JoinColumn(name = "party_id")
        private VirtualParty party;

        /* Подхватываем время из БД */
        private Instant created_time;
        /* Кто создал виртуальную вечеринку */
        private String created_jovial;
        /*
        Как и ранее, создаем вспомогательные методы для
        перекрестного присваивания/добавления сущностей
        */
        public void setJovial(Jovial jovial) {
                this.jovial = jovial;
                jovial.getParties().add(this);
        }

        public void setParty(VirtualParty party) {
                this.party = party;
                party.getJovial().add(this);
        }
}
