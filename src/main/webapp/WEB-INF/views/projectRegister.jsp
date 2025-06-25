<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.ProjectList" %>
<%
  ProjectList bean = (ProjectList) request.getAttribute("project_edit"); // 編集時のbean
  boolean isEdit = (bean != null); // beanがあれば編集モード
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title><%= isEdit ? "プロジェクト編集" : "プロジェクト登録" %></title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <style>
    .form-group input, .form-group select {
      width: 100%;
      padding: 8px;
      font-size: 1rem;
    }
    .btn-section {
      text-align: center;
      margin-top: 20px;
    }
    .btn-section button {
      padding: 5px 10px;
      margin: 0 10px;
    }
    h2 {
      text-align: center;
      color: #2c7be5;
    }
    .page-container {
      max-width: 700px;
      margin: 30px auto;
      background-color: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2><%= isEdit ? "プロジェクト編集" : "プロジェクト登録" %></h2>

    <form action="<%= request.getContextPath() %>/projectControl" method="post">
      <input type="hidden" name="action" value="<%= isEdit ? "confirmUpdate" : "confirmInsert" %>">

      <div class="form-section">
        <div class="form-group">
          <label>プロジェクトコード</label>
          <input type="text" name="Project_code" required maxlength="8"
            value="<%= isEdit ? bean.getProject_code() : "" %>" <%= isEdit ? "readonly" : "" %> >
        </div>
        <div class="form-group">
          <label>プロジェクト名</label>
          <input type="text" name="Project_name" required maxlength="20"
            value="<%= isEdit ? bean.getProject_name() : "" %>">
        </div>
        <div class="form-group">
          <label>プロジェクト責任者</label>
          <input type="text" name="Project_owner" required maxlength="20"
            value="<%= isEdit ? bean.getProject_owner() : "" %>">
        </div>
        <div class="form-group">
          <label>開始日</label>
          <input type="date" name="Start_date"
            value="<%= isEdit && bean.getStart_date() != null && !bean.getStart_date().equals("登録なし") ? bean.getStart_date() : "" %>">
        </div>
        <div class="form-group">
          <label>終了日</label>
          <input type="date" name="End_date"
            value="<%= isEdit && bean.getEnd_date() != null && !bean.getEnd_date().equals("登録なし") ? bean.getEnd_date() : "" %>">
        </div>
        <div class="form-group">
          <label>予算（円）</label>
          <input type="number" name="Project_budget" min="0"
            value="<%= isEdit && bean.getProject_budget() != null ? bean.getProject_budget() : "" %>">
        </div>
        <div class="form-group">
          <label>社員ID（カンマ区切りで複数可）</label>
          <input type="text" name="memberIds"
            value="<%= isEdit && bean.getProject_members() != null && !bean.getProject_members().equals("登録なし") ? bean.getProject_members() : "" %>"
            placeholder="例: E001,E002,E003">
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="location.href='<%= request.getContextPath() %>/project_management_view'">戻る</button>
        <button type="submit"><%= isEdit ? "更新" : "登録" %></button>
      </div>
    </form>
  </div>
</body>
</html>
