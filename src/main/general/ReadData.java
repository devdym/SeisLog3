package main.general;

import main.Controller;
import main.HibernateUtil;
import main.entities.InsTestLimits;
import main.entities.InsTestRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadData {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public static List<InsTestLimits> getInsTestLimits(LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestLimits> query = session.createQuery("FROM InsTestLimits where updated like :dateupd", InsTestLimits.class);
        query.setParameter("dateupd", date);
        List<InsTestLimits> result = query.getResultList();
        transaction.commit();
        return result;
    }

    public static List<InsTestRes> getInsTestRes(LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd", InsTestRes.class);
        query.setParameter("dateupd", date);
        List<InsTestRes> result = query.getResultList();
        transaction.commit();
        return result;
    }

    public static List<InsTestRes> getInsTestResErrOnly(LocalDate date, List<InsTestLimits> lim){
        List<InsTestRes> result = new ArrayList<>();

        for(InsTestLimits l: lim) {
            logger.warn(l.toString());
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            //Cap
            Query<InsTestRes> query = session.createQuery(
                    "FROM InsTestRes where updated like :dateupd" +
                            " and cap not between :cap_min and :cap_max" +
                            " and type = :t", InsTestRes.class);
            query.setParameter("dateupd", date);
            query.setParameter("cap_min", l.getCap_min());
            query.setParameter("cap_max", l.getCap_max());
            query.setParameter("t", l.getSensor_nb());
            result.addAll(query.getResultList());
//            transaction.commit();

            //Cutoff
            Query<InsTestRes> query_cutoff = session.createQuery(
                    "FROM InsTestRes where updated like :dateupd" +
                            " and cutoff not between :cutoff_min and :cutoff_max" +
                            " and type = :t", InsTestRes.class);
            query_cutoff.setParameter("dateupd", date);
            query_cutoff.setParameter("cutoff_min", l.getCutoff_min());
            query_cutoff.setParameter("cutoff_max", l.getCutoff_max());
            query_cutoff.setParameter("t", l.getSensor_nb());
            result.addAll(query_cutoff.getResultList());
//            transaction.commit();

            //leakage
            Query<InsTestRes> query_leakage = session.createQuery(
                    "FROM InsTestRes where updated like :dateupd" +
                            " and leakage < :leakage" +
                            " and type = :t", InsTestRes.class);
            query_leakage.setParameter("dateupd", date);
            query_leakage.setParameter("leakage", l.getLeakage());
            query_leakage.setParameter("t", l.getSensor_nb());
            result.addAll(query_leakage.getResultList());
//            transaction.commit();

            //noise
            Query<InsTestRes> query_noise = session.createQuery(
                    "FROM InsTestRes where updated like :dateupd" +
                            " and noise > :noise" +
                            " and type = :t", InsTestRes.class);
            query_noise.setParameter("dateupd", date);
            query_noise.setParameter("noise", l.getNoise());
            query_noise.setParameter("t", l.getSensor_nb());
            result.addAll(query_noise.getResultList());
            transaction.commit();
        }
        Collections.sort(result, new Sortbystr());

        return result;
    }

    static class Sortbystr implements Comparator<InsTestRes> {
        public int compare(InsTestRes a, InsTestRes b){
            return a.getStreamer() - b.getStreamer();
        }
    }

}
