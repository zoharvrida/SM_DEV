/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.processor;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import bdsm.scheduler.MapKey;
import bdsm.scheduler.StatusDefinition;
import bdsm.scheduler.dao.FixClassConfigDao;
import bdsm.scheduler.dao.FixEmailAccessDao;
import bdsm.scheduler.dao.FixInboxDao;
import bdsm.scheduler.dao.FixLogDao;
import bdsm.scheduler.dao.FixSchedulerXtractDao;
import bdsm.scheduler.dao.TmpKabAcctDAO;
import bdsm.scheduler.exception.FIXException;
import bdsm.scheduler.model.FixClassConfig;
import bdsm.scheduler.model.FixInbox;
import bdsm.scheduler.model.FixLog;
import bdsm.scheduler.model.FixQXtract;
import bdsm.scheduler.model.FixSchedulerXtract;
import bdsm.scheduler.model.TmpKabAcct;
import bdsm.scheduler.model.TmpKabAcctPK;
import bdsm.scheduler.util.FileUtil;
import bdsm.util.SchedulerUtil;
import bdsm.util.excel.XLSReader;
import bdsm.util.excel.XLSXReader;
import bdsmhost.dao.FcrBaBankMastDao;


/**
 * 
 * @author v00019237
 */
public class KabAcctWorker extends BaseProcessor {
	private static final DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	private static String emailApproval = "Dear Sir/Madam,<br/>"
			+ "<br/>"
			+ "Your approval is required for the attached file to be processed further. <br/>"
			+ "<br/>" + "Please reply this email with:<br/>"
			+ "<b>Ok</b>, if you approve the file to be processed, or<br/>"
			+ "<b>Not ok</b>, if otherwise.<br/>" + "<br/>"
			+ "Thanks & regards,<br/>" + "- BDSM -";
	private static String emailDone = "Dear Sir/Madam,<br/>" + "<br/>"
			+ "Process Maintenance KAB Account Data has been Processed. <br/>"
			+ "Please see result Report in Attachment. <br/>" + "<br/>"
			+ "Thanks & regards,<br/>" + "- BDSM -";
	private static String emailRejected = "Dear Sir/Madam,<br/>"
			+ "<br/>"
			+ "Your requested process to Maintenance KAB Account Data has been Rejected by Supervisor. <br/>"
			+ "<br/>" + "Thanks & regards,<br/>" + "- BDSM -";

	
	public KabAcctWorker(Map<? extends String, ? extends Object> context) {
		super(context);
	}
	

	@Override
	protected boolean doExecute() throws Exception {
		String configFile = "excelutil_kabacct.properties";
		FixClassConfig fClassConfig;
		FixSchedulerXtract fixSchedulerXtract;
		FcrBaBankMastDao fcrBABankMastDAO = new FcrBaBankMastDao(this.session);
		TmpKabAcctDAO tmpKabAcctDAO = new TmpKabAcctDAO(this.session);
		FixClassConfigDao classConfigDao = new FixClassConfigDao(this.session);
		FixSchedulerXtractDao fixSchedulerXtractDAO = new FixSchedulerXtractDao(this.session);
		String extFile;
		String batchNo = context.get(MapKey.batchNo).toString();
		String status = context.get(MapKey.status).toString();
		String sourceProcess = context.get(MapKey.processSource).toString();
		String param1 = context.get(MapKey.param1).toString();
		String param5 = (String) context.get(MapKey.param5);
		String filePattern = (String) context.get(MapKey.filePattern);
		int idScheduler = Integer.valueOf(context.get(MapKey.idScheduler).toString());
		int idSchedulerXtract;
		String outFileName;
		
		this.getLogger().info("Begin Execute Worker: " + this.getClass().getName());
		
		if (StatusDefinition.UNAUTHORIZED.equals(status)) {
			String filename = (String) context.get(MapKey.fileName);
			this.getLogger().info("Status : UNAUTHORIZED");
			this.getLogger().info("Param 5 : " + param5);
			this.getLogger().info("Filename : " + filename);
			
			/* Pattern filename */
			try {
				String xtractFilename;
				String dateInFilename;
				java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(filePattern);
				java.util.regex.Matcher matcher = pattern.matcher(filename);
				if (matcher.find()) {
					this.getLogger().info("pattern match start at: " + matcher.start());
					this.getLogger().info("pattern match end at: " + matcher.end());

					xtractFilename = filename.substring(matcher.start(), matcher.end());
					dateInFilename = xtractFilename.substring(xtractFilename.length() - 8);

					dateFormatter.parse(dateInFilename);
					if (!dateFormatter.format(fcrBABankMastDAO.get().getDatProcess()).equals(dateInFilename))
						throw new FIXException("date in filename must be same with business date");
				}
			}
			catch (java.text.ParseException pe) {
				throw new FIXException("Invalid date in filename");
			}
			
			this.readExcel(param5, configFile, tmpKabAcctDAO);
			this.session.flush();
			
			this.getLogger().info("Import Excel file from Requestor Success");
			tmpKabAcctDAO.runValidate(batchNo);
			this.getLogger().info("Filter and Validate Source Data Done");
			fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.UNAUTHORIZED).get(0);
			idSchedulerXtract = fClassConfig.getIdScheduler();
			this.getLogger().info("Getting IdScheduler = " + idSchedulerXtract + " for Source: "
							+ sourceProcess + " and Status : "
							+ StatusDefinition.UNAUTHORIZED + "DONE");
			fixSchedulerXtract = fixSchedulerXtractDAO.get(idSchedulerXtract);
			
			outFileName = FileUtil.getDateTimeFormatedFileName(
					FilenameUtils.getBaseName(param1.replaceFirst(context.get(MapKey.templateName) + "\\s+", "")) + "_{HHmmss}.");
			extFile = fixSchedulerXtract.getFileFormat();
			outFileName += extFile;
			this.getLogger().info("Out File Name : " + outFileName);
			
			if (!sourceProcess.equalsIgnoreCase("sftp")) {
				fixQXtract = new FixQXtract();
				fixQXtract.setIdScheduler(idSchedulerXtract);
				fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
				fixQXtract.setDtmRequest(SchedulerUtil.getTime());
				fixQXtract.setParam1("RE: " + param1);
				FixEmailAccessDao fixEmailAccessDao = new FixEmailAccessDao(session);
				fixQXtract.setParam2(fixEmailAccessDao.getSpv(context.get(MapKey.grpId).toString(), idScheduler));
				fixQXtract.setParam4(emailApproval);
				fixQXtract.setParam5(outFileName);
				fixQXtract.setParam6(batchNo);
				this.getLogger().info("Register FixQXtract Done");
			}
		}
		else if (StatusDefinition.AUTHORIZED.equals(status)) {
			this.getLogger().info("Status : AUTHORIZED");
			
			tmpKabAcctDAO.runProcess(batchNo, context.get(MapKey.emailSender).toString());
			this.getLogger().info("Run process maintenance KAB Account Done");
			
			this.getLogger().info("Register FixQXtract");
			fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.AUTHORIZED).get(0);
			idSchedulerXtract = fClassConfig.getIdScheduler();
			this.getLogger().info("Getting IdScheduler = " + idSchedulerXtract + " for Source: "
							+ sourceProcess + " and Status : "
							+ StatusDefinition.AUTHORIZED + "DONE");
			fixSchedulerXtract = fixSchedulerXtractDAO.get(idSchedulerXtract);
			
			outFileName = FileUtil.getDateTimeFormatedFileName(
					FilenameUtils.getBaseName(param1.replaceFirst(
							"[R|r][E|e]:\\s+" + context.get(MapKey.templateName) + "\\s+", "")) + "_{HHmmss}.");
			extFile = fixSchedulerXtract.getFileFormat();
			outFileName += extFile;
			this.getLogger().info("Out File Name : " + outFileName);
			
			fixQXtract = new FixQXtract();
			fixQXtract.setIdScheduler(idSchedulerXtract);
			fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
			fixQXtract.setDtmRequest(SchedulerUtil.getTime());
			fixQXtract.setParam1(param1);
			
			FixInboxDao fixInboxDao = new FixInboxDao(session);
			if (!context.get(MapKey.itemIdLink).toString().equals(""))
				fixQXtract.setParam2(fixInboxDao.get(context.get(MapKey.itemIdLink).toString()).getSender());
			if (context.get(MapKey.spvAuth).toString().equals("N"))
				fixQXtract.setParam2(context.get(MapKey.param2).toString());
			
			FixEmailAccessDao fixEmailAccessDao = new FixEmailAccessDao(session);
			fixQXtract.setParam3(fixEmailAccessDao.getSpv(context.get(MapKey.grpId).toString(), idScheduler));
			fixQXtract.setParam4(emailDone);
			fixQXtract.setParam5(outFileName);
			fixQXtract.setParam6(batchNo);
			
			this.getLogger().info("Register FixQXtract Done");
		}
		else if (StatusDefinition.REJECTED.equals(status)) {
			this.getLogger().info("Status : REJECTED");
			this.getLogger().info("Register FixQXtract");
			
			tmpKabAcctDAO.runReject(batchNo, context.get(MapKey.emailSender).toString());
			fixQXtract = new FixQXtract();
			fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.REJECTED).get(0);
			idSchedulerXtract = fClassConfig.getIdScheduler();
			fixQXtract.setIdScheduler(idSchedulerXtract);
			fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
			fixQXtract.setDtmRequest(SchedulerUtil.getTime());
			fixQXtract.setParam1(param1);
			FixInboxDao fixInboxDao = new FixInboxDao(session);
			
			if (!context.get(MapKey.itemIdLink).toString().equals(""))
				fixQXtract.setParam2(fixInboxDao.get(context.get(MapKey.itemIdLink).toString()).getSender());
			
			fixQXtract.setParam4(emailRejected);
			fixQXtract.setParam5("");
			
			this.getLogger().info("Register FixQXtract");
		}
		else {
			this.getLogger().info("Status : IGNORED");
			// maybe ignored
			FixInboxDao fixInboxDao = new FixInboxDao(session);
			FixInbox fixInbox = fixInboxDao.get(context.get(MapKey.inboxId).toString());
			fixInbox.setFlgProcess(StatusDefinition.IGNORED);
			fixInboxDao.update(fixInbox);
		}
		
		return true;
	}

	private void readExcel(String param5, String configFile, TmpKabAcctDAO tmpKabAcctDAO) {
		if (FilenameUtils.getExtension(param5).equalsIgnoreCase("xls"))
			readXLS(param5, configFile, tmpKabAcctDAO);
		else if (FilenameUtils.getExtension(param5).equalsIgnoreCase("xlsx"))
			readXLSX(param5, configFile, tmpKabAcctDAO);
	}

	private void readXLS(String param5, String configFile, TmpKabAcctDAO tmpKabAcctDAO) {
		XLSReader xr = XLSReader.getInstance(param5, configFile);
		TmpKabAcct tmpKabAcct;
		TmpKabAcctPK tmpKabAcctPK;
		String[] propertyNames = {"compositeId.account", "compositeId.typeAccount", "command"};
		
		while(xr.hasNextRow()) {
			tmpKabAcct = new TmpKabAcct();
			tmpKabAcctPK = new TmpKabAcctPK();

			tmpKabAcctPK.setBatchNo(context.get(MapKey.batchNo).toString());
			tmpKabAcct.setCompositeId(tmpKabAcctPK);
			tmpKabAcct.setDtmCreated(SchedulerUtil.getTime());
			tmpKabAcct.setIdMaintainedBy(context.get(MapKey.emailSender).toString());
			
			try {
				if (xr.nextRow(tmpKabAcct, propertyNames))
					tmpKabAcctDAO.insert(tmpKabAcct);
			}
			catch(Exception ex) {
				tmpKabAcct.setFlagStatus(StatusDefinition.REJECTED);
				tmpKabAcct.setStatusReason(ex.toString());
			}
		}
	}

	private void readXLSX(String param5, String configFile, TmpKabAcctDAO tmpKabAcctDAO) {
		XLSXReader xr = XLSXReader.getInstance(param5, configFile);
		TmpKabAcct tmpKabAcct;
		TmpKabAcctPK tmpKabAcctPK;
		String[] propertyNames = {"compositeId.account", "compositeId.typeAccount", "command"};
		
		while(xr.hasNextRow()) {
			tmpKabAcct = new TmpKabAcct();
			tmpKabAcctPK = new TmpKabAcctPK();

			tmpKabAcctPK.setBatchNo(context.get(MapKey.batchNo).toString());
			tmpKabAcct.setCompositeId(tmpKabAcctPK);
			tmpKabAcct.setDtmCreated(SchedulerUtil.getTime());
			tmpKabAcct.setIdMaintainedBy(context.get(MapKey.emailSender).toString());
			
			try {
				if (xr.nextRow(tmpKabAcct, propertyNames))
					tmpKabAcctDAO.insert(tmpKabAcct);
			}
			catch(Exception ex) {
				tmpKabAcct.setFlagStatus(StatusDefinition.REJECTED);
				tmpKabAcct.setStatusReason(ex.toString());
			}
		}
	}
}
