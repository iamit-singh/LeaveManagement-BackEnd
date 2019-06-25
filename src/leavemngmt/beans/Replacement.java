package leavemngmt.beans;

public class Replacement {
	private int id;
	private int application_id;
	private String surrogate_eid;
	private String from_date;
	private String to_date;
	private int days;
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getId(){
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getApplication_id() {
		return application_id;
	}
	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}
	public String getSurrogate_eid() {
		return surrogate_eid;
	}
	public void setSurrogate_eid(String surrogate_eid) {
		this.surrogate_eid = surrogate_eid;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
}
