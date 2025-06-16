<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請内容確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>出張費申請 - 内容確認</h2>

    <form style="display:flex; flex-direction: column; gap: 10px" action="submitFinal" method="post">
      <div class="form-section">
        <h3>出張情報</h3>
        <p><strong>出張期間:</strong> <%= request.getParameter("startDate") %> ～ <%= request.getParameter("endDate") %></p>
        <p><strong>地域区分:</strong> <%= request.getParameter("regionType") %></p>
        <p><strong>出張区分:</strong> <%= request.getParameter("tripType") %></p>
        <p><strong>PJコード:</strong> <%= request.getParameter("projectCode") %></p>
        <p><strong>出張報告:</strong><br><%= request.getParameter("tripReport") %></p>
      </div>

      <div class="form-section">
        <h3>日当・宿泊費明細</h3>
        <p><strong>宿泊先:</strong> <%= request.getParameter("hotel") %></p>
        <p><strong>宿泊費:</strong> <%= request.getParameter("hotelFee") %></p>
        <p><strong>日当:</strong> <%= request.getParameter("dailyAllowance") %></p>
        <p><strong>日数:</strong> <%= request.getParameter("days") %></p>
        <p><strong>負担者:</strong> <%= request.getParameter("burden") %></p>
        <p><strong>合計:</strong> <%= request.getParameter("expenseTotal") %></p>
        <p><strong>摘要:</strong><br><%= request.getParameter("memo") %></p>
      </div>

      <div class="form-section">
        <h3>交通費明細</h3>
        <p><strong>訪問先:</strong> <%= request.getParameter("transProject") %></p>
        <p><strong>出発:</strong> <%= request.getParameter("departure") %></p>
        <p><strong>到着:</strong> <%= request.getParameter("arrival") %></p>
        <p><strong>区分:</strong> <%= request.getParameter("transTripType") %></p>
        <p><strong>交通機関:</strong> <%= request.getParameter("transport") %></p>
        <p><strong>負担者:</strong> <%= request.getParameter("burden") %></p>
        <p><strong>金額（税込）:</strong> <%= request.getParameter("expenseTotal") %></p>
        <p><strong>摘要:</strong><br><%= request.getParameter("transMemo") %></p>
      </div>

      <div class="btn-section">
        <button type="button" onclick="history.back()">戻る</button>
        <button type="submit">申請</button>
      </div>
    </form>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>