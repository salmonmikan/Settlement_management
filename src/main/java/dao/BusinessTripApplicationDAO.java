package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripBean;
import bean.Step1Data;
import bean.Step2Detail;
import bean.Step3Detail;
import bean.UploadedFile; // Đảm bảo bạn có bean này và nó có setter/getter
import util.DBConnection;

public class BusinessTripApplicationDAO {

    // --- insert() method is omitted for brevity ---
    public int insert(Step1Data step1Data, int applicationId, Connection conn) throws SQLException {
        // ... code insert của bạn giữ nguyên, không thay đổi
        String sql = "INSERT INTO business_trip_application (application_id, start_date, end_date, project_code, report, total_days) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, applicationId);
            ps.setDate(2, java.sql.Date.valueOf(step1Data.getStartDate().replace('/', '-')));
            ps.setDate(3, java.sql.Date.valueOf(step1Data.getEndDate().replace('/', '-')));
            ps.setString(4, step1Data.getProjectCode());
            ps.setString(5, step1Data.getTripReport());
            ps.setInt(6, step1Data.getTotalDays());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("出張申請の作成に失敗しました。");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("出張申請IDの取得に失敗しました。");
                }
            }
        }
    }


    public BusinessTripBean loadBusinessTripByApplicationId(int applicationId) throws SQLException {
        BusinessTripBean bean = new BusinessTripBean();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int tripApplicationId = -1;

        try {
            conn = DBConnection.getConnection(); 

            // === BƯỚC 1: LẤY THÔNG TIN STEP 1 VÀ TÌM RA "trip_application_id" ===
            String step1Sql = "SELECT * FROM business_trip_application WHERE application_id = ?";
            ps = conn.prepareStatement(step1Sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            if (rs.next()) {
                tripApplicationId = rs.getInt("trip_application_id");
                Step1Data step1Data = new Step1Data();
                step1Data.setStartDate(rs.getDate("start_date").toLocalDate().toString().replace('-', '/'));
                step1Data.setEndDate(rs.getDate("end_date").toLocalDate().toString().replace('-', '/'));
                step1Data.setProjectCode(rs.getString("project_code"));
                step1Data.setTripReport(rs.getString("report"));
                step1Data.setTotalDays(rs.getInt("total_days"));
                bean.setStep1Data(step1Data);
            } else {
                return null;
            }
            rs.close();
            ps.close();

            // === BƯỚC 2: TẢI DỮ LIỆU STEP 2 VÀ CÁC FILE LIÊN QUAN ===
            String step2Sql = "SELECT * FROM allowance_detail WHERE trip_application_id = ?";
            ps = conn.prepareStatement(step2Sql);
            ps.setInt(1, tripApplicationId);
            rs = ps.executeQuery();

            List<Step2Detail> step2Details = new ArrayList<>();
            while (rs.next()) {
                Step2Detail detail = new Step2Detail();
                // Lấy khóa chính của dòng chi tiết này
                int allowanceDetailId = rs.getInt("detail_id");
                
                // Nạp các thông tin khác của chi tiết
                detail.setRegionType(rs.getString("region_type"));
                detail.setTripType(rs.getString("trip_type"));
                detail.setHotel(rs.getString("hotel"));
                detail.setBurden(rs.getString("burden"));
                detail.setHotelFee(rs.getInt("hotel_fee"));
                detail.setDailyAllowance(rs.getInt("daily_allowance"));
                detail.setDays(rs.getInt("days"));
                detail.setExpenseTotal(rs.getInt("expense_total"));
                detail.setMemo(rs.getString("memo"));
                
                // Tải các file liên quan đến chi tiết này từ bảng receipt_file
                String fileSql = "SELECT original_file_name, stored_file_path FROM receipt_file WHERE block_id = ? AND block_type = 'allowance_detail'";
                try (PreparedStatement psFile = conn.prepareStatement(fileSql)) {
                    psFile.setInt(1, allowanceDetailId);
                    try (ResultSet rsFile = psFile.executeQuery()) {
                        List<UploadedFile> files = new ArrayList<>();
                        while (rsFile.next()) {
                            UploadedFile file = new UploadedFile();
                            file.setOriginalFileName(rsFile.getString("original_file_name"));
                            file.setTemporaryPath(rsFile.getString("stored_file_path")); // Dùng đúng tên cột
                            files.add(file);
                        }
                        detail.setTemporaryFiles(files); // Gán danh sách file vào chi tiết
                    }
                }
                step2Details.add(detail);
            }
            bean.setStep2Details(step2Details);
            rs.close();
            ps.close();

            // === BƯỚC 3: TẢI DỮ LIỆU STEP 3 VÀ CÁC FILE LIÊN QUAN ===
            String step3Sql = "SELECT * FROM business_trip_transportation_detail WHERE trip_application_id = ?";
            ps = conn.prepareStatement(step3Sql);
            ps.setInt(1, tripApplicationId);
            rs = ps.executeQuery();
            
            List<Step3Detail> step3Details = new ArrayList<>();
            while (rs.next()) {
                 Step3Detail detail = new Step3Detail();
                 int transportDetailId = rs.getInt("detail_id");
                 
                 detail.setTransProject(rs.getString("trans_project"));
                 detail.setDeparture(rs.getString("departure"));
                 detail.setArrival(rs.getString("arrival"));
                 detail.setTransport(rs.getString("transport"));
                 detail.setFareAmount(rs.getInt("fare_amount"));
                 detail.setTransTripType(rs.getString("trans_trip_type"));
                 detail.setTransBurden(rs.getString("trans_burden"));
                 detail.setTransExpenseTotal(rs.getInt("trans_expense_total"));
                 detail.setTransMemo(rs.getString("trans_memo"));

                 // Tải các file liên quan
                 String fileSql = "SELECT original_file_name, stored_file_path FROM receipt_file WHERE block_id = ? AND block_type = 'business_trip_transportation_detail'";
                 try (PreparedStatement psFile = conn.prepareStatement(fileSql)) {
                    psFile.setInt(1, transportDetailId);
                    try (ResultSet rsFile = psFile.executeQuery()) {
                        List<UploadedFile> files = new ArrayList<>();
                        while (rsFile.next()) {
                            UploadedFile file = new UploadedFile();
                            file.setOriginalFileName(rsFile.getString("original_file_name"));
                            file.setTemporaryPath(rsFile.getString("stored_file_path")); // Dùng đúng tên cột
                            files.add(file);
                        }
                        detail.setTemporaryFiles(files);
                    }
                }
                step3Details.add(detail);
            }
            bean.setStep3Details(step3Details);
            
            bean.calculateTotalAmount();
            return bean;

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}