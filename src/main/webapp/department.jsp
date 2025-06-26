<%@page import="bean.DepartmentBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="ja">
<head>
<title>部署管理</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<!--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">-->
<script src="<%=request.getContextPath()%>/static/js/department.js" defer></script>
</head>
<%
ArrayList<DepartmentBean> department = (ArrayList<DepartmentBean>) session.getAttribute("department_list");
%>
<body style="display: flex; justify-content: center;">
	<div class="page-container"
		style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
		<!-- Sidebar -->
		<div class="staff-dashboard-wrapper">
			<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
			<!-- Main content -->
			<div class="staff-main-content" style="width: 500px;">
				<div class="panel">

					<!-- 管理画面 -->
					<div class="col-10 p-4">
						<h3>部署管理</h3>
						<div class="action-toolbar">
							<div class="spacer"></div>
							<button class="" onclick="addRow()">＋ 新規追加</button>
						</div>
							<table class="table table-bordered">
								<colgroup>
									<!-- 列幅を固定 -->
									<col style="width: 150px;">
									<col style="width: 150px;">
									<col style="width: 150px;">
								</colgroup>
								<thead>
									<tr>
										<th>部署ID</th>
										<th>部署名</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="dept-table-body">
									<%
									for (DepartmentBean bean : department) {
									%>
									<tr id="row-<%=bean.getDepartment_id()%>">
										<td><%=bean.getDepartment_id()%></td>
										<td data-editable class="dept-name"><%=bean.getDepartment_name()%></td>
										<td>
											<form method="POST" action="departmentControl" style="display: inline;">
												 <!-- js側での処理-->
												 <button id="edit-btn-<%=bean.getDepartment_id()%>" type="button" class="btn btn-sm" style="padding: 3px 8px;"
													onclick="editRow('<%=bean.getDepartment_id()%>')">編集</button>
													
												 <!-- servlet側での処理-->
												
												 <input type="hidden" name="department_id" value="<%=bean.getDepartment_id()%>">
												 <input type="hidden" name="department_name" value="<%=bean.getDepartment_name()%>">
												 <!-- 更新(編集押下時)と削除(デフォルト)-->
												 <button id="save-btn-<%=bean.getDepartment_id()%>" type="button" name="action" value="update" class="btn btn-sm"
												  	onclick="submitRow(this)" style="padding: 3px 8px; display: none;">更新</button>
												 <button id="delete-btn-<%=bean.getDepartment_id()%>" type="submit" name="action" value="delete" class="btn btn-sm"
												  	onclick="return confirm('この部署を削除しますか？')" style="padding: 3px 8px;">削除</button>
												
												
												
												<button id="cancel-btn-<%=bean.getDepartment_id()%>" type="button" class="btn btn-sm" style="display: none; padding: 3px 8px"
													onclick="cancelRow('<%=bean.getDepartment_id()%>')">取消</button>
											</form>
										</td>
									</tr>
									<%
									}
									%>
								</tbody>
							</table>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
