package oldboy.lesson_10.MappingEntity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString(exclude = "workers")
@EqualsAndHashCode(exclude = "workers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "firms", schema = "public")
public class Firm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firm_id")
    private Integer firmId;

    @Column(name = "firm_name")
    private String firmName;

    /* Для установки дефолтных значений в List */
    @Builder.Default
    /*
    Когда в одном классе, у нас это Worker, есть поле @ManyToOne,
    а в другом, у нас это Firm, есть поле аннотированное @OneToMany
    такую связь называют двунаправленной - BiDirectional.

    В случае с @ManyToOne применяется дополнительная аннотация
    @JoinColumn с параметром указывающим на поле связку
    (name = "firm_id").

    В случае с @OneToMany маппинг параметр указывает на поле в классе
    с которым происходит связка. Список содержит набор класса Worker,
    одно из полей которого firm связанно с текущим объектом Firm.

    CascadeType.ALL (в любом из состояний) - данный параметр необходим для
    того, чтобы в ситуации изменений данных в одной из связанных таблиц,
    эти изменения каскадом повлияли на все другие связные таблицы.

    Если с добавлением, к параметру CascadeType.ALL вопросов вроде бы нет,
    то вот с удалением, все не так замечательно. При удалении компании, при
    таком варианте настройки, мы автоматом потеряем и всех работников
    связных с этой компанией.  А если существуют еще связи с другими таблицами,
    то и они будут захвачены и так может продолжаться, пока мы не удалим
    содержимое всех таблиц - зачистим всю БД (при неправильной архитектуре
    БД это возможно).

    !!! Существует рекомендация, что настройки каскадирования лучше определять
    на уровне базы данных (быстрее и безопаснее), а не на уровне Hibernate !!!

    Параметр orphanRemoval определяет, как поступать с сущностью из списка
    List<Worker> workers, при удалении или других манипуляциях, но не с
    объектом Firm.
    */
    @OneToMany(targetEntity = Worker.class,
               mappedBy = "firm",
               orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    /* Определяем порядок сортировки по полю workerName */
    @OrderBy("workerName DESC")
    private List<Worker> workers = new ArrayList<>();
    /*
    Взаимный проброс ссылок и установок друг на друга,
    рабочего добавляем в List работников, и фирму добавляем
    работнику.
    */
    public void addWorker(Worker worker){
        workers.add(worker);
        worker.setFirm(this);
    }
}
