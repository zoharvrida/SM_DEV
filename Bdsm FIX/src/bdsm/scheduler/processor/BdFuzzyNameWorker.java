/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.processor;

import bdsm.scheduler.MapKey;
import bdsm.scheduler.PropertyPersister;
import bdsm.scheduler.StatusDefinition;
import bdsm.scheduler.dao.FixClassConfigDao;
import bdsm.scheduler.dao.FixEmailAccessDao;
import bdsm.scheduler.dao.FixInboxDao;
import bdsm.scheduler.dao.FixSchedulerXtractDao;
import bdsm.scheduler.dao.TmpBdFuzzyNameDao;
import bdsm.scheduler.model.FixClassConfig;
import bdsm.scheduler.model.FixInbox;
import bdsm.scheduler.model.FixQXtract;
import bdsm.scheduler.model.FixSchedulerXtract;
import bdsm.scheduler.model.TmpBdFuzzyName;
import bdsm.scheduler.util.FileUtil;
import bdsm.util.SchedulerUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author v00019237
 */
public class BdFuzzyNameWorker extends BaseProcessor {

    private String emailApproval = "Dear Sir/Madam,<br/>"
            + "<br/>"
            + "Your approval is required for the attached file to be processed further. <br/>"
            + "<br/>"
            + "Please reply this email with:<br/>"
            + "<b>Ok</b>, if you approve the file to be processed, or<br/>"
            + "<b>Not ok</b>, if otherwise.<br/>"
            + "<br/>"
            + "Thanks & regards,<br/>"
            + "- BDSM -";
    private String emailDone = "Dear Sir/Madam,<br/>"
            + "<br/>"
            + "Process Upload BD Fuzzy Name Data has been Processed. <br/>"
            + "Please see result Report in Attachment. <br/>"
            + "<br/>"
            + "Thanks & regards,<br/>"
            + "- BDSM -";
    private String emailRejected = "Dear Sir/Madam,<br/>"
            + "<br/>"
            + "Your requested process to Upload BD Fuzzy Name has been Rejected by Supervisor. <br/>"
            + "<br/>"
            + "Thanks & regards,<br/>"
            + "- BDSM -";

    /**
     * 
     * @param context
     */
    public BdFuzzyNameWorker(Map context) {
        super(context);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @Override
    protected boolean doExecute() throws Exception {
        String configFile;
        configFile = "excelutil_bdfuzzy.properties";
        TmpBdFuzzyNameDao tmpBdFuzzyNameDao = new TmpBdFuzzyNameDao(session);
        FixClassConfigDao classConfigDao = new FixClassConfigDao(session);
        FixSchedulerXtractDao fixSchedulerXtractDao = new FixSchedulerXtractDao(session);
        TmpBdFuzzyName tmpBdFuzzyName = null;
        FixClassConfig fClassConfig = null;
        FixSchedulerXtract fixSchedulerXtract = null;
        String extFile = "";
        String BatchID = context.get(MapKey.batchNo).toString();
        String status = context.get(MapKey.status).toString();
        String param1 = context.get(MapKey.param1).toString();
        String sourceProcess = context.get(MapKey.processSource).toString();
        int idScheduler = Integer.valueOf(context.get(MapKey.idScheduler).toString());
        getLogger().info("Done Prepare before execute status U/A");
        if (status.equals(StatusDefinition.UNAUTHORIZED)) {
            String outFileName = FileUtil.getDateTimeFormatedFileName(FilenameUtils.getBaseName(context.get(MapKey.param1).toString().replace("UDFULC ", "")) + "{hhmmss}.xls");
            getLogger().info("Out File Name : " + outFileName);
            String param5 = context.get(MapKey.param5).toString();
            getLogger().info("Param 5 : " + param5);

            readExcel(param5, configFile, tmpBdFuzzyName, tmpBdFuzzyNameDao);

            //buat ilangin bug
            tmpBdFuzzyNameDao.get(BatchID);
            getLogger().info("BD Fuzzy : Import Excel file from Requestor Success");

            // do filter and validation
            tmpBdFuzzyNameDao.runValidateSource(BatchID,context.get(MapKey.emailSender).toString());
            getLogger().info("BD Fuzzy : Filter and Validate Source Data Done");

            fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.UNAUTHORIZED).get(0);
            idScheduler = fClassConfig.getIdScheduler();
            getLogger().info("Getting IdScheduler = " + idScheduler + " for Source: " + sourceProcess + " and Status : " + StatusDefinition.UNAUTHORIZED + "DONE");
            fixSchedulerXtract = fixSchedulerXtractDao.get(idScheduler);
            extFile = fixSchedulerXtract.getFileFormat();
            outFileName = outFileName.replace(".xls", "." + extFile);
            getLogger().info("Getting file Extension Done : " + outFileName);
            if (!sourceProcess.equalsIgnoreCase("sftp")) {
                fixQXtract = new FixQXtract();
                fixQXtract.setIdScheduler(idScheduler);
                fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
                fixQXtract.setDtmRequest(SchedulerUtil.getTime());
                fixQXtract.setParam1("RE: " + param1);
                FixEmailAccessDao fixEmailAccessDao = new FixEmailAccessDao(session);
                fixQXtract.setParam2(fixEmailAccessDao.getSpv(context.get(MapKey.grpId).toString()));
                fixQXtract.setParam4(PropertyPersister.emailApproval);
                fixQXtract.setParam5(outFileName);
                fixQXtract.setParam6(BatchID);
                getLogger().info("BD Fuzzy Register FixQXtract Done");
            }
        } else if (status.equals(StatusDefinition.AUTHORIZED)) {
            getLogger().info("Status : AUTHORIZED");
            tmpBdFuzzyNameDao.runInsert(BatchID, context.get(MapKey.emailSender).toString());
            getLogger().info("Run update to FCR Done");
            String out2FileName = FileUtil.getDateTimeFormatedFileName(FilenameUtils.getBaseName(context.get(MapKey.param1).toString().replace("RE: BDFZNM ", "")) + "{hhmmss}.xls");
            getLogger().info("Register FixQXtract");
            fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.AUTHORIZED).get(0);
            idScheduler = fClassConfig.getIdScheduler();
            getLogger().info("Getting IdScheduler = " + idScheduler + " for Source: " + sourceProcess + " and Status : " + StatusDefinition.AUTHORIZED + "DONE");
            fixSchedulerXtract = fixSchedulerXtractDao.get(idScheduler);
            extFile = fixSchedulerXtract.getFileFormat();
            out2FileName = out2FileName.replace(".xls", "." + extFile);
            getLogger().info("Getting file Extention DONE : " + out2FileName);
            fixQXtract = new FixQXtract();
            fixQXtract.setIdScheduler(idScheduler);
            fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
            fixQXtract.setDtmRequest(SchedulerUtil.getTime());
            fixQXtract.setParam1(param1);
            FixEmailAccessDao fixEmailAccessDao = new FixEmailAccessDao(session);
            fixQXtract.setParam3(fixEmailAccessDao.getSpv(context.get(MapKey.grpId).toString()));
            FixInboxDao fixInboxDao = new FixInboxDao(session);
            if (!context.get(MapKey.itemIdLink).toString().equals("")) {
                fixQXtract.setParam2(fixInboxDao.get(context.get(MapKey.itemIdLink).toString()).getSender());
            }
            if (context.get(MapKey.spvAuth).toString().equals("N")) {
                fixQXtract.setParam2(context.get(MapKey.param2).toString());
            }
            fixQXtract.setParam4(this.emailDone);
            fixQXtract.setParam5(out2FileName);
            fixQXtract.setParam6(BatchID);
            getLogger().info("Register FixQXtract Done");
        } else if (status.equals(StatusDefinition.REJECTED)) {
            getLogger().info("Status : REJECTED");
            getLogger().info("Register FixQXtract");
            tmpBdFuzzyNameDao.runReject(BatchID, context.get(MapKey.emailSender).toString());
            fixQXtract = new FixQXtract();
            fClassConfig = classConfigDao.get(getClass().getName(), sourceProcess, StatusDefinition.REJECTED).get(0);
            idScheduler = fClassConfig.getIdScheduler();
            fixQXtract.setIdScheduler(idScheduler);
            fixQXtract.setFlgProcess(StatusDefinition.REQUEST);
            fixQXtract.setDtmRequest(SchedulerUtil.getTime());
            fixQXtract.setParam1(param1);
            FixInboxDao fixInboxDao = new FixInboxDao(session);
            if (!context.get(MapKey.itemIdLink).toString().equals("")) {
                fixQXtract.setParam2(fixInboxDao.get(context.get(MapKey.itemIdLink).toString()).getSender());
            }
            fixQXtract.setParam4(this.emailRejected);
            fixQXtract.setParam5("");
            getLogger().info("Register FixQXtract");

        } else {
            getLogger().info("Status : IGNORED");
            //maybe ignored
            FixInboxDao fixInboxDao = new FixInboxDao(session);
            FixInbox fixInbox = fixInboxDao.get(context.get(MapKey.inboxId).toString());
            fixInbox.setFlgProcess(StatusDefinition.IGNORED);
            fixInboxDao.update(fixInbox);
        }
        return true;
    }

    private void readExcel(String param5, String configFile, TmpBdFuzzyName tmpBdFuzzyName, TmpBdFuzzyNameDao tmpBdFuzzyNameDao) throws Exception {
        loadConfig(configFile);
        if (FilenameUtils.getExtension(param5).equalsIgnoreCase("xls")) {
            readXLS(param5, tmpBdFuzzyName, tmpBdFuzzyNameDao);
        } else if (FilenameUtils.getExtension(param5).equalsIgnoreCase("xlsx")) {
            readXLSX(param5, tmpBdFuzzyName, tmpBdFuzzyNameDao);
        }
    }

    private void readXLS(String param5, TmpBdFuzzyName tmpBdFuzzyName, TmpBdFuzzyNameDao tmpBdFuzzyNameDao) throws Exception {
        BufferedInputStream file = new BufferedInputStream(new FileInputStream(new File(param5)));
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(sheetData - 1);

            if (rowCnt == 0) {
                rowCnt = sheet.getPhysicalNumberOfRows() + rowDataStartOn - headerRow;
            }
            for (int i = rowDataStartOn - headerRow; i < rowCnt - headerRow; i++) {
                Row row = sheet.getRow(i);
                if (columnCnt == 0) {
                    columnCnt = row.getPhysicalNumberOfCells() + cellDataStartOn - rowNumCell;
                }

                int j = cellDataStartOn - rowNumCell;
                String acctNo = getStringCellVal(row.getCell(j));
                if (acctNo == null || "".equals(acctNo)) {
                    break;
                }
                tmpBdFuzzyName = new TmpBdFuzzyName();
                tmpBdFuzzyName.setCodCasaNo(getStringCellVal(row.getCell(j)));
                tmpBdFuzzyName.setName(getStringCellVal(row.getCell(j + 1)));
                tmpBdFuzzyName.setNoMemo(getStringCellVal(row.getCell(j + 2)));
                tmpBdFuzzyName.setRemarks(getStringCellVal(row.getCell(j + 3)));
                tmpBdFuzzyName.setCmd(getStringCellVal(row.getCell(j + 4)));
                tmpBdFuzzyName.setBatchNo(context.get(MapKey.batchNo).toString());
                tmpBdFuzzyName.setDtmCreated(new Timestamp(Calendar.getInstance().getTime().getTime()));
                tmpBdFuzzyName.setIdCreatedBy(context.get(MapKey.emailSender).toString());
                tmpBdFuzzyNameDao.insert(tmpBdFuzzyName);
            }
        } finally {
            file.close();
        }
    }

    private void readXLSX(String param5, TmpBdFuzzyName tmpBdFuzzyName, TmpBdFuzzyNameDao tmpBdFuzzyNameDao) throws Exception {
        File xlsxFile = new File(param5);
        OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(p);
        XSSFReader xssfReader = new XSSFReader(p);
        StylesTable styles = xssfReader.getStylesTable();
        InputStream stream = xssfReader.getSheet("rId1");
        processSheet(styles, strings, stream, tmpBdFuzzyName, tmpBdFuzzyNameDao);
        stream.close();
    }

    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, TmpBdFuzzyName tmpBdFuzzyName, TmpBdFuzzyNameDao tmpBdFuzzyNameDao) throws Exception {
        int minColumns = -1;
        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        ContentHandler handler = new MyXSSFSheetHandler(styles, strings, minColumns, System.out, tmpBdFuzzyName, tmpBdFuzzyNameDao);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
    }

    private void loadConfig(String configFile) throws Exception {
        Properties properties = new Properties();
        InputStream in = AajDlrWorker.class.getClassLoader().getResourceAsStream(configFile);
        properties.load(in);
        headerRow = Integer.parseInt(properties.getProperty("row_header_position"));
        rowNumCell = Integer.parseInt(properties.getProperty("row_num_cell"));
        rowDataStartOn = Integer.parseInt(properties.getProperty("row_data_start_on"));
        cellDataStartOn = Integer.parseInt(properties.getProperty("cell_data_start_on"));
        columnCnt = Integer.parseInt(properties.getProperty("column_count"));
        rowCnt = Integer.parseInt(properties.getProperty("row_count"));
        sheetData = Integer.parseInt(properties.getProperty("sheet_data"));
        in.close();
    }

    private String getStringCellVal(Cell cell) {
        String ret = "";
        if (cell != null) {
            if (cell.getCellType() == 0) {
                ret = String.valueOf((long) cell.getNumericCellValue());
            } else if (cell.getCellType() == 1) {
                ret = cell.getStringCellValue();
            }
        }
        return ret.trim();
    }

    private int headerRow;
    private int rowNumCell;
    private int rowDataStartOn;
    private int columnCnt;
    private int rowCnt;
    private int cellDataStartOn;
    private int sheetData;

    class MyXSSFSheetHandler extends DefaultHandler {

        private StylesTable stylesTable;
        private ReadOnlySharedStringsTable sharedStringsTable;
        private final PrintStream output;
        private final int minColumnCount;
        private boolean vIsOpen;
        private xssfDataType nextDataType;
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;
        private int thisColumn = -1;
        private int lastColumnNumber = -1;
        private StringBuffer value;
        private int ctr;
        private int minColumns;
        private TmpBdFuzzyName fzz;
        private TmpBdFuzzyNameDao fzzDao;

        public MyXSSFSheetHandler(
                StylesTable styles,
                ReadOnlySharedStringsTable strings,
                int cols,
                PrintStream target, TmpBdFuzzyName tmpBdFuzzyName, TmpBdFuzzyNameDao tmpBdFuzzyNameDao) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.output = target;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            ctr = 0;
            this.minColumns = cols;
            this.fzz = tmpBdFuzzyName;
            this.fzzDao = tmpBdFuzzyNameDao;
            this.fzz = new TmpBdFuzzyName();
        }

        @Override
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {

            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                value.setLength(0);
            } else if ("c".equals(name)) {
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length();
                        ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType)) {
                    nextDataType = xssfDataType.BOOL;
                } else if ("e".equals(cellType)) {
                    nextDataType = xssfDataType.ERROR;
                } else if ("inlineStr".equals(cellType)) {
                    nextDataType = xssfDataType.INLINESTR;
                } else if ("s".equals(cellType)) {
                    nextDataType = xssfDataType.SSTINDEX;
                } else if ("str".equals(cellType)) {
                    nextDataType = xssfDataType.FORMULA;
                } else if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            String thisStr = null;
            if ("v".equals(name)) {
                switch (nextDataType) {
                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;
                    case ERROR:
                        thisStr = "\"ERROR:" + value.toString() + '"';
                        break;
                    case FORMULA:
                        thisStr = value.toString();
                        break;
                    case INLINESTR:
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                        thisStr = '"' + rtsi.toString() + '"';
                        break;
                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString();
                        } catch (NumberFormatException ex) {
                            getLogger().error(ex, ex);
                        }
                        break;
                    case NUMBER:
                        String n = value.toString();
                        if (this.formatString != null) {
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                        } else {
                            thisStr = n;
                        }
                        break;
                    default:
                        thisStr = "(Unexpected type: " + nextDataType + ")";
                        break;
                }

                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (ctr >= (rowDataStartOn - 1)) {
                    if (thisColumn == 1) {
                        fzz.setCodCasaNo(thisStr);
                    }
                    if (thisColumn == 2) {
                        fzz.setName(thisStr);
                    }
                    if (thisColumn == 3) {
                        fzz.setNoMemo(thisStr);
                    }
                    if (thisColumn == 4) {
                        fzz.setRemarks(thisStr);
                    }
                    if (thisColumn == 5) {
                        fzz.setCmd(thisStr);
                    }                    
                    fzz.setBatchNo(context.get(MapKey.batchNo).toString());
                    fzz.setDtmCreated(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    fzz.setIdCreatedBy(context.get(MapKey.emailSender).toString());
                }
                if (thisColumn
                        > -1) {
                    lastColumnNumber = thisColumn;
                }

            } else if ("row".equals(name)) {
                if (minColumns
                        > 0) {
                    if (lastColumnNumber == -1) {
                        lastColumnNumber = 0;
                    }
                    for (int i = lastColumnNumber; i < (this.minColumnCount);
                            i++) {
                    }
                }

                if (ctr >= (rowDataStartOn - 1)) {
                    fzzDao.insert(fzz);
                }
                ctr++;
                lastColumnNumber = -1;
                fzz = new TmpBdFuzzyName();
            }

        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (vIsOpen) {
                value.append(ch, start, length);
            }
        }

        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length();
                    ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }
    }

    enum xssfDataType {

        BOOL,
        ERROR,
        FORMULA,
        INLINESTR,
        SSTINDEX,
        NUMBER,
    }
}