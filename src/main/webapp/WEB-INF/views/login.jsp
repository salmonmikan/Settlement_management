<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
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
      <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
        <div class="login-form-group">
          <input type="text" name="staffId" placeholder="ユーザーID" required>
        </div>
        <div class="login-form-group password-group">
          <input type="password" id="password" name="password" placeholder="パスワード" required>
          <span class="toggle-password" id="toggleLabel" onclick="togglePassword()">表示</span>
        </div>
        <% String error = (String) request.getAttribute("error"); %>
		<% if (error != null) { %>
		  <div style="color:red; text-align:center;"><%= error %></div>
		<% } %>
        <button type="submit" class="login-btn">ログイン</button>
      </form>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>