<%@page import="model.ProjectList"%>

<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--数値カンマ対応-->
<%@ page import="java.text.DecimalFormat" %>
<%
    DecimalFormat df = new DecimalFormat("#,###");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>社員一覧 - 管理画面</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<style>
.page-container {
	max-width: 80%;
	margin: 30px auto;
	background-color: white;
	border-radius: 10px;
	padding: 20px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
	font-size: 0.95rem;
}

th, td {
	padding: 10px;
	border: 1px solid #ccc;
	text-align: center;
}

th {
	background-color: #e6f0fa;
}

h2 {
	color: #2c7be5;
	margin-bottom: 20px;
	text-align: center;
}

.center-link {
	display: block;
	text-align: center;
	margin-top: 30px;
	font-size: 1rem;
	color: #2c7be5;
	text-decoration: none;
}

.center-link:hover {
	text-decoration: underline;
}
</style>
</head>
<%
ArrayList<ProjectList> list = (ArrayList<ProjectList>)request.getAttribute("projectList_management");
%>
<body>
	<div class="page-container">
		<h2>プロジェクト一覧</h2>
		<div class="action-toolbar">
			<div class="spacer"></div>
			<button class="" onclick="">＋ 新規追加</button>
		</div>
		<table>
			<thead>
				<tr>
					<th>コード</th>
					<th>プロジェクト名</th>
					<th>責任者</th>
					<th>メンバー</th>					
					<th>開始日</th>
					<th>終了日</th>
					<th>予算</th>
					<th>実積</th>
				</tr>
			</thead>
			<tbody>
				<%
				for(ProjectList bean:list) {
					// nullでないなら、数値項目をカンマ区切りに直す
					String budgetStr = bean.getProject_budget() != null ? df.format(bean.getProject_budget()) : "";
			        String actualStr = bean.getProject_actual() != null ? df.format(bean.getProject_actual()) : "";
				%>
				<tr>
					<td><%=bean.getProject_code()%></td>
					<td><%=bean.getProject_name()%></td>
					<td><%=bean.getProject_owner()%></td>
					<td><%=bean.getProject_members()%></td>
					<td><%=bean.getStart_date()%></td>
					<td><%=bean.getEnd_date()%></td>
					<td style="text-align: right;"><%= budgetStr %>円</td>
        			<td style="text-align: right;"><%= actualStr %>円</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
		<a href="<%=request.getContextPath()%>/employeeRegisterPage"
			class="center-link">プロジェクト登録はこちら</a>
	</div>
</body>
</html>
