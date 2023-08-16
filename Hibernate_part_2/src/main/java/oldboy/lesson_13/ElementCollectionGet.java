package oldboy.lesson_13;
/*
Тестируем ElementCollection
Данные в БД в таблицу Books внесли руками
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_13.MappingEntity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ElementCollectionGet {
    public static void main(String[] args) {

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии : " + sessionOne.getStatistics());
                /* Статистика первой сессии : SessionStatistics[entity count=0,collection count=0] */
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                Book bookToReadLocale = sessionOne.get(Book.class,2);
                System.out.println(bookToReadLocale.getLocales());
                /*
                Hibernate:
                    select
                        book0_.book_id as book_id1_2_0_,
                        book0_.book_name as book_nam2_2_0_
                    from
                        public.books book0_
                    where
                        book0_.book_id=?
                Hibernate:
                    select
                        locales0_.book_id as book_id1_1_0_,
                        locales0_.description as descript2_1_0_,
                        locales0_.lang as lang3_1_0_
                    from
                        public.book_locale locales0_
                    where
                        locales0_.book_id=?

                    [LocaleInfo(lang=EN, description=English description),
                    LocaleInfo(lang=RU, description=Описание на русском)]
                */
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
        }
    }
}
