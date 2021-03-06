package bdsm.scheduler.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import bdsm.model.BaseModel;
import bdsm.scheduler.model.TmpIncDrNoteFooter;
import bdsm.scheduler.model.TmpIncDrNotePK;


/**
*
* @author v00019372
*/
public class TmpIncDrNoteFooterDAO extends bdsmhost.dao.BaseDao {
	
	public TmpIncDrNoteFooterDAO(Session session) {
		super(session);
	}
	

	public TmpIncDrNoteFooter get(TmpIncDrNotePK pk) {
		return (TmpIncDrNoteFooter) this.getSession().get(TmpIncDrNotePK.class, pk);
	}
	
	public TmpIncDrNoteFooter get(String fileId, Long recordId) {
		TmpIncDrNotePK pk = new TmpIncDrNotePK(fileId, recordId);
		return (TmpIncDrNoteFooter) this.getSession().get(TmpIncDrNotePK.class, pk);
	}
	
	@SuppressWarnings("unchecked")
	public TmpIncDrNoteFooter getFooterByFileId(String fileId) {
		Criteria crt = this.getSession().createCriteria(TmpIncDrNoteFooter.class);
		crt.add(Restrictions.eq("compositeId.fileId", fileId));
		
		
		List<TmpIncDrNoteFooter> list = crt.list();
		if (list.size() > 0) 
			return list.get(0);
		else
			return null;
	}
	

	@Override
	protected boolean doInsert(BaseModel model) {
		this.getSession().save((TmpIncDrNoteFooter) model);
		return true;
	}

	@Override
	protected boolean doUpdate(BaseModel model) {
		this.getSession().update((TmpIncDrNoteFooter) model);
		return true;
	}

	@Override
	protected boolean doDelete(BaseModel model) {
		this.getSession().delete((TmpIncDrNoteFooter) model);
		return true;
	}

}
