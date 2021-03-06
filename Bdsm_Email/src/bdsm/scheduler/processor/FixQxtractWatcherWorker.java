/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.processor;

import bdsm.scheduler.dao.FieldParamaterDao;
import bdsm.scheduler.dao.FixQXtractDao;
import bdsm.scheduler.dao.FixQxtractWatcherDao;
import bdsm.scheduler.model.FieldParameterPK;
import bdsm.scheduler.model.FixQXtract;
import bdsm.scheduler.model.FixqxtractWatcher;
import bdsm.scheduler.model.FixqxtractWatcherPK;
import bdsm.scheduler.model.FieldParameter;
import bdsm.scheduler.util.SchedulerUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SDM
 */
public class FixQxtractWatcherWorker extends BaseProcessor {

    protected FixQXtract qx = new FixQXtract();
    protected FixqxtractWatcher objGlobal = new FixqxtractWatcher();

    public FixQxtractWatcherWorker(Map context) {
        super(context);
    }

    @Override
    protected boolean doExecute() throws Exception {
        FixQxtractWatcherDao fixqWdao = new FixQxtractWatcherDao(session);
        FixQXtractDao gFixqDao = new FixQXtractDao(session);
        FieldParamaterDao paramDao = new FieldParamaterDao(session);
        getLogger().info("[BEGIN] FIXQXTRACT WATCHER >>> ");
        List<FixqxtractWatcher> watcher = fixqWdao.get("Q", "0");
        getLogger().info("[LIST] Number of Watcher : " + watcher.size());
        for (FixqxtractWatcher objWatcher : watcher) {
            if (objWatcher != null) {
                List<FixQXtract> lQxtract = gFixqDao.getQXtractbasedWatcher(objWatcher.getFixqxtractWatcherPK().getQid(), objWatcher.getIdScheduler(), objWatcher.getParam6());
                if (!lQxtract.isEmpty()) {
                    this.qx = lQxtract.get(0);
                    bdsm.util.ClassConverterUtil.ReferenceClass(objWatcher, this.qx);
                    getLogger().info("SOURCE :" + objWatcher.toString());
                    getLogger().info("Dest :" + this.qx.toString());
                    if ("D".equalsIgnoreCase(this.qx.getFlgProcess())) {
                        // update Fixqxtract watcher
                        runprocess("D");
                    } else if ("E".equalsIgnoreCase(this.qx.getFlgProcess())) {
                        // update Fixqxtract watcher, check retention, get QID, update and flagback to Q
                        FieldParameterPK fpk = new FieldParameterPK();
                        FieldParameter fParam = paramDao.get(fpk);
                        if (Integer.parseInt(fParam.getFormatRule()) > 0) {
                            // this profile has repeat period
                            List errorList = new ArrayList();
                            if(objWatcher.getQidRef()==0){
                                errorList = fixqWdao.getAllError(objWatcher.getFixqxtractWatcherPK().getQid(), objWatcher.getParam6(), objWatcher.getIdScheduler());
                            } else {
                                errorList = fixqWdao.getAllError(objWatcher.getQidRef(), objWatcher.getParam6(), objWatcher.getIdScheduler());
                            }
                            
                            if (errorList.size() <= Integer.parseInt(fParam.getFormatRule())) {
                                this.qx.setIdScheduler(objWatcher.getIdScheduler());
                            this.qx.setFlgProcess("Q");
                            this.qx.setDtmRequest(SchedulerUtil.getTime());
                            gFixqDao.insert(qx);

                                objWatcher.setFlgProcess("E");
                                objWatcher.setProcessStatus("0");
                                objWatcher.setDtmFinish(SchedulerUtil.getTime());
                            fixqWdao.update(objWatcher);
                            this.tx.commit();
                            this.tx = this.session.beginTransaction();
                            objWatcher.setQidRef(objWatcher.getFixqxtractWatcherPK().getQid());
                            FixqxtractWatcherPK pk = new FixqxtractWatcherPK();
                                getLogger().info("[QID] Error qid : " + this.qx.getqId());
                            pk.setQid(this.qx.getqId());
                                pk.setTypeProcess(objWatcher.getFixqxtractWatcherPK().getTypeProcess());
                                pk.setNamProcess(objWatcher.getFixqxtractWatcherPK().getNamProcess());
                            objWatcher.setFixqxtractWatcherPK(pk);
                            objWatcher.setDtmStartProc(SchedulerUtil.getTime());
                            fixqWdao.insert(objWatcher);
                            } else {
                                runprocess("E");
                        }
                            
                        }

                    }
                }
            }
        }
        getLogger().info("[END] FIXQXTRACT WATCHER >>> ");
        return true;
    }

    protected void runprocess(String status) {
        FixQxtractWatcherDao fixqWdao = new FixQxtractWatcherDao(session);
        FixQXtractDao gFixqDao = new FixQXtractDao(session);
        FieldParamaterDao paramDao = new FieldParamaterDao(session);
        
        if (objGlobal.getFixqxtractWatcherPK().getTypeProcess().equals(0)) {
            // java Class
            this.qx.setFlgProcess("Q");
            this.qx.setDtmRequest(SchedulerUtil.getTime());
            this.qx.setIdScheduler(Integer.parseInt(objGlobal.getFixqxtractWatcherPK().getNamProcess()));
            gFixqDao.insert(qx);
            objGlobal.setFlgProcess(status);
            objGlobal.setProcessStatus("1");
            objGlobal.setDtmProcess(SchedulerUtil.getTime());
            fixqWdao.update(objGlobal);
            this.tx.commit();
            this.tx = this.session.beginTransaction();
            objGlobal.setQidRef(objGlobal.getFixqxtractWatcherPK().getQid());
            // INSERT SINGLE RECORD AS LAST WATCHER CHAIN PROCESS
            getLogger().info("[QID] RunProcess :"+status+" qid : " + this.qx.getqId());
            FixqxtractWatcherPK pk = new FixqxtractWatcherPK();
            pk.setQid(this.qx.getqId());
            pk.setTypeProcess(2);
            pk.setNamProcess("");
            objGlobal.setFixqxtractWatcherPK(pk);
            objGlobal.setFlgProcess("Q");
            objGlobal.setProcessStatus("0");
            objGlobal.setDtmStartProc(SchedulerUtil.getTime());
            fixqWdao.insert(objGlobal);
        } else if (objGlobal.getFixqxtractWatcherPK().getTypeProcess().equals(1)) {
            // Package call
            FieldParameterPK fpk = new FieldParameterPK();
            FieldParameter fParam = paramDao.get(fpk);
            String delimiter = fParam.getFormat();
            String pckResult = fixqWdao.runQuery(objGlobal.getFixqxtractWatcherPK().getNamProcess(), objGlobal.getCostumparam(), objGlobal.getCostumField(), delimiter);
            getLogger().info("RUNNING PACKAGE SUCCESS :" + pckResult);
        } else if (objGlobal.getFixqxtractWatcherPK().getTypeProcess().equals(2)) {
            objGlobal.setFlgProcess(status);
            objGlobal.setProcessStatus("2");
            objGlobal.setDtmFinish(SchedulerUtil.getTime());
            fixqWdao.update(objGlobal);
        }
    }
}
