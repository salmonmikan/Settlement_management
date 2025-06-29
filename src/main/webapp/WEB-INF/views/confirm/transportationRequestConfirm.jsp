<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, bean.TransportationRequest" %>
<%
    List<TransportationRequest> transList = (List<TransportationRequest>) session.getAttribute("transportationList");
    Integer grandTotal = (Integer) session.getAttribute("transportationTotal");

    if (transList == null) transList = new ArrayList<>();
    if (grandTotal == null) grandTotal = 0;
%>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>交通費申請 - 内容確認</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>
<div class="page-container">
    <h2>交通費申請内容の確認</h2>
    <p>以下の内容で申請します。間違いがなければ「送信」をクリックしてください。</p>

    <table border="1" style="width:100%; border-collapse: collapse;">
        <tr>
            <th>日付</th>
            <th>PJコード</th>
            <th>出発</th>
            <th>到着</th>
            <th>区分</th>
            <th>交通機関</th>
            <th>負担者</th>
            <th>金額</th>
            <th>合計</th>
            <th>摘要</th>
        </tr>
        <% for (TransportationRequest t : transList) { %>
        <tr>
            <td><%= t.getDate() %></td>
            <td><%= t.getProjectCode() %></td>
            <td><%= t.getDepartureStation() %></td>
            <td><%= t.getArrivalStation() %></td>
            <td><%= t.getCategory() %></td>
            <td><%= t.getTransportType() %></td>
            <td><%= t.getPayer() %></td>
            <td><%= t.getAmount() %> 円</td>
            <td><%= t.getTotalAmount() %> 円</td>
            <td><%= t.getAbstractNote() %></td>
        </tr>
        <% } %>
    </table>

    <p style="text-align: right; font-weight: bold; margin-top: 20px;">
        総合計金額: <%= grandTotal %> 円
    </p>

    <div class="btn-section" style="margin-top: 20px;">
        <form action="<%= request.getContextPath() %>/transportationRequest" method="get" style="display:inline;">
            <button type="submit">戻る</button>
        </form>
        <form action="<%= request.getContextPath() %>/submitTransportationRequest" method="post" style="display:inline;">
            <button type="submit">送信</button>
        </form>
    </div>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>
