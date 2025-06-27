<%@ page contentType="text/html; charset=UTF-8" %>
<%
String staffName = (String) session.getAttribute("staffName");
if (staffName == null) {
  response.sendRedirect(request.getContextPath() + "/login.jsp");
  return;
}
Boolean editMode = (Boolean) request.getAttribute("editMode");
Integer applicationId = (Integer) request.getAttribute("applicationId");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 交通費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/businessTrip.js"></script>
  <style>
    .remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.2rem; color: #888; cursor: pointer; }
    .d-none { display: none; }
  </style>
  <script>
    const sessionPosition = "<%= session.getAttribute("position") %>";
    window.onload = function() {
      const diff = parseInt(document.querySelector("input[name='days[]']")?.value || "1");
      initializeAllowanceBlock(diff);
    };
  </script>
</head>
<body>
  <div class="page-container">
    <h2>交通費申請</h2>

    <!-- ✅ FORM chính duy nhất -->
    <form action="${pageContext.request.contextPath}/businessTripStep3" method="post" enctype="multipart/form-data">
    
      <!-- ✅ Đảm bảo có STEP = 3 -->
      <input type="hidden" name="step" value="3" id="stepHidden">
      <input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
      <% if (editMode != null && editMode) { %>
        <input type="hidden" name="applicationId" value="<%= applicationId %>">
      <% } %>
      <input type="hidden" name="startDateHidden" value="<%= request.getParameter("startDateHidden") %>">
      <input type="hidden" name="endDateHidden" value="<%= request.getParameter("endDateHidden") %>">
      <input type="hidden" name="totalDays" value="<%= request.getParameter("totalDays") %>">
      <input type="file" name="receiptStep3Files" multiple />
	  
      <div style="display: flex; flex-direction: column; gap: 10px" id="trans-container">
        <div class="form-section trans-block">
          <button type="button" class="remove-btn d-none" onclick="removeTransBlock(this)">×</button>

          <div class="form-group"><label>訪問先</label><input required type="text" name="transProject[]" placeholder="例:株式会社AAA"></div>
          <div class="form-group"><label>出発</label><input required type="text" name="departure[]" placeholder="例:東京"></div>
          <div class="form-group"><label>到着</label><input required type="text" name="arrival[]" placeholder="例:大阪"></div>
          <div class="form-group">
            <label>交通機関</label>
            <select required name="transport[]">
              <option value="">選択してください</option>
              <option value="新幹線">新幹線</option>
              <option value="電車">電車</option>
              <option value="タクシー">タクシー</option>
              <option value="飛行機">飛行機</option>
              <option value="自家用車">自家用車</option>
              <option value="レンタカー">レンタカー</option>
              <option value="他の">他の</option>
            </select>
          </div>

          <div class="form-group"><label>金額（税込）</label><input required type="number" name="fareAmount[]" step="100" onchange="calcFareTotal(this)"></div>
          <div class="form-group">
            <label>区分</label>
            <select required name="transTripType[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="片道">片道</option>
              <option value="往復">往復</option>
            </select>
          </div>
          <div class="form-group">
            <label>負担者</label>
            <select required name="burden[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="会社">会社</option>
              <option value="自己">自己</option>
            </select>
          </div>

          <div class="form-group"><label>合計</label><input type="number" name="expenseTotal[]" readonly></div>
          <div class="form-group"><label>摘要</label><textarea name="transMemo[]" placeholder="メモなど"></textarea></div>

          <div class="form-group">
            <label>領収書添付（交通費）</label>
            <input type="file" name="receiptStep3_0" multiple class="fileInput">
            <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
            <ul class="fileList"></ul>
          </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addTransBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/businessTripStep3Back'">戻る</button>
        <button type="submit">確認</button>
      </div>
    </form>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>