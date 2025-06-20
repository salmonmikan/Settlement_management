package model;

// プロジェクト管理用bean
public class ProjectList {
    private String project_code;
    private String project_name;
    private String project_owner;
    private String project_members;
    private String start_date;
    private String end_date;
    private Integer project_budget;
    private Integer project_actual;
    
    
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}
	public String getProject_owner() {
		return project_owner;
	}
	public void setProject_owner(String project_owner) {
		this.project_owner = project_owner;
	}
	public String getProject_members() {
		return project_members;
	}
	public void setProject_members(String project_members) {
		this.project_members = project_members;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public Integer getProject_actual() {
		return project_actual;
	}
	public void setProject_actual(Integer project_actual) {
		this.project_actual = project_actual;
	}
	public Integer getProject_budget() {
		return project_budget;
	}
	public void setProject_budget(Integer project_budget) {
		this.project_budget = project_budget;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
}