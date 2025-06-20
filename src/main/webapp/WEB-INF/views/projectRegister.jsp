<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>プロジェクト登録</title>
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
    <h2>プロジェクト登録</h2>


<!--    <c:if test="${not empty error}">-->
<!--      <p style="color: red; text-align:center;">${error}</p>-->
<!--    </c:if>-->

    <form action="<%= request.getContextPath() %>/projectControl" method="post">
      <input type="hidden" name="action" value="confirm">

      <div class="form-section">
        <div class="form-group">
          <label>プロジェクトコード</label>
          <input type="text" name="projectCode" required maxlength="8">
        </div>
        <div class="form-group">
          <label>プロジェクト名</label>
          <input type="text" name="projectName" required maxlength="20">
        </div>
        <div class="form-group">
          <label>プロジェクト責任者</label>
          <input type="text" name="projectOwner" required maxlength="20">
        </div>
        <div class="form-group">
          <label>開始日</label>
          <input type="date" name="startDate" >
        </div>
        <div class="form-group">
          <label>終了日</label>
          <input type="date" name="endDate" >
        </div>
        <div class="form-group">
          <label>予算（円）</label>
          <input type="number" name="projectBudget" min="0">
        </div>
        <div class="form-group">
          <label>社員ID（カンマ区切りで複数可）</label>
          <input type="text" name="memberIds" placeholder="例: E001,E002,E003">
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
