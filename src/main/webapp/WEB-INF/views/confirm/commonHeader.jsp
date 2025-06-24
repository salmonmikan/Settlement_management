<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");

  Boolean isDetailMode = (Boolean) request.getAttribute("isDetailMode");
  isDetailMode = (isDetailMode != null) ? isDetailMode : false;

  Boolean isApprovalScreen = (Boolean) request.getAttribute("isApprovalScreen");
  isApprovalScreen = (isApprovalScreen != null) ? isApprovalScreen : false;
%>
<head>
  <meta charset="UTF-8">
  <title><%= type %>申請内容の確認</title>
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

<div style="padding-bottom: 0" class="page-container">
  
    <h2><%= type %>申請内容の確認</h2>
    <p style="text-align:center; margin-bottom: 0">以下の内容で申請します。間違いがなければ「送信」をクリックしてください。</p>
</div>