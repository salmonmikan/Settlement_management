<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>社員登録</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .form-group input, .form-group select {
      width: 100%;
      padding: 8px;
      font-size: 1rem;
    }
    .btn-section {
      text-align: center;
      margin-top: 20px;
    }
    .btn-section button {
      padding: 10px 20px;
      margin: 0 10px;
    }
    h2 {
      text-align: center;
      color: #2c7be5;
    }
    .page-container {
      max-width: 700px;
      margin: 30px auto;
      background-color: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2>社員登録フォーム</h2>

    <%-- Hiển thị lỗi nếu có --%>
    <c:if test="${not empty error}">
      <p style="color: red; text-align:center;">${error}</p>
    </c:if>

    <form action="<%= request.getContextPath() %>/employeeRegisterPage" method="post">
      <%-- Đây là hidden input để xác định hành động là confirm --%>
      <input type="hidden" name="action" value="confirm">

      <div class="form-section">
        <div class="form-group">
          <label>社員ID</label>
          <input type="text" name="employeeId" required maxlength="5">
        </div>
        <div class="form-group">
          <label>氏名</label>
          <input type="text" name="fullName" required maxlength="20">
        </div>
        <div class="form-group">
          <label>ふりがな</label>
          <input type="text" name="furigana" required maxlength="20">
        </div>
        <div class="form-group">
          <label>生年月日</label>
          <input type="date" name="birthDate" required>
        </div>
        <div class="form-group">
          <label>住所</label>
          <input type="text" name="address" maxlength="50">
        </div>
        <div class="form-group">
          <label>入社日</label>
          <input type="date" name="joinDate" required>
        </div>
        <div class="form-group">
          <label>ログインID</label>
          <input type="text" name="loginId" required maxlength="5">
        </div>
        <div class="form-group">
          <label>パスワード</label>
          <input type="text" name="password" required maxlength="8">
        </div>
        <div class="form-group">
          <label>パスワード確認</label>
          <input type="text" name="confirmPassword" required maxlength="8">
        </div>
        <div class="form-group">
          <label>部署ID</label>
          <input type="text" name="departmentId" required maxlength="5">
        </div>
        <div class="form-group">
          <label>役職ID</label>
          <input type="text" name="positionId" required maxlength="5">
        </div>
      </div>

      <div class="btn-section">
        <button type="submit">登録</button>
        <button type="reset">リセット</button>
      </div>
    </form>
  </div>
</body>
</html>
