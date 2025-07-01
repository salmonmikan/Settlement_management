<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>パスワード変更</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<script src="<%=request.getContextPath()%>/static/js/script.js"></script>
<style>
.page-container {
	max-width: 100vw;
}

.form-section {
	max-width: 500px;
	margin: 0 auto;
}

.form-group input {
	width: 100%;
	padding: 8px;
	font-size: 1rem;
}

.error-message {
	color: red;
	margin-top: 5px;
	font-size: 0.9rem;
}
</style>
</head>
<body>
	<div class="page-container">
		<h2>パスワード変更</h2>

		<form action="<%=request.getContextPath()%>/changePassword" method="post">
			<div class="form-section">
				<div class="form-group">
					<label>現在のパスワード</label>
					<input type="password" name="currentPassword" required>
				</div>

				<div class="form-group">
					<label>新しいパスワード</label>
					<input type="password" name="newPassword" required>
				</div>

				<div class="form-group">
					<label>新しいパスワード（確認）</label>
					<input type="password" name="confirmPassword" required>
				</div>
			</div>

			<div class="btn-section">
				<button type="button" onclick="location.href='<%=request.getContextPath()%>/menu'">戻る</button>
				<button type="submit">変更</button>
			</div>
		</form>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

	<c:if test="${not empty sessionScope.successMsg}">
		<script>alert('${sessionScope.successMsg}');</script>
		<c:remove var="successMsg" scope="session" />
	</c:if>

	<c:if test="${not empty sessionScope.errorMsg}">
		<script>alert('${sessionScope.errorMsg}');</script>
		<c:remove var="errorMsg" scope="session" />
	</c:if>
</body>
</html>
