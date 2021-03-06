package bdsm.model;


/**
 * 
 * @author v00019372
 */
@SuppressWarnings("serial")
public abstract class SknNgInOutDebitParent extends bdsm.model.BaseModel implements java.io.Serializable {
	private SknNgPK compositeId;
	private String tipeRecord;
	private String kodeDke;
	private String type;
	private String extractStatus;
	
	
	public SknNgPK getCompositeId() {
		return this.compositeId;
	}
	public void setCompositeId(SknNgPK compositeId) {
		this.compositeId = compositeId;
	}
	public String getTipeRecord() {
		return this.tipeRecord;
	}
	public void setTipeRecord(String tipeRecord) {
		this.tipeRecord = tipeRecord;
	}
	public String getKodeDke() {
		return this.kodeDke;
	}
	public void setKodeDke(String kodeDke) {
		this.kodeDke = kodeDke;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getExtractStatus() {
		return this.extractStatus;
	}
	public void setExtractStatus(String extractStatus) {
		this.extractStatus = extractStatus;
	}
	
	
	
	/* Hibernate to Database properties */
	
	protected Character getTypeDB() {
		return (this.type == null)? null: this.type.charAt(0);
	}
	protected void setTypeDB(Character type) {
		this.type = (type == null)? null: type.toString();
	}
	
	protected Character getExtractStatusDB() {
		return (this.extractStatus == null)? null: this.extractStatus.charAt(0);
	}
	protected void setExtractStatusDB(Character extractStatus) {
		this.extractStatus = (extractStatus == null)? null: extractStatus.toString();
	}
	
	
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder()
    		.append("compositeId=").append(this.compositeId.toString())
    		.append(",tipeRecord=").append(this.tipeRecord)
    		.append(",kodeDke=").append(this.kodeDke)
    		.append(",type=").append(this.type)
    		.append(",extractStatus=").append(this.extractStatus)
    		;
    	
    	return sb.toString();
    }
	

}
