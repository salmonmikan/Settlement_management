<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
  if (type == null) type = "申請";
%>
<head>
  <meta charset="UTF-8">
  <title><%= type %>申請内容の詳細</title>
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

<div class="page-container">
  <h2><%= type %>申請内容の詳細</h2>
  <p style="text-align:center; margin-bottom: 15px">以下は申請済みの内容です。</p>