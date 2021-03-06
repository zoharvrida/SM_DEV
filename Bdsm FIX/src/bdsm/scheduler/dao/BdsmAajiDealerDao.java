/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.dao;

import bdsm.model.BaseModel;
import bdsm.scheduler.model.UcfTmpSrc;
import bdsmhost.dao.BaseDao;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.impl.SessionImpl;
import org.hibernate.jdbc.Work;

/**
 *
 * @author NCBS
 */
public class BdsmAajiDealerDao extends BaseDao implements Work {
    private String parameter = null;
    private static final String queryUpload = "{call PK_ULC_TMP.updateOldValue(?)}";
    private static final String queryInsert = "{call PK_ULC_TMP.insertULCUDF(?)}";
    private static final String queryUpdate = "{call PK_ULC_TMP.updateULCUDF(?)}";
    private static final String queryReject = "{call PK_ULC_TMP.rejectULCUDF(?)}";
    private int workResult1 = 0;
    private int workResult2 = 0;
    private int workResult3 = 0;
    private int workResult4 = 0;
    private int workType = 0;

    /**
     * 
     * @param session
     */
    public BdsmAajiDealerDao(Session session) {
        super(session);
    }

    /**
     * 
     * @param param
     * @return
     */
    public List<UcfTmpSrc> get(String param) {        
        Query query = getSession().createQuery("from UlcTmpSrc where batch = :prm_batch");
        query.setString("prm_batch", param);
        return query.list();
    }

    /**
     * 
     * @param model
     * @return
     */
    @Override
    protected boolean doInsert(BaseModel model) {
        getSession().save((UcfTmpSrc) model);
        return true;
    }

    /**
     * 
     * @param model
     * @return
     */
    @Override
    protected boolean doUpdate(BaseModel model) {
        getSession().update((UcfTmpSrc) model);
        return true;
    }

    /**
     * 
     * @param model
     * @return
     */
    @Override
    protected boolean doDelete(BaseModel model) {
        getSession().delete((UcfTmpSrc) model);
        return true;
    }

    /**
     * 
     * @param cnctn
     */
    public void execute(Connection cnctn) {
        try {
            String query = null;
            switch (workType) {
                case 1:
                    query = queryUpload;
                    break;
                case 2:
                    query = queryInsert;
                    break;
                case 3:
                    query = queryUpdate;
                    break;
                case 4:
                    query = queryReject;
                    break;                    
            }
            CallableStatement stmt = cnctn.prepareCall(query);
            stmt.setString(1, parameter);
            int workRes = stmt.executeUpdate();
            stmt.close();
            switch (workType) {
                case 1:
                    workResult1 = workRes;
                    break;
                case 2:
                    workResult2 = workRes;
                    break;
                case 3:
                    workResult3 = workRes;
                    break;
                case 4:
                    workResult4 = workRes;
                    break;                    
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * 
     * @param param
     * @return
     */
    public int runUploadUlc(String param) {
        Session session = getSession();
        workType = 1;
        parameter = param;
        session.doWork(this);
        return workResult1;
    }

    /**
     * 
     * @param param
     * @return
     */
    public int runInsertUlc(String param) {
        Session session = getSession();
        workType = 2;
        parameter = param;
        session.doWork(this);
        return workResult2;
    }

    /**
     * 
     * @param param
     * @return
     */
    public int runUpdateUlc(String param) {
        Session session = getSession();
        workType = 3;
        parameter = param;
        session.doWork(this);
        return workResult3;
    }

    /**
     * 
     * @param param
     * @return
     */
    public int runRejectUlc(String param) {
        Session session = getSession();
        workType = 4;
        parameter = param;
        session.doWork(this);
        return workResult4;
    }

    /**
     * 
     * @param prm
     * @return
     * @throws SQLException
     */
    public List<String[]> getUploadedLBU(String prm) throws SQLException {
        List<String[]> dt = null;
        String query = "select ROWNUM,ACCOUNT_NO,FIELD_TAG,TASK_CODE,"
                + "COMMAND,OLD_VALUE,NEW_VALUE,EXPECTED_NEW_VALUE,STATUS,"
                + "STATUS_REASON from v_rpt_ulc_tmp_udf_acct where batch_no=?";
        Session session = getSession();
        Connection conn = ((SessionImpl) session).connection();
        CallableStatement stmt = conn.prepareCall(query);
        stmt.setString(1, prm);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        dt = new ArrayList<String[]>();
        String[] row = null;
        /* batch  */
        row = new String[rsmd.getColumnCount()];
        row[0] = "Batch No";
        row[1] = prm;
        row[2] = "";
        row[3] = "";
        row[4] = "";
        row[5] = "";
        row[6] = "";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        dt.add(row);
        /* end batch */
        /* business date  */
        row = new String[rsmd.getColumnCount()];
        row[0] = "Business Date";
        row[1] = new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date());
        row[2] = "";
        row[3] = "";
        row[4] = "";
        row[5] = "";
        row[6] = "";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        dt.add(row);
        /* end biz date */
        /* date  uploaded*/
        row = new String[rsmd.getColumnCount()];
        row[0] = "Uploaded Date";
        row[1] = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new java.util.Date());
        row[2] = "";
        row[3] = "";
        row[4] = "";
        row[5] = "";
        row[6] = "";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        dt.add(row);
        /* end date uploaded */
        /* null row */
        row = new String[rsmd.getColumnCount()];
        row[0] = "";
        row[1] = "";
        row[2] = "";
        row[3] = "";
        row[4] = "";
        row[5] = "";
        row[6] = "";
        row[7] = "";
        row[8] = "";
        row[9] = "";
        dt.add(row);
        /* end null row */
        /* header */
        row = new String[rsmd.getColumnCount()];
        row[0] = "NO";
        row[1] = "ACCOUNT_NO";
        row[2] = "FIELD_TAG";
        row[3] = "TASK_CODE";
        row[4] = "COMMAND";
        row[5] = "OLD_VALUE";
        row[6] = "NEW_VALUE";
        row[7] = "EXPECTED_NEW_VALUE";
        row[8] = "STATUS";
        row[9] = "STATUS_REASON";
        dt.add(row);
        /* end header */
        while (rs.next()) {
            row = new String[rsmd.getColumnCount()];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                row[i] = rs.getString(i + 1);
            }
            dt.add(row);

        }
        conn.close();
        return dt;
    }
}
