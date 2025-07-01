<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>処理結果</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
        }
        /* Màu cho icon thành công */
        .icon.success {
            color: #28a745; 
        }
        /* Màu cho icon thất bại */
        .icon.error {
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
            border: none;
            cursor: pointer;
        }
        .btn-primary { background-color: var(--primary-color); color: white; }
        .btn-primary:hover { background-color: var(--primary-color-dark); }
        .btn-secondary { background-color: #6c757d; color: white; }
        .btn-secondary:hover { background-color: #5a6268; }
    </style>
</head>
<body>
    <div class="result-container">
        <%-- <c:if test="${status == 'success'}">
            <i class="icon success fa-solid fa-circle-check"></i>
        </c:if>
        <c:if test="${status == 'error'}">
            <i class="icon error fa-solid fa-circle-xmark"></i>
        </c:if> --%>

        <h2>
            <c:if test="${status == 'success'}">更新が完了しました</c:if>
            <c:if test="${status == 'error'}">更新失敗</c:if>
        </h2>

        <p>${message}</p>

        <div class="btn-section">
            <a href="${pageContext.request.contextPath}/applicationMain" class="btn btn-primary">申請一覧へ</a>
            
            <c:if test="${status == 'success'}">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">メニューへ戻る</a>
            </c:if>
        </div>
    </div>
</body>
</html>