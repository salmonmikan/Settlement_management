<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="bean.BusinessTripBean.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>社員一覧 - 管理画面</title>
<link rel="stylesheet"
	href="<%= request.getContextPath() %>/static/css/style.css">
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
		<h2>社員一覧</h2>
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
					<th>社員ID</th>
					<th>氏名</th>
					<th>ふりがな</th>
					<th>生年月日</th>
					<th>住所</th>
					<th>入社日</th>
					<th>ログインID</th>
					<th>部署</th>
					<th>役職</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="emp" items="${employeeList}">
					<tr>
						<td>${emp.employeeId}</td>
						<td>${emp.fullName}</td>
						<td>${emp.furigana}</td>
						<td>${emp.birthDate}</td>
						<td>${emp.address}</td>
						<td>${emp.joinDate}</td>
						<td>${emp.loginId}</td>
						<td>${emp.department}</td>
						<td>${emp.position}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<a href="<%= request.getContextPath() %>/employeeRegisterPage"
			class="center-link">アカウント登録はこちら</a>
	</div>
</body>
</html>
