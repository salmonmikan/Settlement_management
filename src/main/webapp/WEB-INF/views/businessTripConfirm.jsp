<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
  String tripStartDate = (String) request.getAttribute("tripStartDate");
  String tripEndDate = (String) request.getAttribute("tripEndDate");
  String projectCode = (String) request.getAttribute("projectCode");
  long numOfDays = (Long) request.getAttribute("numOfDays");

  String[] regionTypes = (String[]) request.getAttribute("regionTypes");
  String[] tripTypes = (String[]) request.getAttribute("tripTypes");
  String[] nightAllowances = (String[]) request.getAttribute("nightAllowance");
  String[] burdensStep2 = (String[]) request.getAttribute("burdensStep2");
  String[] hotelFees = (String[]) request.getAttribute("hotelFees");
  String[] dailyAllowances = (String[]) request.getAttribute("dailyAllowances");
  String[] days = (String[]) request.getAttribute("days");
  String[] totals = (String[]) request.getAttribute("totals");
  String[] memos = (String[]) request.getAttribute("memos");

  String[] transDates = (String[]) request.getAttribute("transDates");
  String[] departures = (String[]) request.getAttribute("departures");
  String[] arrivals = (String[]) request.getAttribute("arrivals");
  String[] transports = (String[]) request.getAttribute("transports");
  String[] fareAmounts = (String[]) request.getAttribute("fareAmounts");
  String[] transTripTypes = (String[]) request.getAttribute("transTripTypes");
  String[] burdensStep3 = (String[]) request.getAttribute("burdensStep3");
  String[] expenseTotals = (String[]) request.getAttribute("expenseTotals");
  String[] transMemos = (String[]) request.getAttribute("transMemos");

  Map<String, List<String>> receiptMap = (Map<String, List<String>>) request.getAttribute("receiptMap");

  // Tính tổng
  int totalStep2 = 0;
  if (totals != null) {
    for (String t : totals) totalStep2 += Integer.parseInt(t);
  }

  int totalStep3 = 0;
  if (expenseTotals != null) {
    for (String t : expenseTotals) totalStep3 += Integer.parseInt(t);
  }
  int totalAmount = totalStep2 + totalStep3;
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 内容確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .confirmPage-total {
      margin-top: 10px;
      text-align: right;
      background-color: #e0f7fa;
      padding: 5px 10px;
      font-weight: bold;
    }
  </style>
</head>
<body>
<div class="page-container">
  <h2>出張費申請内容の確認</h2>
  <p>以下の内容で申請します。間違いがなければ「送信」をクリックしてください。</p>

  <!-- Step1 -->
  <div class="form-section">
    <h3>基本情報</h3>
    <table>
      <tr><th>出張期間</th><td><%= tripStartDate %> ～ <%= tripEndDate %></td></tr>
      <tr><th>PJコード</th><td><%= projectCode %></td></tr>
      <tr><th>合計日数</th><td><%= numOfDays %>日間</td></tr>
    </table>
  </div>

  <!-- Step2 -->
  <div class="form-section">
    <h3>日当・宿泊費明細</h3>
    <table>
      <tr>
        <th>地域区分</th><th>出徒区分</th><th>負担者</th><th>宿泊費</th>
        <th>日当</th><th>宿泊費</th><th>日数</th><th>合計</th><th>摘要</th>
      </tr>
      <% if (regionTypes != null) {
        for (int i = 0; i < regionTypes.length; i++) { %>
      <tr>
        <td><%= regionTypes[i] %></td>
        <td><%= tripTypes[i] %></td>
        <td><%= burdensStep2[i] %></td>
        <td><%= hotelFees[i] %></td>
        <td><%= dailyAllowances[i] %></td>
        <td><%= hotelFees[i] %></td>
        <td><%= days[i] %></td>
        <td><%= totals[i] %></td>
        <td><%= memos[i] %></td>
      </tr>
      <% } } %>
    </table>
    <p class="confirmPage-total">日当・宿泊費 合計: <%= totalStep2 %> 円</p>

    <% 
      List<String> step2Files = receiptMap != null ? receiptMap.getOrDefault("step2", Collections.emptyList()) : Collections.emptyList();
      if (!step2Files.isEmpty()) {
    %>
    <div class="form-section">
      <h4>日当・宿泊費 領収書ファイル:</h4>
      <ul>
        <% for (int i = 0; i < step2Files.size(); i++) {
             String file = step2Files.get(i);
             String original = file.substring(file.indexOf("_") + 1);
        %>
          <li><%= original %></li>
        <% } %>
      </ul>
    </div>
    <% } %>
  </div>

  <!-- Step3 -->
  <div class="form-section">
    <h3>交通費明細</h3>
    <table>
      <tr>
        <th>訪問日</th><th>出発</th><th>到着</th><th>交通機関</th>
        <th>金額</th><th>区分</th><th>負担者</th><th>合計</th><th>摘要</th>
      </tr>
      <% if (transDates != null) {
        for (int i = 0; i < transDates.length; i++) { %>
      <tr>
        <td><%= transDates[i] %></td>
        <td><%= departures[i] %></td>
        <td><%= arrivals[i] %></td>
        <td><%= transports[i] %></td>
        <td><%= fareAmounts[i] %></td>
        <td><%= transTripTypes[i] %></td>
        <td><%= burdensStep3[i] %></td>
        <td><%= expenseTotals[i] %></td>
        <td><%= transMemos[i] %></td>
      </tr>
      <% } } %>
    </table>
    <p class="confirmPage-total">交通費 合計: <%= totalStep3 %> 円</p>

    <% 
      List<String> step3Files = receiptMap != null ? receiptMap.getOrDefault("step3", Collections.emptyList()) : Collections.emptyList();
      if (!step3Files.isEmpty()) {
    %>
    <div class="form-section">
      <h4>交通費 領収書ファイル:</h4>
      <ul>
        <% for (int i = 0; i < step3Files.size(); i++) {
             String file = step3Files.get(i);
             String original = file.substring(file.indexOf("_") + 1);
        %>
          <li><%= original %></li>
        <% } %>
      </ul>
    </div>
    <% } %>
  </div>

  <div class="confirmPage-total">総合計金額: <%= totalAmount %> 円</div>

  <div class="btn-section">
    <form action="<%= request.getContextPath() %>/businessTripConfirmBack" method="get" style="display:inline;">
      <button type="submit">戻る</button>
    </form>
    <form action="<%= request.getContextPath() %>/submitBusinessTrip" method="post">
      <button type="submit">送信</button>
    </form>
  </div>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>
