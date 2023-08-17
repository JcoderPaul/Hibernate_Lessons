package oldboy.entity.accessory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {
    /*
    Много раз нарывался и снова!!! Необходимо максимально внимательно
    сопоставлять поля классов и поля таблиц БД - буквально буква в букву,
    одна лишняя или недостающая и можно поймать:
    WARN [org.hibernate.engine.jdbc.spi.SqlExceptionHelper: 137] SQL Error: 0, SQLState: 42703
    и по неопытности искать ошибку, а это - "hibernate couldn't find column".
    */
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
}