<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - Step 1</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
  <script>
    const contextPath = "<%= request.getContextPath() %>";
  </script>
</head>
<body>
  <div class="page-container">
    <h2>出張費申請 - Step 1: 出張情報</h2>
    <form action="<%= request.getContextPath() %>/businessTrip" method="post" id="step1Form">
      <input type="hidden" name="step" value="1">
      <div class="form-section">
        <div class="form-group">
          <label>出張期間</label>
          <div style="display: flex; gap: 1rem;">
            <input type="date" name="startDate" id="startDate" required>
            <span>～</span>
            <input type="date" name="endDate" id="endDate" required>
          </div>
        </div>

        <div class="form-group">
          <label>PJコード</label>
          <select name="projectCode" required>
            <option value="">選択してください</option>
            <c:forEach var="p" items="${projectList}">
              <option value="${p.id}">${p.id}：${p.name}</option>
            </c:forEach>
          </select>
        </div>

        <div class="form-group">
          <label>出張報告</label>
          <textarea name="tripReport" class="hokoku-text" placeholder="業務内容や目的を入力してください"></textarea>
        </div>

        <input type="hidden" name="totalDays" id="totalDays">
        <input type="hidden" name="startDateHidden" id="startDateHidden">
        <input type="hidden" name="endDateHidden" id="endDateHidden">
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/home'">戻る</button>
        <button type="submit">次へ</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

  <script>
    document.getElementById("step1Form").addEventListener("submit", function(e) {
      const start = new Date(this.startDate.value);
      const end = new Date(this.endDate.value);
      if (end < start) {
        alert("終了日は開始日より後の日付を選択してください。");
        e.preventDefault();
        return;
      }
      const totalDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
      document.getElementById("totalDays").value = totalDays;
      document.getElementById("startDateHidden").value = this.startDate.value;
      document.getElementById("endDateHidden").value = this.endDate.value;
    });
  </script>
</body>
</html>