package bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDetailBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String projectCode;
    private String date;
    private String destinations;
    private String accountingItem;
    private int amount;
    private String report;
    private List<UploadedFile> temporaryFiles = new ArrayList<>();

    // Toàn bộ Getters và Setters ở đây...
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDestinations() { return destinations; }
    public void setDestinations(String destinations) { this.destinations = destinations; }
    public String getAccountingItem() { return accountingItem; }
    public void setAccountingItem(String accountingItem) { this.accountingItem = accountingItem; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
    public List<UploadedFile> getTemporaryFiles() { return temporaryFiles; }
    public void setTemporaryFiles(List<UploadedFile> temporaryFiles) { this.temporaryFiles = temporaryFiles; }
}