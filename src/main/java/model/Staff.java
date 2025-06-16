package model;

public class Staff {
  private String staffId;
  private String name;
  private String position; // ✨ thêm

  public String getStaffId() { return staffId; }
  public void setStaffId(String staffId) { this.staffId = staffId; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getPosition() { return position; } // ✨ thêm
  public void setPosition(String position) { this.position = position; } // ✨ thêm
}