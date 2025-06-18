<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.BusinessTripBean.*, java.util.*" %>
<%
  BusinessTripBean bt = (BusinessTripBean) session.getAttribute("businessTripBean");
  Step1Data s1 = bt.getStep1Data();
  List<Step2Detail> s2List = bt.getStep2List();
  List<Step3Detail> s3List = bt.getStep3List();
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 内容確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>出張費申請内容の確認</h2>
    <p>以下の内容で申請します。間違いがなければ「送信」をクリックしてください。</p>

    <!-- Step1 内容 -->
    <div class="form-section">
      <h3>基本情報</h3>
      <table>
        <tr><th>出張期間</th><td><%= s1.getStartDate() %> ～ <%= s1.getEndDate() %></td></tr>
        <tr><th>PJコード</th><td><%= s1.getProjectCode() %></td></tr>
        <tr><th>出張報告</th><td><%= s1.getReport() %></td></tr>
        <tr><th>合計日数</th><td><%= s1.getTotalDays() %>日間</td></tr>
      </table>
    </div>

    <!-- Step2 内容 -->
    <div class="form-section">
      <h3>日当・宿泊費明細</h3>
      <table>
        <tr>
          <th>地域区分</th><th>出徒区分</th><th>負担者</th><th>宿泊先</th>
          <th>日当</th><th>宿泊費</th><th>日数</th><th>合計</th><th>摘要</th>
        </tr>
        <%
          for (Step2Detail s2 : s2List) {
        %>
        <tr>
          <td><%= s2.getRegionType() %></td>
          <td><%= s2.getTripType() %></td>
          <td><%= s2.getBurden() %></td>
          <td><%= s2.getHotel() %></td>
          <td><%= s2.getDailyAllowance() %></td>
          <td><%= s2.getHotelFee() %></td>
          <td><%= s2.getDays() %></td>
          <td><%= s2.getExpenseTotal() %></td>
          <td><%= s2.getMemo() %></td>
        </tr>
        <%
          }
        %>
      </table>
    </div>

    <!-- Step3 内容 -->
    <div class="form-section">
      <h3>交通費明細</h3>
      <table>
        <tr>
          <th>訪問先</th><th>出発</th><th>到着</th><th>交通機関</th>
          <th>金額</th><th>区分</th><th>負担者</th><th>合計</th><th>摘要</th>
        </tr>
        <%
          for (Step3Detail s3 : s3List) {
        %>
        <tr>
          <td><%= s3.getTransProject() %></td>
          <td><%= s3.getDeparture() %></td>
          <td><%= s3.getArrival() %></td>
          <td><%= s3.getTransport() %></td>
          <td><%= s3.getFareAmount() %></td>
          <td><%= s3.getTransTripType() %></td>
          <td><%= s3.getTransBurden() %></td>
          <td><%= s3.getTransExpenseTotal() %></td>
          <td><%= s3.getTransMemo() %></td>
        </tr>
        <%
          }
        %>
      </table>
    </div>

    <!-- ボタン -->
    <div class="btn-section">
      <button type="button" onclick="history.back()">戻る</button>
      <form action="<%= request.getContextPath() %>/businessTripSubmit" method="post" style="display:inline;">
        <button type="submit">送信</button>
      </form>
    </div>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>