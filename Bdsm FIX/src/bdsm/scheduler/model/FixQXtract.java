package bdsm.scheduler.model;

import bdsm.model.BaseModel;
import java.sql.Timestamp;

/**
 * 
 * @author bdsm
 */
public class FixQXtract extends BaseModel {

    private Integer qId;
    private Integer idScheduler;
    private Timestamp dtmRequest;
    private Timestamp dtmProcess;
    private Timestamp dtmFinish;
    private String flgProcess;
    private String reason;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private String param6;

    /**
     * @return the qId
     */
    public Integer getqId() {
        return qId;
    }

    /**
     * @param qId the qId to set
     */
    public void setqId(Integer qId) {
        this.qId = qId;
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

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        if (reason != null) {
            if (reason.length() > 200) {
                this.reason = reason.substring(0, 200);
            } else {
                this.reason = reason;
            }
        }
    }

    /**
     * @return the param1
     */
    public String getParam1() {
        return param1;
    }

    /**
     * @param param1 the param1 to set
     */
    public void setParam1(String param1) {
        this.param1 = param1;
    }

    /**
     * @return the param2
     */
    public String getParam2() {
        return param2;
    }

    /**
     * @param param2 the param2 to set
     */
    public void setParam2(String param2) {
        this.param2 = param2;
    }

    /**
     * @return the param3
     */
    public String getParam3() {
        return param3;
    }

    /**
     * @param param3 the param3 to set
     */
    public void setParam3(String param3) {
        this.param3 = param3;
    }

    /**
     * @return the param4
     */
    public String getParam4() {
        return param4;
    }

    /**
     * @param param4 the param4 to set
     */
    public void setParam4(String param4) {
        if (param4 != null) {
            if (param4.length() > 4000) {
                this.param4 = param4.substring(0, 4000);
            } else {
                this.param4 = param4;
            }
        }
    }

    /**
     * @return the param5
     */
    public String getParam5() {
        return param5;
    }

    /**
     * @param param5 the param5 to set
     */
    public void setParam5(String param5) {
        this.param5 = param5;
    }

    /**
     * @return the dtmRequest
     */
    public Timestamp getDtmRequest() {
        return dtmRequest;
    }

    /**
     * @param dtmRequest the dtmRequest to set
     */
    public void setDtmRequest(Timestamp dtmRequest) {
        this.dtmRequest = dtmRequest;
    }

    /**
     * @return the dtmProcess
     */
    public Timestamp getDtmProcess() {
        return dtmProcess;
    }

    /**
     * @param dtmProcess the dtmProcess to set
     */
    public void setDtmProcess(Timestamp dtmProcess) {
        this.dtmProcess = dtmProcess;
    }

    /**
     * @return the dtmFinish
     */
    public Timestamp getDtmFinish() {
        return dtmFinish;
    }

    /**
     * @param dtmFinish the dtmFinish to set
     */
    public void setDtmFinish(Timestamp dtmFinish) {
        this.dtmFinish = dtmFinish;
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
    @Override
    public String toString() {
        return "FixQXtract{" + "qId=" + qId + ", idScheduler=" + idScheduler + ", dtmRequest=" + dtmRequest + ", dtmProcess=" + dtmProcess + ", dtmFinish=" + dtmFinish + ", flgProcess=" + flgProcess + ", reason=" + reason + ", param1=" + param1 + ", param2=" + param2 + ", param3=" + param3 + ", param4=" + param4 + ", param5=" + param5 + ", param6=" + param6 + '}';
    }
}
