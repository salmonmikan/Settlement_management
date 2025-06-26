<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.PositionBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    PositionBean bean = (PositionBean) request.getAttribute("position");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>役職編集</title>
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
    <h2>役職編集</h2>
    <form action="positionControl" method="post">
        <input type="hidden" name="action" value="update">

        <div>
            <label for="position_id">役職ID：</label>
            <span><%= bean.getPosition_id() %></span>
            <input type="hidden" name="position_id" value="<%= bean.getPosition_id() %>">
        </div>

        <div>
            <label for="position_name">役職名：</label>
            <input type="text" id="position_name" name="position_name" value="<%= bean.getPosition_name() %>" required>
        </div>

        <div class="button-row">
            <button type="button" onclick="history.back();">戻る</button>
            <button type="submit">更新</button>
        </div>
    </form>
</div>

</body>
</html>
