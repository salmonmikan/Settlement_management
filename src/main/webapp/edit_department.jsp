<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.DepartmentBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    DepartmentBean bean = (DepartmentBean) request.getAttribute("department");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>部署編集</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
.container {
    max-width: 500px;
    margin: 40px auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
}
h2 {
    text-align: center;
    margin-bottom: 20px;
}
form {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
label {
    font-weight: bold;
}
input[type="text"] {
    padding: 8px;
    font-size: 1rem;
}
.button-row {
    display: flex;
    justify-content: space-between;
    gap: 10px;
}
button {
    padding: 8px 16px;
    font-size: 1rem;
}
</style>
</head>
<body>

<div class="container">
    <h2>部署編集</h2>
    <form action="department" method="post">
        <input type="hidden" name="action" value="update">

        <div>
            <label for="department_id">部署ID：</label>
            <span><%= bean.getDepartment_id() %></span>
            <input type="hidden" name="department_id" value="<%= bean.getDepartment_id() %>">
        </div>

        <div>
            <label for="department_name">部署名：</label>
            <input type="text" id="department_name" name="department_name" value="<%= bean.getDepartment_name() %>" required>
        </div>

        <div class="button-row">
            <button type="button" onclick="history.back();">戻る</button>
            <button type="submit">更新</button>
        </div>
    </form>
</div>

</body>
</html>
