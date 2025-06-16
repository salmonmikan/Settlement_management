<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - Step 1</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <script>
    const contextPath = "<%= request.getContextPath() %>";
  </script>
</head>
<body>
  <div class="page-container">
    <h2>出張費申請 - Step 1: 出張情報</h2>
    <form action="businessTrip2.jsp" method="post" id="step1Form">
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
          <select name="projectCode">
            <option value="PJ101">PJ101：A社向け新規提案プロジェクト</option>
            <option value="PJ102">PJ102：年間契約更新キャンペーン</option>
            <option value="PJ103">PJ103：大阪展示会出展準備</option>
            <option value="PJ104">PJ104：代理店開拓プロジェクト</option>
            <option value="PJ105">PJ105：顧客満足度向上活動</option>
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
        <button type="button" onclick="window.location.href='staffMenu.jsp'">戻る</button>
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