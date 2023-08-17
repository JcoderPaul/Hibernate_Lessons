package oldboy.listeners;

import oldboy.entity.revtable.RevisionRecorder;
import org.hibernate.envers.RevisionListener;
/*
Создаем слушатель ревизий, который будет фиксировать
события по изменению состояния аудируемых сущностей
и вносить в БД имя того, кто эти изменения внес -
чистая демонстрация, без реализации серьезного кода.
*/
public class MyFirstRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        /*
        В данной ситуации мы задаем имя пользователя внесшего
        изменения в БД руками, т.к. такой функционал как,
        например: SecurityContext.getUser().getId() тут не
        реализован.
         */
        ((RevisionRecorder) revisionEntity).setUserName("OldBoy");
    }
}
