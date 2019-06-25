package leavemngmt.beans;

public class Notification {
	private int notification_id;
	private int application_id;
	private String sender_eid;
	private String recipient_eid;
	private String recipient_designation;
	private String is_new_for_sender;
	private String is_new_for_recipient;
	private String lastUpdate;
	
	public String getRecipient_designation() {
		return recipient_designation;
	}
	public void setRecipient_designation(String recipient_designation) {
		this.recipient_designation = recipient_designation;
	}
	public String getSender_eid() {
		return sender_eid;
	}
	public void setSender_eid(String sender_eid) {
		this.sender_eid = sender_eid;
	}
	public int getNotification_id() {
		return notification_id;
	}
	public void setNotification_id(int notification_id) {
		this.notification_id = notification_id;
	}
	public int getApplication_id() {
		return application_id;
	}
	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}
	public String getRecipient_eid() {
		return recipient_eid;
	}
	public void setRecipient_eid(String recipient_eid) {
		this.recipient_eid = recipient_eid;
	}
	public String getIs_new_for_sender() {
		return is_new_for_sender;
	}
	public void setIs_new_for_sender(String is_new_for_sender) {
		this.is_new_for_sender = is_new_for_sender;
	}
	public String getIs_new_for_recipient() {
		return is_new_for_recipient;
	}
	public void setIs_new_for_recipient(String is_new_for_recipient) {
		this.is_new_for_recipient = is_new_for_recipient;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
