package main.general;

import main.Controller;
import main.HibernateUtil;
import main.entities.Projects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class StoreData {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static void storeProject(Projects pr) {
        SessionFactory sessFact = HibernateUtil.getSessionFactory();
        Session session = sessFact.getCurrentSession();
        org.hibernate.Transaction tr = session.beginTransaction();
        session.save(pr);
        tr.commit();
        logger.warn("New Project Successfully inserted");
        sessFact.close();
    }
}
