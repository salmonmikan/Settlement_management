<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>社員情報登録 - Step 2</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
  <div class="center-viewport">
    <div class="register-step2-wrapper">
      <h2>新規登録</h2>
      <form action="RegisterStep2Servlet" method="post">
        <div class="form-group">
          <label>氏名</label>
          <input type="text" name="name" required>
        </div>
        <div class="form-group">
          <label>フリガナ</label>
          <input type="text" name="furigana" required>
        </div>
        <div class="form-group">
          <label>生年月日</label>
          <input type="date" name="birthDate" required>
        </div>
        <div class="form-group">
          <label>住所</label>
          <input type="text" name="address" required>
        </div>
        <div class="form-group">
          <label>入社日</label>
          <input type="date" name="hireDate" required>
        </div>
        <div class="form-group">
          <label>部署</label>
          <select name="department" required>
            <option value="">選択してください</option>
            <option value="営業部">営業部</option>
            <option value="管理部">管理部</option>
          </select>
        </div>
        <div class="form-group">
          <label>役職</label>
          <select name="position" required>
            <option value="">選択してください</option>
            <option value="一般社員">一般社員</option>
            <option value="主任">主任</option>
            <option value="部長">部長</option>
          </select>
        </div>
        <div class="btn-section">
          <button class="btn" type="button" onclick="history.back()">戻る</button>
          <button class="btn" type="submit">確認</button>
        </div>
      </form>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>