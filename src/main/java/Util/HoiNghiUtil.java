package Util;

import model.DSThamGiaHN;
import model.HoiNghi;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HoiNghiUtil {
    public static List<HoiNghi> getData(){
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        List<HoiNghi> list=session.createQuery("From HoiNghi order by THOIGIANBD desc").getResultList();

        transaction.commit();
        session.close();
        return list;
    }
}
