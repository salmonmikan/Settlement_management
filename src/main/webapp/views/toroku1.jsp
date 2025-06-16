<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>アカウント登録 - Step 1</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
  <!-- Bao bọc nội dung trong center-viewport -->
  <div class="center-viewport">
    <div class="register-step1-wrapper">
      <h2>新規登録</h2>
      <form action="RegisterStep1Servlet" method="post">
        <div class="form-group">
          <label>社員番号（ユーザーID）</label>
          <input type="text" name="employeeId" required placeholder="例: S0001">
        </div>
        <div class="form-group">
          <label>パスワード</label>
          <input type="password" name="password" required>
        </div>
        <div class="form-group">
          <label>パスワード確認</label>
          <input type="password" name="confirmPassword" required>
        </div>
        <div class="btn-section">
          <button class="btn" type="submit">次へ</button>
        </div>
      </form>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>