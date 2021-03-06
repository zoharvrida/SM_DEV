package bdsm.scheduler.model;


/**
 * 
 * @author v00019372
 */
@SuppressWarnings("serial")
public class TmpTxnUploadPK implements Cloneable, java.io.Serializable {
	private String fileId;
	private Integer recordId;
	
	
	public TmpTxnUploadPK() {}
	
	public TmpTxnUploadPK(String fileId, Integer recordId) {
		this.fileId = fileId;
		this.recordId = recordId;
	}
	
	
	public String getFileId() {
		return this.fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public Integer getRecordId() {
		return this.recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	
	
	public TmpTxnUploadPK clone() {
		try {
			return (TmpTxnUploadPK) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			return new TmpTxnUploadPK();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName())
			.append("{")
			.append("fileId=").append(this.fileId)
			.append(",recordId=").append(this.recordId)
			.append("}");
		
		return sb.toString();
	}
	
}
