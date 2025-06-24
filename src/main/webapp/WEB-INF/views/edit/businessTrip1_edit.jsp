<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% Boolean editMode = (Boolean) request.getAttribute("editMode"); %>
<% Integer applicationId = (Integer) request.getAttribute("applicationId"); %>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - Step 1</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>出張情報</h2>
    <form action="<%= request.getContextPath() %>/businessTrip" method="post" enctype="multipart/form-data" onsubmit="return setHiddenFields()">
      <input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
	  <% if (editMode != null && editMode) { %>
	    <input type="hidden" name="applicationId" value="<%= applicationId %>">
	  <% } %>
      <input type="hidden" name="step" value="1">
      <input type="hidden" name="totalDays" id="totalDays">
      <input type="hidden" name="startDateHidden" id="startDateHidden">
      <input type="hidden" name="endDateHidden" id="endDateHidden">

      <div class="form-section">
        <div class="form-group">
          <label>出張期間</label>
          <div style="display: flex; gap: 1rem;">
            <input type="date" name="startDate" id="startDate" value="${step1Data.startDate}">
            <span>～</span>
            <input type="date" name="endDate" id="endDate" value="${step1Data.endDate}" required>
          </div>
        </div>

        <div class="form-group">
          <label>PJコード</label>
          <select name="projectCode" required>
			  <option value="">選択してください</option>
			  <c:forEach var="p" items="${projectList}">
			    <option value="${p.id}" <c:if test="${p.id eq step1Data.projectCode}">selected</c:if>>
			      ${p.id}：${p.name}
			    </option>
			  </c:forEach>
		  </select>
        </div>

        <div class="form-group">
          <label>出張報告</label>
          <textarea name="tripReport" class="hokoku-text" placeholder="業務内容や目的を入力してください">${step1Data.report}</textarea>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/home'">戻る</button>
        <button type="submit">次へ</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
  
  <script>
    function setHiddenFields() {
      const startInput = document.getElementById("startDate");
      const endInput = document.getElementById("endDate");
      const start = new Date(startInput.value);
      const end = new Date(endInput.value);
      if (end < start) {
        alert("終了日は開始日より後の日付を選択してください。");
        return false;
      }
      const totalDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
      document.getElementById("totalDays").value = totalDays;
      document.getElementById("startDateHidden").value = startInput.value;
      document.getElementById("endDateHidden").value = endInput.value;
      return true;
    }
  </script>
</body>
</html>