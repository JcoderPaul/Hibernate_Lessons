package oldboy.listener;

import oldboy.entity.audit.Audit;
import org.hibernate.event.spi.*;
/*
Пусть наша система аудита будет фиксировать все события связанные с
удалением и вставкой сущностей, для этого наш класс имплементирует
соответствующие интерфейсы. И естественно нам нужно переопределить
их методы.

Но, кроме того, после написания необходимого кода, нам нужно
'зарегистрировать' в нужной группе слушателей - listener-ов
наш текущий AuditTableListener, чтобы он мог взаимодействовать
с Hibernate-ом. Это мы будем делать в нашем утилитном классе:
oldboy/Util/HibernateUtil.java
*/
public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        /*
        Возвращая false мы показываем системе, что операция
        идет в пределах задуманного и ее можно разрешить.
        */
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }
    /*
    При сохранении EVENT сущности будет генерироваться очередной event - событие,
    и это приведет к зацикливанию процесса сохранения (event - save - new event -
    new save - и т.д.). Т.е. например, произошло сохранение User-a - генерация
    event-a, и тут же мы вносим эти данные в таблицу 'audit', а это снова событие,
    или новый event и так по кругу. Для этого применим проверку и исключим из
    прослушивания Event событий связанных с Audit сущностями.
    */
    public void auditEntity(AbstractPreDatabaseOperationEvent event,
                            Audit.Operation operation) {
        if (event.getEntity().getClass() != Audit.class) {
            Audit audit = Audit.builder()
                    .entityId(event.getId())
                    .entityName(event.getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().save(audit);
        }
    }
}
