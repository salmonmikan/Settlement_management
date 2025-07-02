<%@page import="java.io.Console"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/style.css">
<script src="${pageContext.request.contextPath}/static/js/script.js"></script>
<script>
    function togglePassword() {
      const input = document.getElementById("password");
      const toggle = document.getElementById("toggleLabel");
      const isHidden = input.type === "password";
      input.type = isHidden ? "text" : "password";
      toggle.textContent = isHidden ? "非表示" : "表示";
    }
  </script>
</head>
<body>
	<div class="center-viewport">
		<div class="login-wrapper">
			<div class="login-title">ログイン</div>
			<form action="${pageContext.request.contextPath}/LoginServlet"
				method="post">
				<div class="login-form-group">
					<input type="text" name="staffId" placeholder="社員ID" required>
				</div>
				<div class="login-form-group password-group">
					<input type="password" id="password" name="password"
						placeholder="パスワード" required> <span
						class="toggle-password" id="toggleLabel"
						onclick="togglePassword()">表示</span>
				</div>

				<%-- [CẢI THIỆN] Sử dụng JSTL để hiển thị lỗi, code sạch hơn --%>
				<c:if test="${not empty requestScope.error}">
					<div style="color: red; text-align: center;">${requestScope.error}</div>
				</c:if>

				<button type="submit" class="login-btn">ログイン</button>
			</form>
		</div>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
<script>console.log("system_19:32")</script>
</html>