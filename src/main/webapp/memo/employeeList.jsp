<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Employee" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>社員一覧 - 管理画面</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .page-container {
      max-width: 80%;
      margin: 30px auto;
      background-color: white;
      border-radius: 10px;
      padding: 20px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
      font-size: 0.95rem;
    }
    th, td {
      padding: 10px;
      border: 1px solid #ccc;
      text-align: center;
    }
    th {
      background-color: #e6f0fa;
    }
    h2 {
      color: #2c7be5;
      margin-bottom: 20px;
      text-align: center;
    }
    .center-link {
      display: block;
      text-align: center;
      margin-top: 30px;
      font-size: 1rem;
      color: #2c7be5;
      text-decoration: none;
    }
    .center-link:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2>社員一覧</h2>
    <table>
      <thead>
        <tr>
          <th>社員ID</th>
          <th>氏名</th>
          <th>ふりがな</th>
          <th>生年月日</th>
          <th>住所</th>
          <th>入社日</th>
          <th>ログインID</th>
          <th>パスワード</th>
          <th>部署</th>
          <th>役職</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="emp" items="${employeeList}">
          <tr>
            <td>${emp.employeeId}</td>
            <td>${emp.fullName}</td>
            <td>${emp.furigana}</td>
            <td>${emp.birthDate}</td>
            <td>${emp.address}</td>
            <td>${emp.joinDate}</td>
            <td>${emp.loginId}</td>
            <td>${emp.password}</td>
            <td>${emp.department}</td>  
            <td>${emp.position}</td>    
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <a href="<%= request.getContextPath() %>/employeeRegister.jsp" class="center-link">アカウント登録はこちら</a>
  </div>
</body>
</html>
