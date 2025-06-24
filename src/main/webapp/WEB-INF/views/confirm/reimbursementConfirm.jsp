<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>立替金申請 - 内容確認</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>

<div class="page-container">
    <h2>立替金申請内容の確認</h2>
    <p>以下の内容で申請します。間違いがなければ「送信」をクリックしてください。</p>

    <table border="1" style="width:100%; border-collapse: collapse;">
        <tr>
            <th>訪問日</th>
            <th>訪問先</th>
            <th>PJコード</th>
            <th>報告書</th>
            <th>勘定科目</th>
            <th>摘要</th>
            <th>金額</th>
        </tr>

        <c:forEach var="i" begin="0" end="${fn:length(dateList) - 1}">
            <tr>
                <td>${dateList[i]}</td>
                <td>${destinationsList[i]}</td>
                <td>${projectCodeList[i]}</td>
                <td>${reportList[i]}</td>
                <td>${accountingItemList[i]}</td>
                <td>${memoList[i]}</td>
                <td>${amountList[i]} 円</td>
            </tr>
        </c:forEach>
    </table>

    <div class="btn-section" style="margin-top: 20px;">
        
        <!-- Form quay lại màn nhập liệu -->
        <form action="<%= request.getContextPath() %>/reimbursement" method="get" style="display:inline;">
            <button type="submit">戻る</button>
        </form>

        <!-- Form gửi dữ liệu chính xác sang submitReimbursement -->
        <form action="<%= request.getContextPath() %>/submitReimbursement" method="post" style="display:inline;">
            <c:forEach var="i" begin="0" end="${fn:length(dateList) - 1}">
                <input type="hidden" name="date[]" value="${dateList[i]}">
                <input type="hidden" name="destinations[]" value="${destinationsList[i]}">
                <input type="hidden" name="projectCode[]" value="${projectCodeList[i]}">
                <input type="hidden" name="report[]" value="${reportList[i]}">
                <input type="hidden" name="accountingItem[]" value="${accountingItemList[i]}">
                <input type="hidden" name="memo[]" value="${memoList[i]}">
                <input type="hidden" name="amount[]" value="${amountList[i]}">
            </c:forEach>
            <button type="submit">送信</button>
        </form>
    </div>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

</body>
</html>
