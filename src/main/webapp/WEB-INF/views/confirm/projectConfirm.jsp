<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="bean.ProjectList"%>
<%@ page import="java.util.Map"%>
<%
ProjectList bean = (ProjectList) request.getAttribute("projectConfirm");

// Format tiền có dấu phẩy
String budgetDisplay = "";
if (bean.getProject_budget() != null) {
    budgetDisplay = String.format("%,d", bean.getProject_budget());
}

// Xác định chế độ: register hoặc update
String mode = (String) request.getAttribute("confirmMode");
if (mode == null) {
    mode = "register"; // default là đăng ký
}
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロジェクト確認</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
body {
    font-family: "Yu Gothic UI", sans-serif;
    background-color: #fff;
    margin: 0;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
}
.page-container {
    max-width: 700px;
    background-color: white;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
    text-align: center;
}
h2 {
    color: #2c7be5;
    margin-bottom: 20px;
}
.confirm-box {
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    margin-bottom: 20px;
    margin-left: 20px;
    text-align: left;
}
.confirm-box div {
    margin-bottom: 12px;
    font-size: 1rem;
}
.btn-section {
    text-align: center;
}
.btn-section button {
    padding: 5px 15px;
    margin: 0 10px;
}
.info-row {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 10px;
}
.info-row .label {
    min-width: 160px;
    display: inline-block;
}
</style>
</head>
<body>
<div class="page-container">
    <h2>プロジェクト確認</h2>

    <div class="confirm-box">
        <div class="info-row">
            <span class="label">プロジェクトコード：</span><span><%=bean.getProject_code()%></span>
        </div>
        <div class="info-row">
            <span class="label">プロジェクト名：</span><span><%=bean.getProject_name()%></span>
        </div>
        <div class="info-row">
            <span class="label">プロジェクト責任者：</span><span><%=bean.getProject_owner()%></span>
        </div>
        <div class="info-row">
            <span class="label">開始日：</span><span><%=bean.getStart_date() != null ? bean.getStart_date() : ""%></span>
        </div>
        <div class="info-row">
            <span class="label">終了日：</span><span><%=bean.getEnd_date() != null ? bean.getEnd_date() : ""%></span>
        </div>
        <div class="info-row">
            <span class="label">予算（円）：</span><span><%=!budgetDisplay.isEmpty() ? budgetDisplay : "未登録"%></span>
        </div>
        <div class="info-row">
            <span class="label">プロジェクトメンバー：</span>
        </div>
<%
Map<String, String> memberMap = (Map<String, String>) request.getAttribute("memberMap");
if (memberMap != null && !memberMap.isEmpty()) {
    for (Map.Entry<String, String> entry : memberMap.entrySet()) {
%>
        <div class="info-row">
            <span class="label" style="visibility: hidden;">dummy：</span> <span><%= entry.getKey() %>：<%= entry.getValue() %></span>
        </div>
<%
    }
} else {
%>
        <div class="info-row">
            <span class="label" style="visibility: hidden;">dummy：</span> <span>未登録</span>
        </div>
<%
}
%>
    </div>

    <form action="<%=request.getContextPath()%>/projectControl" method="post">
        <input type="hidden" name="action" value="<%=mode%>">
        <input type="hidden" name="Project_code" value="<%=bean.getProject_code()%>">
        <input type="hidden" name="Project_name" value="<%=bean.getProject_name()%>">
        <input type="hidden" name="Project_owner" value="<%=bean.getProject_owner()%>">
        <input type="hidden" name="Start_date" value="<%=bean.getStart_date()%>">
        <input type="hidden" name="End_date" value="<%=bean.getEnd_date()%>">
        <input type="hidden" name="Project_budget" value="<%=bean.getProject_budget() != null ? bean.getProject_budget() : ""%>">
        <input type="hidden" name="memberIds" value="<%=bean.getProject_members() != null ? bean.getProject_members() : ""%>">

        <div class="btn-section">
            <button type="button" onclick="history.back()">戻る</button>
            <button type="submit">確定</button>
        </div>
    </form>
</div>
</body>
</html>
