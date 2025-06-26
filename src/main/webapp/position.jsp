<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="bean.PositionBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    ArrayList<PositionBean> positionList = (ArrayList<PositionBean>) session.getAttribute("position_list");
    if (positionList == null) positionList = new ArrayList<>();
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>役職一覧 - 管理画面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
<style>
.page-container {
	max-width: 80%;
	margin: 30px auto;
	display: flex;
	gap: 50px;
	align-items: flex-start;
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
	text-align: center;
	margin-bottom: 20px;
}
</style>
</head>
<body>
	<nav>
		精算管理システム
		<form class="logoutForm" action="<%= request.getContextPath() %>/logOutServlet" method="post">
			<button type="submit" title="Log out"><i class="fa-solid fa-right-from-bracket"></i></button>
		</form>
	</nav>

	<div class="page-container">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
		<div>
			<h2>役職一覧</h2>
			<form action="positionControl" method="post">
				<div class="action-toolbar">
					<div class="spacer"></div>
					<button type="button" onclick="location.href='positionControl?action=add'">＋ 新規追加</button>
					<button type="submit" name="action" value="edit" id="editBtn" disabled>編集</button>
					<button type="submit" name="action" value="delete" id="deleteBtn" disabled onclick="return confirm('本当に削除しますか？')">削除</button>
				</div>
				<table>
					<thead>
						<tr>
							<th>選択</th>
							<th>役職ID</th>
							<th>役職名</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (PositionBean bean : positionList) {
						%>
						<tr>
							<td><input type="checkbox" name="position_id" value="<%=bean.getPosition_id()%>" class="row-check"></td>
							<td><%=bean.getPosition_id()%></td>
							<td><%=bean.getPosition_name()%></td>
						</tr>
						<%
						}
						%>
					</tbody>
				</table>
			</form>
		</div>
	</div>

	<script>
	const checkboxes = document.querySelectorAll('.row-check');
	const editBtn = document.getElementById('editBtn');
	const deleteBtn = document.getElementById('deleteBtn');

	checkboxes.forEach(cb => {
		cb.addEventListener('change', () => {
			const checkedCount = Array.from(checkboxes).filter(cb => cb.checked).length;
			editBtn.disabled = checkedCount !== 1;
			deleteBtn.disabled = checkedCount === 0;
		});
	});
	</script>

	<%
	String method = request.getMethod();
	String successMsg = (String) request.getAttribute("successMsg");
	String errorMsg = (String) request.getAttribute("errorMsg");

	if ("POST".equalsIgnoreCase(method)) {
		if (successMsg != null) {
	%>
	<script>alert("<%=successMsg%>");</script>
	<%
		} else if (errorMsg != null) {
	%>
	<script>alert("<%=errorMsg%>");</script>
	<%
		}
	}
	%>
</body>
</html>
