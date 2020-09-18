package main.general;

import main.HibernateUtil;
import main.entities.InsTestLimits;
import main.entities.Projects;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DeleteData {
    public static void deleteProject(long id){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Projects project = session.get(Projects.class, id);
        session.delete(project);
        transaction.commit();
    }
}
