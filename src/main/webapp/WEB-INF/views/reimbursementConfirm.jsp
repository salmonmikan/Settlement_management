<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.BusinessTripBean.*, java.util.*" %>
<%
  ReimbursementBean bean = (ReimbursementBean) session.getAttribute("reimbursementBean");
  Step1Data s1 = bean.getStep1();
  List<Step2Detail> s2List = bean.getStep2List();
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>立替金申請内容の確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>立替金申請内容の確認</h2>
    <p>以下の内容で申請します。内容をご確認の上、「送信」をクリックしてください。</p>

    <!-- Step1 情報 -->
    <div class="form-section">
      <h3>基本情報</h3>
      <table>
        <tr><th>訪問日</th><td><%= s1.getDate() %></td></tr>
        <tr><th>PJコード</th><td><%= s1.getProjectCode() %></td></tr>
        <tr><th>報告書</th><td><%= s1.getReport() %></td></tr>
      </table>
    </div>

    <!-- Step2 情報 -->
    <div class="form-section">
      <h3>経費明細</h3>
      <table>
        <tr>
          <th>勘定科目</th><th>摘要</th><th>金額</th>
        </tr>
        <%
          for (Step2Detail s2 : s2List) {
        %>
        <tr>
          <td><%= s2.getAccountingItem() %></td>
          <td><%= s2.getMemo() %></td>
          <td><%= s2.getAmount() %> 円</td>
        </tr>
        <%
          }
        %>
      </table>
    </div>

    <!-- ボタン -->
    <div class="btn-section">
      <form action="<%= request.getContextPath() %>/reimbursementConfirmBack" method="get" style="display:inline;">
        <button type="submit">戻る</button>
      </form>
      <form action="#" method="post" style="display:inline;">
        <input type="hidden" name="step" value="3">
        <button type="submit">送信</button>
      </form>
    </div>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>
