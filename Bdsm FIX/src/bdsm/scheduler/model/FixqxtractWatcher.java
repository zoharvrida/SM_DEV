/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.model;

import bdsm.model.BaseModel;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 *
 * @author SDM
 */

@SuppressWarnings("serial")
public class FixqxtractWatcher extends BaseModel implements Serializable {
    /**
     * 
     */
    protected FixqxtractWatcherPK fixqxtractWatcherPK;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private String param6;
    private String costumparam;
    private String flgProcess;
    private String processStatus;
    private Timestamp dtmStartProc;
    private Timestamp dtmProcess;
    private Timestamp dtmFinish;
    private Integer idScheduler;
    private String costumField;
    private Integer qidRef;


    /**
     * 
     */
    public FixqxtractWatcher() {
    }

    /**
     * 
     * @param fixqxtractWatcherPK
     */
    public FixqxtractWatcher(FixqxtractWatcherPK fixqxtractWatcherPK) {
        this.fixqxtractWatcherPK = fixqxtractWatcherPK;
    }

    /**
     * 
     * @param qid
     * @param typeProcess
     * @param namProcess
     */
    public FixqxtractWatcher(Integer qid, Integer typeProcess, String namProcess) {
        this.fixqxtractWatcherPK = new FixqxtractWatcherPK(qid,  typeProcess, namProcess);
    }

    /**
     * 
     * @return
     */
    public FixqxtractWatcherPK getFixqxtractWatcherPK() {
        return fixqxtractWatcherPK;
    }

    /**
     * 
     * @param fixqxtractWatcherPK
     */
    public void setFixqxtractWatcherPK(FixqxtractWatcherPK fixqxtractWatcherPK) {
        this.fixqxtractWatcherPK = fixqxtractWatcherPK;
    }

    /**
     * 
     * @return
     */
    public String getParam1() {
        return param1;
    }

    /**
     * 
     * @param param1
     */
    public void setParam1(String param1) {
        this.param1 = param1;
    }

    /**
     * 
     * @return
     */
    public String getParam2() {
        return param2;
    }

    /**
     * 
     * @param param2
     */
    public void setParam2(String param2) {
        this.param2 = param2;
    }

    /**
     * 
     * @return
     */
    public String getParam3() {
        return param3;
    }

    /**
     * 
     * @param param3
     */
    public void setParam3(String param3) {
        this.param3 = param3;
    }

    /**
     * 
     * @return
     */
    public String getParam4() {
        return param4;
    }

    /**
     * 
     * @param param4
     */
    public void setParam4(String param4) {
        this.param4 = param4;
    }

    /**
     * 
     * @return
     */
    public String getParam5() {
        return param5;
    }

    /**
     * 
     * @param param5
     */
    public void setParam5(String param5) {
        this.param5 = param5;
    }

    /**
     * 
     * @return
     */
    public String getCostumparam() {
        return costumparam;
    }

    /**
     * 
     * @param costumparam
     */
    public void setCostumparam(String costumparam) {
        this.costumparam = costumparam;
    }

    /**
     * 
     * @return
     */
    public String getProcessStatus() {
        return processStatus;
    }

    /**
     * 
     * @param processStatus
     */
    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * 
     * @return
     */
    public Date getDtmStartProc() {
        return dtmStartProc;
    }

    /**
     * 
     * @param dtmStartProc
     */
    public void setDtmStartProc(Timestamp dtmStartProc) {
        this.dtmStartProc = dtmStartProc;
    }

    /**
     * 
     * @return
     */
    public Date getDtmProcess() {
        return dtmProcess;
    }

    /**
     * 
     * @param dtmProcess
     */
    public void setDtmProcess(Timestamp dtmProcess) {
        this.dtmProcess = dtmProcess;
    }

    /**
     * 
     * @return
     */
    public Date getDtmFinish() {
        return dtmFinish;
    }

    /**
     * 
     * @param dtmFinish
     */
    public void setDtmFinish(Timestamp dtmFinish) {
        this.dtmFinish = dtmFinish;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fixqxtractWatcherPK != null ? fixqxtractWatcherPK.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "bdsm.scheduler.model.FixqxtractWatcher[ fixqxtractWatcherPK=" 
                + fixqxtractWatcherPK + " , param1="+param1+" , param2="+param2+" , param3="+param3+" , param4="+param4+" , param5="+param5+" , param6="+getParam6()
                + " , costumparam="+costumparam+"]";
    }

    /**
     * @return the idScheduler
     */
    public Integer getIdScheduler() {
        return idScheduler;
    }

    /**
     * @param idScheduler the idScheduler to set
     */
    public void setIdScheduler(Integer idScheduler) {
        this.idScheduler = idScheduler;
    }

    /**
     * @return the param6
     */
    public String getParam6() {
        return param6;
    }

    /**
     * @param param6 the param6 to set
     */
    public void setParam6(String param6) {
        this.param6 = param6;
    }

    /**
     * @return the costumField
     */
    public String getCostumField() {
        return costumField;
    }

    /**
     * @param costumField the costumField to set
     */
    public void setCostumField(String costumField) {
        this.costumField = costumField;
    }

    /**
     * @return the qidRef
     */
    public Integer getQidRef() {
        return qidRef;
    }

    /**
     * @param qidRef the qidRef to set
     */
    public void setQidRef(Integer qidRef) {
        this.qidRef = qidRef;
    }

	/**
	 * @return the flgProcess
	 */
	public String getFlgProcess() {
		return flgProcess;
	}

	/**
	 * @param flgProcess the flgProcess to set
	 */
	public void setFlgProcess(String flgProcess) {
		this.flgProcess = flgProcess;
	}
    
}
