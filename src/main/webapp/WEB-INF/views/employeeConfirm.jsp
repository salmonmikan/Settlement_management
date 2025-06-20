<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("employeeTemp");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/employeeRegisterPage");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>社員登録確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .page-container {
      max-width: 700px;
      margin: 40px auto;
      background-color: white;
      border-radius: 10px;
      padding: 30px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 30px;
    }
    th, td {
      padding: 10px;
      border: 1px solid #ccc;
      text-align: left;
    }
    th {
      background-color: #e6f0fa;
      width: 30%;
    }
    .btn-section {
      text-align: center;
    }
    .btn-section button {
      margin: 0 10px;
      padding: 10px 25px;
      background-color: #2c7be5;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    .btn-section button:hover {
      opacity: 0.9;
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2 style="text-align:center; color:#2c7be5;">登録内容の確認</h2>
    <table>
      <tr><th>社員ID</th><td><%= emp.getEmployeeId() %></td></tr>
      <tr><th>氏名</th><td><%= emp.getFullName() %></td></tr>
      <tr><th>ふりがな</th><td><%= emp.getFurigana() %></td></tr>
      <tr><th>生年月日</th><td><%= emp.getBirthDate() %></td></tr>
      <tr><th>住所</th><td><%= emp.getAddress() %></td></tr>
      <tr><th>入社日</th><td><%= emp.getJoinDate() %></td></tr>
      <tr><th>ログインID</th><td><%= emp.getLoginId() %></td></tr>
      <tr><th>部署ID</th><td><%= emp.getDepartmentId() %></td></tr>
      <tr><th>役職ID</th><td><%= emp.getPositionId() %></td></tr>
    </table>

    <div class="btn-section">
      <!-- Gửi form để đăng ký -->
      <form action="<%= request.getContextPath() %>/employeeRegisterPage" method="post" style="display: inline;">
        <input type="hidden" name="action" value="register">
        <button type="submit">登録</button>
      </form>

      <!-- Quay lại form nhập -->
      <form action="<%= request.getContextPath() %>/employeeRegisterPage" method="get" style="display: inline;">
        <button type="submit">戻る</button>
      </form>
    </div>
  </div>
</body>
</html>
