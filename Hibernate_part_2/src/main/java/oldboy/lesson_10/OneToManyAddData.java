package oldboy.lesson_10;
/*
Пример работы со связью Один-ко-Многим и Многие-к-Одному
картируемые классы находятся в lesson_10/MappingEntity.
В текущем файле мы добавим немного связных записей в БД.
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_10.MappingEntity.Firm;
import oldboy.lesson_10.MappingEntity.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OneToManyAddData {
    public static void main(String[] args) {

        Worker workerOne = Worker.
                builder().
                workerName("Giro Kano").
                salary(1351.2).
                build();

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                Firm firmToAdd = sessionOne.get(Firm.class, 1);
                /*
                В данном примере мы фирму добавляем работника, а затем сохраняем фирму
                в базе данных. При этом, мы специально не сохраняем работника, и однако
                в базе он появляется (как связная запись)
                */
                firmToAdd.addWorker(workerOne);
                sessionOne.save(firmToAdd);

                /* Получаем весь список рабочих по фирме с FORM_ID = 1 */
                Firm firmToRemove = sessionOne.getReference(Firm.class, 1);
                System.out.println(firmToRemove.getWorkers());
                sessionOne.flush();
                /* Удаляем из списка если таковой есть работника с WORKER_ID = 8 */
                firmToRemove.getWorkers().removeIf(worker -> worker.getWorkerId().equals(10));

                System.out.println("Статистика первой сессии перед commit: " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
                System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());

            System.out.println("------------ Close first session ------------");
        }
    }
}
