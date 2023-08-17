package oldboy.lesson_15_volTwo;

import oldboy.Util.HibernateUtil;

import oldboy.lesson_15_volOne.Entity_15_volOne.Driver;
import oldboy.lesson_15_volTwo.Entity_15_volTwo.DistributionCenter;
import oldboy.lesson_15_volTwo.Entity_15_volTwo.Storage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.Instant;

public class MappedInterfaceDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            Driver driver = Driver.
                                builder().
                                driverName("Yuriy Boyko").
                                driverAge(48).
                                carModel("Kenworth T600").
                                build();
            Storage storage = Storage.
                                builder().
                                storageAddress("Lantoly co. st.32 p.12").
                                build();

            DistributionCenter distributionCenter =
                    new DistributionCenter();

            distributionCenter.setDriver(driver);
            distributionCenter.setStorage(storage);
            distributionCenter.setCreatedAt(Instant.now()); // Задаем текущее время (отпечаток времени)
            distributionCenter.setCreatedBy("Yaken Hgar");

            sessionOne.saveOrUpdate(distributionCenter);

            sessionOne.getTransaction().commit();
        }
    }
}
