package oldboy.lesson_8;
/*
Демонстрация способов генерации ID записи в БД
стратегия генерации GenerationType.TABLE
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_8.EntityDemo.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class GeneratorIdByTable {

    public static void main(String[] args) {

        Teacher techOne = Teacher.
                            builder().
                            teacher_name("Timus Rodderik").
                            teacher_email("timus@swordwing.com").
                            age(148).
                            build();

        Teacher techTwo = Teacher.
                            builder().
                            teacher_name("Govard Addington").
                            teacher_email("govi@swordwing.com").
                            age(654).
                            build();
        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

                /* Следующий лог после начала транзакции */
                sessionOne.beginTransaction();

                sessionOne.saveOrUpdate(techOne);
                /*
                При сохранении сущности, сначала идет обращение к 'all_sequence' и это
                запрос блокирует базу, на случай если кто-то попытается изменить текущее
                состояние счетчика кроме нас, во избежании ситуации, когда мы имеем две
                сущности с одинаковым ID.
                ********************************************************************
                   Hibernate:
                        select
                            tbl.pk_value
                        from
                            all_sequence tbl
                        where
                            tbl.table_name=? for update
                                of tbl
                ********************************************************************
                за тем, получение названия таблицы и ее величины (при первом обращении
                это 0, который затем увеличивается на 1 и т.д.):
                ********************************************************************
                   Hibernate:
                            insert
                            into
                                all_sequence
                                (table_name, pk_value)
                            values
                                (?,?)
                ********************************************************************
                за тем наш персистивный объект класса Teacher получает свой ID:
                ********************************************************************
                    Hibernate:
                            update
                                all_sequence
                            set
                                pk_value=?
                            where
                                pk_value=?
                                and table_name=?
                ********************************************************************
                и только затем вносится в БД, ТОЛЬКО ПОСЛЕ КОММИТА, т.е. после
                sessionOne.getTransaction().commit():
                ********************************************************************
                Hibernate:
                            insert
                            into
                                public.teachers
                                (age, teacher_email, teacher_name, id)
                            values
                                (?, ?, ?, ?)
                ********************************************************************
                */
                sessionOne.saveOrUpdate(techTwo);

                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();
            } catch (Exception exc) {
                throw exc;
            } finally {
                sessionOne.close();
            }
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

            System.out.println("------------ Close first session ------------");
        }
    }
}
