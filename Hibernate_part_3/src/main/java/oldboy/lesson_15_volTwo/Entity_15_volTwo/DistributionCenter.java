package oldboy.lesson_15_volTwo.Entity_15_volTwo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.entity.accessory.AuditableEntity;
import oldboy.lesson_15_volOne.Entity_15_volOne.Driver;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "distribution_center", schema = "training_base")
public class DistributionCenter extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private Long storageId;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    /*
    Повторим: Данная аннотация, при текущем параметре, позволяет
    сохранить или обновить связные сущности, без применения отдельного
    метода сохранения в БД.

    Т.е. при сохранении сущности DistributionCenter в БД, со всеми
    установленными полями, будет сохранена сущность Driver, без
    использования отдельного метода session.save(Driver).
    См. MappedInterfaceDemo.java
    */
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "storage_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Storage storage;

    public void setDriver(Driver drv) {
        this.driver = drv;
        this.driver.getDistributionCenters().add(this);
    }

    public void setStorage(Storage str) {
        this.storage = str;
        this.storage.getDistributionCenters().add(this);
    }

    @Override
    public void setId(Long id) {
        this.storageId = id;
    }

    @Override
    public Long getId() {
        return this.storageId;
    }
}