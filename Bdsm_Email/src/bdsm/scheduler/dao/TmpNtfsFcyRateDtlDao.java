/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.dao;

import bdsm.model.BaseModel;
import bdsm.scheduler.model.TmpNtfsFcyRateDtl;
import bdsmhost.dao.BaseDao;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author bdsm
 */
public class TmpNtfsFcyRateDtlDao extends BaseDao {

    public TmpNtfsFcyRateDtlDao(Session session) {
        super(session);
    }

    public List<TmpNtfsFcyRateDtl> list(String batch){
        Query q = getSession().createQuery("from TmpNtfsFcyRateDtl where bdsmBatch=:batch");
        q.setString("batch", batch);
        return q.list();
    }

    @Override
    protected boolean doInsert(BaseModel model) {
        getSession().save((TmpNtfsFcyRateDtl) model);
        return true;
    }

    @Override
    protected boolean doUpdate(BaseModel model) {
        getSession().update((TmpNtfsFcyRateDtl) model);
        return true;
    }

    @Override
    protected boolean doDelete(BaseModel model) {
        getSession().delete((TmpNtfsFcyRateDtl) model);
        return true;
    }
}
