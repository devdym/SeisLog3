package main.general;

import main.Controller;
import main.HibernateUtil;
import main.ballasting.SeqData;
import main.entities.Ballasting;
import main.entities.Batteries;
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

    public static List<LocalDate> getInsTestDates(){
        logger.warn("reading InsTest dates from DB");
        List<LocalDate> res = new ArrayList<>();
        //read InsTest Dates
        Runnable readPreferences = () -> {
            logger.warn("query ins test dates");
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            org.hibernate.Transaction transaction = session.beginTransaction();

            List<LocalDate> tt = session.createQuery("SELECT DISTINCT updated FROM InsTestRes order by updated desc", LocalDate.class).getResultList();
            res.addAll(tt);
            transaction.commit();
        };
        new Thread(readPreferences).start();
        return res;
    }

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

    public static List<Batteries> getAllBatt(LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query =
                session.createQuery("FROM Batteries where date_ like :dateupd",
                        Batteries.class);
        query.setParameter("dateupd", date);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    public static List<Batteries> getErrBatt(LocalDate date, Double min_a_v, Double min_b_v){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query =
                session.createQuery("FROM Batteries where date_ like :dateupd and bankA < :minA or bankB < :minB",
                        Batteries.class);
        query.setParameter("dateupd", date);
        query.setParameter("minA", min_a_v);
        query.setParameter("minB", min_b_v);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    public static List<Batteries> readBattDataForGraph(String selected){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query = session.createQuery("FROM Batteries where unit like :u order by date_", Batteries.class);
        query.setParameter("u", selected);
        List<Batteries> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<LocalDate> readBattDate() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        org.hibernate.Transaction transaction = session.beginTransaction();

        List<LocalDate> batteryDate =
                session.createQuery("SELECT DISTINCT date_ FROM Batteries order by date_ desc", LocalDate.class)
                        .getResultList();
       // BattDates.addAll(batteryDate);
        transaction.commit();
        return batteryDate;
    }

    //        Collections.sort(result, new Sortbystr());
    static class Sortbystr implements Comparator<InsTestRes> {
        public int compare(InsTestRes a, InsTestRes b){
            return a.getStreamer() - b.getStreamer();
        }
    }

//    public static List<InsTestRes> getInsTestResErrOnly(LocalDate date, List<InsTestLimits> lim){
//        List<InsTestRes> result = new ArrayList<>();
//
//        for(InsTestLimits l: lim) {
//            logger.warn(l.toString());
//            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//            Session session = sessionFactory.getCurrentSession();
//            Transaction transaction = session.beginTransaction();
//            //Cap
//            Query<InsTestRes> query = session.createQuery(
//                    "FROM InsTestRes where updated like :dateupd" +
//                            " and cap not between :cap_min and :cap_max" +
//                            " and type = :t", InsTestRes.class);
//            query.setParameter("dateupd", date);
//            query.setParameter("cap_min", l.getCap_min());
//            query.setParameter("cap_max", l.getCap_max());
//            query.setParameter("t", l.getSensor_nb());
//            result.addAll(query.getResultList());
////            transaction.commit();
//
//            //Cutoff
//            Query<InsTestRes> query_cutoff = session.createQuery(
//                    "FROM InsTestRes where updated like :dateupd" +
//                            " and cutoff not between :cutoff_min and :cutoff_max" +
//                            " and type = :t", InsTestRes.class);
//            query_cutoff.setParameter("dateupd", date);
//            query_cutoff.setParameter("cutoff_min", l.getCutoff_min());
//            query_cutoff.setParameter("cutoff_max", l.getCutoff_max());
//            query_cutoff.setParameter("t", l.getSensor_nb());
//            result.addAll(query_cutoff.getResultList());
////            transaction.commit();
//
//            //leakage
//            Query<InsTestRes> query_leakage = session.createQuery(
//                    "FROM InsTestRes where updated like :dateupd" +
//                            " and leakage < :leakage" +
//                            " and type = :t", InsTestRes.class);
//            query_leakage.setParameter("dateupd", date);
//            query_leakage.setParameter("leakage", l.getLeakage());
//            query_leakage.setParameter("t", l.getSensor_nb());
//            result.addAll(query_leakage.getResultList());
////            transaction.commit();
//
//            //noise
//            Query<InsTestRes> query_noise = session.createQuery(
//                    "FROM InsTestRes where updated like :dateupd" +
//                            " and noise > :noise" +
//                            " and type = :t", InsTestRes.class);
//            query_noise.setParameter("dateupd", date);
//            query_noise.setParameter("noise", l.getNoise());
//            query_noise.setParameter("t", l.getSensor_nb());
//            result.addAll(query_noise.getResultList());
//            transaction.commit();
//        }
//        Collections.sort(result, new Sortbystr());
//
//        return result;
//    }

    public static List<Integer> getLastSeq(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT seq FROM Ballasting order by seq desc", Integer.class);
        query.setMaxResults(10);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public static List<Integer> getStrList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT streamer FROM Ballasting order by streamer", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public static List<Integer> getCompassList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT compass FROM Ballasting order by compass", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public static List<Ballasting> getDataList(int seq){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Ballasting> query = session.createQuery("FROM Ballasting WHERE seq = :seq", Ballasting.class);
        query.setParameter("seq", seq);
        List<Ballasting> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public static List<Ballasting> getBallastinDataForGraph(SeqData unit){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Ballasting> query = session.createQuery("FROM Ballasting where streamer = :s and compass = :c order by seq", Ballasting.class);
        query.setParameter("s", unit.getStreamer());
        query.setParameter("c", unit.getUnit());
        List<Ballasting> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<InsTestRes> getCapGraphData(int SelStr, LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
        query.setParameter("dateupd", date);
        query.setParameter("str", SelStr);
        List<InsTestRes> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<InsTestRes> getCutGraphData(int SelStr, LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
        query.setParameter("dateupd", date);
        query.setParameter("str", SelStr);
        List<InsTestRes> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<InsTestRes> getLeakageGraphData(int SelStr, LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
        query.setParameter("dateupd", date);
        query.setParameter("str", SelStr);
        List<InsTestRes> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<InsTestRes> getNoiseGraphData(int SelStr, LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
        query.setParameter("dateupd", date);
        query.setParameter("str", SelStr);
        List<InsTestRes> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

    public static List<InsTestRes> getChGraphData(int selected){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<InsTestRes> query = session.createQuery("FROM InsTestRes where ass_sn = :s order by updated", InsTestRes.class);
        query.setParameter("s", selected);
        List<InsTestRes> sp = query.getResultList();
        transaction.commit();
        return sp;
    }

}
