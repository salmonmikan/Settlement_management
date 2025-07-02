<%@ page contentType="text/html; charset=UTF-8" %>
<head>
  <meta charset="UTF-8">
  <title>出張費申請内容の確認</title>
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
<%
	String type = (String) request.getAttribute("application_type");

	String view_mode = (String) request.getAttribute("view_mode");
	String title = "申請内容の確認";
	String message = "以下の内容で申請します。間違いがなければ「送信」をクリックしてください。";
	
	if ("view".equals(view_mode)) {
	    title = "申請内容の詳細";
	    message = "以下は登録済みの申請内容です。編集したい場合は「編集」をクリックしてください。";
	}
%>
<div style="padding-bottom: 0" class="page-container">
  <h2><%= type %><%= title %></h2>
  <p style="text-align:center; margin-bottom: 0"><%= message %></p>
  </div>