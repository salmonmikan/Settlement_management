<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>申請完了</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <style>
    body { background-color: #f4f7f6; }
    .result-container {
      max-width: 600px;
      margin: 80px auto;
      padding: 40px;
      background-color: #fff;
      text-align: center;
      border-radius: 10px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.08);
    }
    .icon {
      font-size: 5rem;
      color: #28a745; /* Màu xanh lá cây thành công */
    }
    .result-container h2 {
      font-size: 2rem;
      color: #333;
      margin: 20px 0 10px;
    }
    .result-container p {
      color: #666;
      font-size: 1.1rem;
    }
    .btn-section {
      margin-top: 30px;
      display: flex;
      justify-content: center;
      gap: 15px;
    }
    .btn {
      padding: 12px 25px;
      text-decoration: none;
      border-radius: 5px;
      font-weight: bold;
      transition: all 0.3s ease;
    }
    .btn-primary { background-color: var(--primary-color); color: white; }
    .btn-primary:hover { background-color: var(--primary-color-dark); }
    .btn-secondary { background-color: #6c757d; color: white; }
    .btn-secondary:hover { background-color: #5a6268; }
  </style>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
  <div class="result-container">
    <h2>編集が完了しました</h2>
    <p>申請内容は正常に送信されました。状況は申請一覧ページで確認できます。</p>
    <div class="btn-section">
      <a href="${pageContext.request.contextPath}/applicationMain" class="btn btn-primary">申請一覧へ</a>
      <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">メニューへ戻る</a>
    </div>
  </div>
</body>
</html>