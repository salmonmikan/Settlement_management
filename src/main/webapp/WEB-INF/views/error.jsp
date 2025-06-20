<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>エラー発生</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .error-container {
      max-width: 600px;
      margin: 50px auto;
      padding: 20px;
      border: 1px solid #ccc;
      background: #fff8f8;
      text-align: center;
      border-radius: 8px;
    }
    .error-container h2 {
      color: #d00;
    }
    .btn-section {
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <div class="error-container">
    <h2>エラーが発生しました</h2>
    <p>システムエラーのため、処理を完了できませんでした。</p>
    <p>もう一度お試しいただくか、管理者にお問い合わせください。</p>

    <div class="btn-section">
      <a href="<%= request.getContextPath() %>/home" class="btn">スタッフメニューへ</a>
    </div>
  </div>
</body>
</html>