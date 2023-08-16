package oldboy.lesson_13;
/*
Тестируем ElementCollection
Данные в БД в таблицу Books внесли руками
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_13.MappingEntity.LocaleInfo;
import oldboy.lesson_13.MappingEntity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ElementCollectionAdd {
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

                Book bookToAddLocale = sessionOne.get(Book.class,2);
                bookToAddLocale.getLocales().add(LocaleInfo.of("RU","Описание на русском"));
                bookToAddLocale.getLocales().add(LocaleInfo.of("EN","English description"));

                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
        }
    }
}
