<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>処理結果</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .result-container {
            max-width: 600px;
            margin: 40px auto;
            padding: 30px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .result-container.success { background-color: #f0fff4; border: 1px solid #4caf50; }
        .result-container.error { background-color: #fff0f0; border: 1px solid #f44336; }
        .result-icon { font-size: 4rem; }
        .success .result-icon { color: #4caf50; }
        .error .result-icon { color: #f44336; }
        .result-message { font-size: 1.1rem; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="page-container">
        <div class="result-container ${status}">
            <div class="result-icon">
                <c:if test="${status == 'success'}">✓</c:if>
                <c:if test="${status == 'error'}">✗</c:if>
            </div>
            <h2>
                <c:if test="${status == 'success'}">更新成功</c:if>
                <c:if test="${status == 'error'}">更新失敗</c:if>
            </h2>
            <p class="result-message">${message}</p>
            <div class="btn-section">
                <button type="button" class="next-btn" onclick="window.location.href='${pageContext.request.contextPath}/applicationMain'">リストに</button>
            </div>
        </div>
    </div>
    <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>