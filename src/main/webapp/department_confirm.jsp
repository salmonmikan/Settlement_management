<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.DepartmentBean" %>
<%
    DepartmentBean bean = (DepartmentBean) request.getAttribute("department");
    String action = (String) request.getAttribute("action");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>部署確認</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
.container {
    max-width: 400px;
    margin: 40px auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #fff;
    text-align: center;
}
h2 {
    margin-bottom: 20px;
}
.box {
    padding: 30px;
    margin-bottom: 20px;
}
.label {
    margin: 10px 0;
    font-weight: bold;
}
.value {
    margin-bottom: 20px;
    font-size: 1rem;
    font-weight: bold;
}
.button-row {
    display: flex;
    justify-content: center;
    gap: 30px;
}
button {
    padding: 8px 16px;
    font-size: 1rem;
}
</style>
</head>
<body>

<div class="container">
    <h2>部署確認</h2>
    <div class="box">
        <div class="label">部署ID</div>
        <div class="value"><%= bean.getDepartment_id() %></div>

        <div class="label">部署名</div>
        <div class="value"><%= bean.getDepartment_name() %></div>
    </div>

    <form action="department" method="post">
        <input type="hidden" name="action" value="<%= action %>">
        <input type="hidden" name="department_id" value="<%= bean.getDepartment_id() %>">
        <input type="hidden" name="department_name" value="<%= bean.getDepartment_name() %>">
        <div class="button-row">
            <button type="button" onclick="history.back();">戻る</button>
            <button type="submit">確認</button>
        </div>
    </form>
</div>

</body>
</html>
