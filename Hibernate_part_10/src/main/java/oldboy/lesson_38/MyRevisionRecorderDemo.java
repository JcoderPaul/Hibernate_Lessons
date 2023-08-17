package oldboy.lesson_38;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Company;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MyRevisionRecorderDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            Company companyForEdit = sessionOne.find(Company.class, 5);
            companyForEdit.setCompanyName("Miramax");
            sessionOne.save(companyForEdit);

            Company companyForAdd = Company.
                    builder().
                    companyName("Alrosa co.").
                    build();
            sessionOne.save(companyForAdd);

            Company forRemoveCompany = sessionOne.find(Company.class, 6);
            sessionOne.remove(forRemoveCompany);
            /*
            Соответствующие запросы формируются Hibernate и изменения фиксируются в
            таблицах (в нашем случае company_aud, revision_records)
            */
            sessionOne.getTransaction().commit();
        }
    }
}
