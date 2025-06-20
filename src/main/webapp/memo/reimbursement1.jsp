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
  
  <style>
  .reportGroup{
  	border: 1px solid var(--border-color);
    padding: 20px;
    border-radius: 10px;
  }
  
  </style>
</head>
<body>
  <div class="page-container">
    <h2>立替金精算書</h2>
    <form action="#" method="post" id="step1Form">
      <input type="hidden" name="step" value="1">
      <div class="form-section">
        <div class="form-group">
          <label>訪問期間</label>
          <div style="display: flex; gap: 1rem;">
            <input type="date" name="date" id="date" required>
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

<!--        <div class="form-group">-->
<!--          <label>報告書</label>-->
<!--          <div class="reportGroup">-->
<!--          <label>訪問先担当者 : <input type="text" name="tantousha"></label>-->
<!--          <label>目的 : <input type="text" name="mokuteki"></label>-->
<!--          <label>行動報告 : <input type="text" name="houkoku"></label>-->
<!--          <label>成果 : <input type="text" name="seika"></label>-->
<!--          <label>今後の課題 : <input type="text" name="kadai"></label>-->
<!--          </div>-->
<!--        </div>-->

		<div class="form-group">
          <label>報告書</label>
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
