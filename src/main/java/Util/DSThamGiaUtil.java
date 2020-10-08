package Util;

import model.DSThamGiaHN;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public  class DSThamGiaUtil {
     public static List<DSThamGiaHN> getData(){
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        List<DSThamGiaHN> list=session.createQuery("From DSThamGiaHN").getResultList();

        transaction.commit();
        session.close();
        return list;
    }
}
