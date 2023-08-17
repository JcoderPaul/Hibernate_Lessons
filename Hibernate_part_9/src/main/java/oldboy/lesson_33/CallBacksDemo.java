package oldboy.lesson_33;
/*
Демонстрация работы CallBacks.
См. DOC/CallBackInHibernate.txt

В данном примере задействована сущность MeetingRoom в
которой аннотированы @PrePersist и @PreUpdate отдельные
методы, и они жестко связанны с данной сущностью.

Если же мы хотим, чтобы данные или другие CallBack - и
вызывались у других сущностей, то эти методы, с CallBack
аннотациями должны быть внесены в отдельный класс (например,
наш AuditableEntity) и тогда все его наследники смогут
воспользоваться 'данным сервисом'
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.MeetingRoom;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CallBacksDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            MeetingRoom meetingRoom = MeetingRoom.
                    builder().
                    roomName("Black").
                    enterPrice(1123).
                    build();

            sessionOne.save(meetingRoom);
            sessionOne.flush();
            /*
            insert
                    into
            part_four_base.meeting_rooms
                    (created_at, created_by, updated_at, updated_by, enter_price, room_name)
            values
                    (?, ?, ?, ?, ?, ?)

            */
            MeetingRoom sameMeetingRoom = sessionOne.find(MeetingRoom.class, 1L);
            sameMeetingRoom.setEnterPrice(sameMeetingRoom.getEnterPrice() + 100);
            /*
            update
                part_four_base.meeting_rooms
            set
                created_at=?,
                created_by=?,
                updated_at=?,
                updated_by=?,
                enter_price=?,
                room_name=?
            where
                room_id=?
            */
            sessionOne.getTransaction().commit();
            /*
            В базе данных мы увидим (хотя специально данные по времени не вносили), как
            сработали callback - и:

            room_id, "room_name", "created_at",              "updated_at",             "enter_price"
            1,       "Black",     "2023-08-06 20:00:08.45",  "2023-08-06 20:00:08.51", 1223
            */
        }
    }
}
