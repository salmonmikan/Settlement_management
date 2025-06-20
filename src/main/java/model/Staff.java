package model;

public class Staff {
  private String staffId;
  private String name;
  private String position; // ✨ thêm
//メソッドを追加　by ソン
  private String department;
  private String password;

  public String getStaffId() { return staffId; }
  public void setStaffId(String staffId) { this.staffId = staffId; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getPosition() { return position; } // ✨ thêm
  public void setPosition(String position) { this.position = position; } // ✨ thêm
  
  //メソッドを追加　by ソン
  public String getDepartment() { return department; }
  public void setDepartment(String department) { this.department = department; }
  
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}