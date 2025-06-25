package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Project;
import model.ProjectList;

public class ProjectDAO {
	// bussinessTrip用 yuiさん作成？by ebara
	public List<Project> getAllProjects() throws Exception {
		List<Project> list = new ArrayList<>();
		Connection conn = util.DBConnection.getConnection();
		String sql = "SELECT project_id, project_name FROM project_manage ORDER BY project_id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			list.add(new Project(rs.getString("project_id"), rs.getString("project_name")));
		}
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}

	// プロジェクト管理画面-表示
	// 使用TABLE：project_info, project_budget, project_management, personal_info
	// 使用TABLE：project_info, project_budget, project_management, staff //update by son
	public List<ProjectList> getAllProject_management() throws Exception {
		List<ProjectList> list = new ArrayList<>();
		Connection conn = util.DBConnection.getConnection();
		String sql = """
				    SELECT
				        pi.project_code,
				        pi.project_name,
				        pi.project_owner,
				        GROUP_CONCAT(p.name ORDER BY p.name SEPARATOR ', ') AS member_names,
				        pi.start_date,
				        pi.end_date,
				        pb.project_budget,
				        pb.project_actual
				    FROM
				        project_info pi
				    LEFT JOIN
				        project_management pm ON pi.project_code = pm.project_code
				    LEFT JOIN
						staff p ON pm.employee_id = p.staff_id
				    LEFT JOIN
				        project_budget pb ON pi.project_code = pb.project_code
				    GROUP BY
				        pi.project_code, pi.project_name, pi.project_owner, pi.start_date, pi.end_date, pb.project_budget, pb.project_actual
				    ORDER BY
				        pi.project_code
				""";

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			ProjectList psl = new ProjectList();

			psl.setProject_code(rs.getString("project_code"));
			// 以下の項目は、nullの場合「登録なし」と表示する
			String project_name = rs.getString("project_name");
			psl.setProject_name((project_name == null || project_name.isBlank()) ? "登録なし" : project_name);
			String project_owner = rs.getString("project_owner");
			psl.setProject_owner((project_owner == null || project_owner.isBlank()) ? "登録なし" : project_owner);
			String member_names = rs.getString("member_names");
			psl.setProject_members((member_names == null || member_names.isBlank()) ? "登録なし" : member_names);
			String startDate = rs.getString("start_date");
			psl.setStart_date((startDate == null || startDate.isBlank()) ? "登録なし" : startDate);
			String endDate = rs.getString("end_date");
			psl.setEnd_date((endDate == null || endDate.isBlank()) ? "登録なし" : endDate);
			// getIntだとnullでも0を返してしまうので、getObj。
			int budget = (rs.getObject("project_budget") != null) ? rs.getInt("project_budget") : 0;
			psl.setProject_budget(budget);
			psl.setProject_actual(rs.getObject("project_actual") != null ? rs.getInt("project_actual") : null);

			list.add(psl);
		}
		rs.close();
		stmt.close();
		conn.close();
		return list;
	}

	// プロジェクト管理-新規登録
	// 使用TABLE：project_info, project_budget, project_management
	public void insertProject(ProjectList p) throws Exception {
		Connection conn = util.DBConnection.getConnection();

		try {
			conn.setAutoCommit(false);

			// project_info 登録
			String sql_p_i = """
					    INSERT INTO project_info (project_code, project_name, project_owner, start_date, end_date)
					    VALUES (?, ?, ?, ?, ?)
					""";
			try (PreparedStatement stmt = conn.prepareStatement(sql_p_i)) {
				stmt.setString(1, p.getProject_code());
				stmt.setString(2, p.getProject_name());
				stmt.setString(3, p.getProject_owner());
				// date型は""空文字だとエラーになるので、nullに置き換える
				if (p.getStart_date() == null || p.getStart_date().isBlank()) {
					stmt.setNull(4, Types.DATE);
				} else {
					stmt.setString(4, p.getStart_date());
				}
				if (p.getEnd_date() == null || p.getEnd_date().isBlank()) {
					stmt.setNull(5, Types.DATE);
				} else {
					stmt.setString(5, p.getEnd_date());
				}
				stmt.executeUpdate();
			}

			// project_budget 登録
			String sql_p_b = """
					    INSERT INTO project_budget (project_code, project_budget)
					    VALUES (?, ?)
					""";
			try (PreparedStatement stmt = conn.prepareStatement(sql_p_b)) {
				stmt.setString(1, p.getProject_code());
				if (p.getProject_budget() != null) {
					stmt.setInt(2, p.getProject_budget());
				} else {
					stmt.setNull(2, Types.INTEGER);
				}
				stmt.executeUpdate();
			}

			// project_management 登録（複数可、String想定）
			String memberStr = p.getProject_members(); // ← "E001,E002,E003" などのユーザー文字列
			if (memberStr != null && !memberStr.isEmpty()) {
				String[] empIds = memberStr.split("\\s*,\\s*"); // ← 空白を除いて分割

				String sql_p_m = """
						    INSERT INTO project_management (
						        employee_id, project_code, role, remarks, report
						    ) VALUES (?, ?, ?, ?, ?)
						""";

				try (PreparedStatement stmt = conn.prepareStatement(sql_p_m)) {
					for (String empId : empIds) {
						stmt.setString(1, empId);
						stmt.setString(2, p.getProject_code());
						stmt.setString(3, "");
						stmt.setString(4, "");
						stmt.setString(5, "");
						stmt.addBatch(); // SQLindexに登録
					}
					stmt.executeBatch(); //SQLinsertを一括実行
				} catch (Exception e) {
					conn.rollback();
					throw e; // ここ2重になってるけど要らないか？
				}
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
	}
	
	// プロジェクト管理-1件検索(削除メソッドの関係でString[]を使用しているが、一件検索が前提)
	public ProjectList findByProjectCode(String[] project_code) throws Exception {
	    ProjectList psl = null;
	    Connection conn = util.DBConnection.getConnection();
	    
	    String project_code_str = project_code[0];
	    
	    String sql = """
	        SELECT
	            pi.project_code,
	            pi.project_name,
	            pi.project_owner,
	            GROUP_CONCAT(p.name ORDER BY p.name SEPARATOR ', ') AS member_names,
	            pi.start_date,
	            pi.end_date,
	            pb.project_budget,
	            pb.project_actual
	        FROM
	            project_info pi
	        LEFT JOIN
	            project_management pm ON pi.project_code = pm.project_code
	        LEFT JOIN
	            staff p ON pm.employee_id = p.staff_id
	        LEFT JOIN
	            project_budget pb ON pi.project_code = pb.project_code
	        WHERE
	            pi.project_code = ?
	        GROUP BY
	            pi.project_code, pi.project_name, pi.project_owner, pi.start_date, pi.end_date, pb.project_budget, pb.project_actual
	        """;

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, project_code_str);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            psl = new ProjectList();
	            psl.setProject_code(rs.getString("project_code"));
	            psl.setProject_name(rs.getString("project_name"));
	            psl.setProject_owner(rs.getString("project_owner"));
	            psl.setProject_members(rs.getString("member_names"));
	            psl.setStart_date(rs.getString("start_date"));
	            psl.setEnd_date(rs.getString("end_date"));
	            psl.setProject_budget(rs.getObject("project_budget") != null ? rs.getInt("project_budget") : 0);
	            psl.setProject_actual(rs.getObject("project_actual") != null ? rs.getInt("project_actual") : 0);
	        }
	    } finally {
	        conn.close();
	    }
	    return psl;
	}

	
	
	
	public void updateProject(ProjectList p) throws Exception {
	    Connection conn = util.DBConnection.getConnection();

	    try {
	        conn.setAutoCommit(false);

	        // project_info 更新
	        String sql_p_i = """
	            UPDATE project_info
	            SET project_name = ?, project_owner = ?, start_date = ?, end_date = ?
	            WHERE project_code = ?
	        """;
	        try (PreparedStatement stmt = conn.prepareStatement(sql_p_i)) {
	            stmt.setString(1, p.getProject_name());
	            stmt.setString(2, p.getProject_owner());

	            if (p.getStart_date() == null || p.getStart_date().isBlank()) {
	                stmt.setNull(3, Types.DATE);
	            } else {
	                stmt.setString(3, p.getStart_date());
	            }

	            if (p.getEnd_date() == null || p.getEnd_date().isBlank()) {
	                stmt.setNull(4, Types.DATE);
	            } else {
	                stmt.setString(4, p.getEnd_date());
	            }

	            stmt.setString(5, p.getProject_code());
	            stmt.executeUpdate();
	        }

	        // project_budget 更新（存在しない場合 INSERT でも可）
	        String sql_p_b = """
	            UPDATE project_budget
	            SET project_budget = ?
	            WHERE project_code = ?
	        """;
	        try (PreparedStatement stmt = conn.prepareStatement(sql_p_b)) {
	            if (p.getProject_budget() != null) {
	                stmt.setInt(1, p.getProject_budget());
	            } else {
	                stmt.setNull(1, Types.INTEGER);
	            }
	            stmt.setString(2, p.getProject_code());
	            stmt.executeUpdate();
	        }

	        // project_management（いったん削除 → 再登録）
	        String sql_delete_pm = "DELETE FROM project_management WHERE project_code = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql_delete_pm)) {
	            stmt.setString(1, p.getProject_code());
	            stmt.executeUpdate();
	        }

	        // project_management 再登録
	        String memberStr = p.getProject_members(); // E001,E002,...
	        if (memberStr != null && !memberStr.isEmpty()) {
	            String[] empIds = memberStr.split("\\s*,\\s*");

	            String sql_p_m = """
	                INSERT INTO project_management (
	                    employee_id, project_code
	                ) VALUES (?, ?)
	            """;
	            try (PreparedStatement stmt = conn.prepareStatement(sql_p_m)) {
	                for (String empId : empIds) {
	                    stmt.setString(1, empId);
	                    stmt.setString(2, p.getProject_code());
	                    stmt.addBatch();
	                }
	                stmt.executeBatch();
	            }
	        }

	        conn.commit();
	    } catch (Exception e) {
	        conn.rollback();
	        throw e;
	    } finally {
	        conn.close();
	    }
	}


	//プロジェクト管理-削除（複数件対応）
	// 使用TABLE：project_info, project_budget, project_management
	public void deleteProjects(String[] Project_code) throws Exception {
//		if (projectCodes == null || projectCodes.isEmpty()) {
//			throw new IllegalArgumentException("削除対象のプロジェクトコードが空です");
//		}

		Connection conn = util.DBConnection.getConnection();
		try {
			conn.setAutoCommit(false);

			// IN句用のプレースホルダを作成
			String placeholders = String.join(",", java.util.Collections.nCopies(Project_code.length, "?"));

			// 1. project_management削除
			String sql_pm = "DELETE FROM project_management WHERE project_code IN (" + placeholders + ")";
			try (PreparedStatement stmt = conn.prepareStatement(sql_pm)) {
				for (int i = 0; i < Project_code.length; i++) {
					stmt.setString(i + 1, Project_code[i]);
				}
				stmt.executeUpdate();
			}

			// 2. project_budget削除
			String sql_pb = "DELETE FROM project_budget WHERE project_code IN (" + placeholders + ")";
			try (PreparedStatement stmt = conn.prepareStatement(sql_pb)) {
				for (int i = 0; i < Project_code.length; i++) {
					stmt.setString(i + 1, Project_code[i]);
				}
				stmt.executeUpdate();
			}

			// 3. project_info削除
			String sql_pi = "DELETE FROM project_info WHERE project_code IN (" + placeholders + ")";
			try (PreparedStatement stmt = conn.prepareStatement(sql_pi)) {
				for (int i = 0; i < Project_code.length; i++) {
					stmt.setString(i + 1, Project_code[i]);
				}
				int deleted = stmt.executeUpdate();
				if (deleted == 0) {
					throw new Exception("削除対象のプロジェクトコードが存在しません");
				}
			}

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
	}
	
	//by Songoku 
	public Map<String, String> getStaffNamesByIds(String[] ids) {
	    Map<String, String> result = new LinkedHashMap<>();
	    if (ids == null || ids.length == 0) return result;

	    String placeholders = String.join(",", Collections.nCopies(ids.length, "?"));
	    String sql = "SELECT staff_id, name FROM staff WHERE staff_id IN (" + placeholders + ")";

	    try (Connection conn = util.DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        for (int i = 0; i < ids.length; i++) {
	            ps.setString(i + 1, ids[i].trim());
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                result.put(rs.getString("staff_id"), rs.getString("name"));
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}


}
