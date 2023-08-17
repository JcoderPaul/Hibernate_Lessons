package oldboy.listener;
/*
Обычно всю логику по прослушиванию жизненного цикла
сущности все же принято выносить в отдельный класс,
что мы и сделали.

Класс несущий подобный функционал называют слушатель -
listener.

!!! В данном слушателе мы можем реализовать все 7-мь
вариантов callback - ов, см. DOC/CallBackInHibernate.txt,
однако, только по одному варианту каждого вида !!!

Т.е. например, два @PrePersist callback - а, но с разными
названиями в одном слушателе мы создать не можем. Хотим
еще навесить функционала (другого) на помеченный @PrePersist
метод нужно создавать другой слушатель - вот их уже два.
*/
import oldboy.entity.accessory.AuditableEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class CreateUpdateListener {
    /*
    Callback аннотации для перехвата события в сущности
    наследующей AuditableEntity классу. Был вариант
    поместить всю эту логику именно в аудит-класс, но
    так делать 'не по-канону' - чем меньше связность тем
    лучше.
    */
    @PrePersist
    public void prePersist(AuditableEntity<?> entity) {
        entity.setCreatedAt(Instant.now());
        /*
        Тут мы реализуем только логику проставления даты,
        но не логику внесения данных по полю createdBy в
        классе AuditableEntity.java, а соответственно и в
        таблицах БД.

        В методе *.preUpdate() тоже самое, только дата.
        */
    }

    @PreUpdate
    public void preUpdate(AuditableEntity<?> entity) {
        entity.setUpdatedAt(Instant.now());
    }
}
