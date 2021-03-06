/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.rpt.dao;

import bdsm.model.BaseModel;
import bdsm.rpt.model.FixReportParam;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author v00019722
 */
public class FixReportParamDao extends BaseDao {
    public FixReportParamDao(Session session) {
        super(session);
    }
    
    public List list(String idReport) {
        Criteria criteriaQuery = getSession().createCriteria(FixReportParam.class);
        criteriaQuery.add(Restrictions.eq("compositeId.idReport",idReport));
        criteriaQuery.addOrder(Order.asc("compositeId.seq"));
        return criteriaQuery.list();
    }
    
    public FixReportParam get(String idReport) {
        return (FixReportParam) getSession().get(FixReportParam.class, idReport);
    }
 
    @Override
    protected boolean doInsert(BaseModel model) {
        getSession().save(model);
        return true;
    }

    @Override
    protected boolean doUpdate(BaseModel model) {
        getSession().update(model);
        return true;
    }

    @Override
    protected boolean doDelete(BaseModel model) {
        getSession().delete(model);
        return true;
    }
}
