<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.PositionBean" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>役職確認</title>
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
    border: 1px solid #aaa;
    padding: 30px;
    margin-bottom: 20px;
}
.label {
    margin: 10px 0;
    font-weight: bold;
}
.value {
    margin-bottom: 20px;
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

<%
    PositionBean bean = (PositionBean) request.getAttribute("position");
    String action = (String) request.getAttribute("action"); // "create" or "update"
%>

<div class="container">
    <h2>役職確認</h2>
    <div class="box">
        <div class="label">役職ID</div>
        <div class="value"><%= bean.getPosition_id() %></div>

        <div class="label">役職名</div>
        <div class="value"><%= bean.getPosition_name() %></div>
    </div>

    <form action="positionControl" method="post">
        <input type="hidden" name="action" value="<%= action %>">
        <input type="hidden" name="position_id" value="<%= bean.getPosition_id() %>">
        <input type="hidden" name="position_name" value="<%= bean.getPosition_name() %>">
        <div class="button-row">
            <button type="button" onclick="history.back();">戻る</button>
            <button type="submit">確認</button>
        </div>
    </form>
</div>

</body>
</html>
