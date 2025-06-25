<%@page import="model.ProjectList"%>

<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="bean.BusinessTripBean.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--数値カンマ対応-->
<%@ page import="java.text.DecimalFormat"%>
<%
    DecimalFormat df = new DecimalFormat("#,###");
%>


<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>プロジェクト一覧 - 管理画面</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
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
	color: var;
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
	<nav>
		精算管理システム
		<form class="logoutForm"
			action="<%= request.getContextPath() %>/logOutServlet" method="post">
			<button type="submit" title="Log out">
				<i class="fa-solid fa-right-from-bracket"></i>
			</button>
		</form>
	</nav>
	<div class="page-container">
		<h2>プロジェクト一覧</h2>
		<form id="" action="projectControl" method="post">
			<div class="action-toolbar">
				<div class="spacer"></div>
				<button type="button"
					onclick="location.href='projectControl?action=add'">＋ 新規追加</button>
				<button type="submit" name="action" value="edit" id="editBtn"
					disabled>編集</button>
				<button type="submit" name="action" value="delete" id="deleteBtn"
					disabled onclick="return confirm('本当に削除しますか？')">削除</button>
			</div>
			<table>
				<thead>
					<tr>
						<th>選択</th>
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
						<td><input type="checkbox" name="Project_code"
							value="<%=bean.getProject_code()%>" class="row-check"></td>
						<td><%=bean.getProject_code()%></td>
						<td><%=bean.getProject_name()%></td>
						<td><%=bean.getProject_owner()%></td>
						<td><%=bean.getProject_members()%></td>
						<td><%=bean.getStart_date()%></td>
						<td><%=bean.getEnd_date()%></td>
						<td style="text-align: right;"><%= budgetStr %>円</td>
						<td style="text-align: right;"><%= !("0".equals(actualStr)) ? actualStr + "円" : "未清算" %></td>
					</tr>
					<%
				}
				%>
				</tbody>
			</table>
		</form>
		<!--		<a href="<%=request.getContextPath()%>/employeeRegisterPage"-->
		<!--			class="center-link">プロジェクト登録はこちら</a>-->
	</div>
	<script type="text/javascript">
		const checkboxes = document.querySelectorAll('.row-check');
	  	const editBtn = document.getElementById('editBtn');

		document.querySelectorAll('.row-check').forEach(cb => {
		  	cb.addEventListener('change', () => {
			    // チェックが変更された瞬間に処理が走る
			    console.log('チェック変更トリガー', cb.checked);
			    const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;
			 	// 編集ボタン：1つだけ選択されている時だけ活性
			    editBtn.disabled = checkedCount !== 1;

			    // 削除ボタン：1つ以上選択されていれば活性
			    deleteBtn.disabled = checkedCount === 0;
		  });
		});
	</script>
	<%
	String successMsg = (String) request.getAttribute("successMsg");
	%>
	<%
	if (successMsg != null) {
	%>
	<script>
    alert("<%=successMsg%>");
</script>
	<% } %>
</body>
</html>
