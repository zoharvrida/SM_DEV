/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.dao;

import bdsm.model.BaseModel;
import bdsm.scheduler.model.TmpMoveCustomers;
import bdsmhost.dao.BaseDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 *
 * @author v00020841
 */
public class TmpMoveCustomersDao extends BaseDao implements Work{

    public TmpMoveCustomersDao(Session session) {
        super(session);
    }

    public List<TmpMoveCustomers> getBybatchNo(String param){
        Query query = getSession().createQuery("from TmpMoveCustomers where batchNo = :paramBatch");
        query.setString("paramBatch", param);
        return query.list();
    }
    
    @Override
    protected boolean doInsert(BaseModel model) {
        getSession().save((TmpMoveCustomers) model);
        return true;
    }

    @Override
    protected boolean doUpdate(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean doDelete(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void execute(Connection cnctn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
