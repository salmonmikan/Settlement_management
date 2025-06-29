<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>エラー発生</title>
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
      border-top: 5px solid #dc3545; /* Màu đỏ lỗi */
    }
    .icon {
      font-size: 5rem;
      color: #dc3545;
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
    .error-details {
      margin-top: 15px;
      padding: 10px;
      background-color: #f8d7da;
      border: 1px solid #f5c6cb;
      color: #721c24;
      border-radius: 5px;
      font-family: monospace;
    }
    .btn-section { margin-top: 30px; }
    .btn { padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; background-color: #6c757d; color: white; transition: all 0.3s ease; }
    .btn:hover { background-color: #5a6268; }
  </style>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
  <div class="result-container">
    <div class="icon"><i class="fas fa-exclamation-triangle"></i></div>
    <h2>処理中にエラーが発生しました</h2>
    <p>システムエラーのため、処理を完了できませんでした。もう一度お試しいただくか、管理者にお問い合わせください。</p>
    
    <%-- Hiển thị thông điệp lỗi cụ thể nếu có --%>
    <c:if test="${not empty requestScope.errorMessage}">
        <div class="error-details">
            <strong>詳細:</strong> ${requestScope.errorMessage}
        </div>
    </c:if>
    
    <div class="btn-section">
      <a href="${pageContext.request.contextPath}/home" class="btn">メニューへ戻る</a>
    </div>
  </div>
</body>
</html>