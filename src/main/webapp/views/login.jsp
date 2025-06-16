<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <script>
    function togglePassword() {
      const input = document.getElementById("password");
      input.type = (input.type === "password") ? "text" : "password";
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
          <span class="toggle-password" onclick="togglePassword()">表示️</span>
        </div>
        <button type="submit" class="login-btn">Login</button>
      </form>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>