package bean;

import java.io.Serializable;
import java.sql.Date;

public class ReimbursementBean implements Serializable {

    private String reimbursementId;
    private String staffId;
    private String projectCode;
    private Date date;
    private String destinations;
    private String accountingItem;
    private int amount;
    private String memo;
    private String abstractNote;

    public ReimbursementBean() {}

    public ReimbursementBean(String reimbursementId, String staffId, String projectCode, Date date, String destinations, String accountingItem, int amount, String memo, String abstractNote) {
        this.reimbursementId = reimbursementId;
        this.staffId = staffId;
        this.projectCode = projectCode;
        this.date = date;
        this.destinations = destinations;
        this.accountingItem = accountingItem;
        this.amount = amount;
        this.memo = memo;
        this.abstractNote = abstractNote;
    }

    // Getter & Setter

    public String getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(String reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getDestinations() {
        return destinations;
    }
    
    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public String getAccountingItem() {
        return accountingItem;
    }

    public void setAccountingItem(String accountingItem) {
        this.accountingItem = accountingItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAbstractNote() {
        return abstractNote;
    }

    public void setAbstractNote(String abstractNote) {
        this.abstractNote = abstractNote;
    }
}
