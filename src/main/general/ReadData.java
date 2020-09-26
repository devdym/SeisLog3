package main.general;

import com.sun.javafx.scene.control.Properties;
import main.Controller;
import main.HibernateUtil;
import main.ballasting.SeqData;
import main.entities.*;
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
    // general
    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    // Instrument test tab
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

    public static List<Projects> getAllProjects() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Projects> query = session.createQuery("FROM Projects", Projects.class);
        List<Projects> result = query.getResultList();
        transaction.commit();
        session.close();
        return result;
    }

    //        Collections.sort(result, new Sortbystr());
    static class Sortbystr implements Comparator<InsTestRes> {
        public int compare(InsTestRes a, InsTestRes b){
            return a.getStreamer() - b.getStreamer();
        }
    }

    // Batteries tab
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
        session.close();
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
        session.close();
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
        session.close();
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
        session.close();
        return batteryDate;
    }

    // Ballasting tab
    public static List<Integer> getLastSeq(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT seq FROM Ballasting order by seq desc", Integer.class);
        query.setMaxResults(10);
        List<Integer> res = query.getResultList();
        transaction.commit();
        session.close();
        return res;
    }

    public static List<Integer> getStrList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT streamer FROM Ballasting order by streamer", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        session.close();
        return res;
    }

    public static List<Integer> getCompassList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT compass FROM Ballasting order by compass", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        session.close();
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
        session.close();
        return res;
    }

    public static List<Ballasting> getBallastingDataForGraph(SeqData unit){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Ballasting> query = session.createQuery("FROM Ballasting where streamer = :s and compass = :c order by seq", Ballasting.class);
        query.setParameter("s", unit.getStreamer());
        query.setParameter("c", unit.getUnit());
        List<Ballasting> sp = query.getResultList();
        transaction.commit();
        session.close();
        return sp;
    }

    // Tension tab

    public static List<Tension> getTension(LocalDate current_date, LocalDate until_date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Tension> query = session.createQuery("FROM Tension WHERE date_ between :until_date and :current_date ORDER BY date_",
                Tension.class);
        query.setParameter("current_date", current_date);
        query.setParameter("until_date", until_date);
        List<Tension> result = query.getResultList();
        transaction.commit();
        session.close();
        return result;

    }
}
