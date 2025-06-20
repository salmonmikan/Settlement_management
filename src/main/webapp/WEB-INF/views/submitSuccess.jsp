<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>送信完了</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>申請が送信されました</h2>
    <p>申請内容は正常に保存されました。</p>

    <div class="btn-section">
      <a href="<%= request.getContextPath() %>/home" class="btn">スタッフメニューへ戻る</a>
      <a href="<%= request.getContextPath() %>/applicationList" class="btn">申請一覧を見る</a>
    </div>
  </div>
</body>
</html>