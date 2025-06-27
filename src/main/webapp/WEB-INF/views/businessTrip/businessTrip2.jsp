<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String startDate = request.getParameter("startDateHidden");
  String endDate = request.getParameter("endDateHidden");

  if ((startDate == null || startDate.isEmpty()) || (endDate == null || endDate.isEmpty())) {
    bean.BusinessTripBean.BusinessTripBean tripBean = (bean.BusinessTripBean.BusinessTripBean) session.getAttribute("businessTripBean");
    if (tripBean != null && tripBean.getStep1Data() != null) {
      startDate = tripBean.getStep1Data().getStartDate();
      endDate = tripBean.getStep1Data().getEndDate();
    }
  }
  if (startDate == null) startDate = "";
  if (endDate == null) endDate = "";
%>
<% Boolean editMode = (Boolean) request.getAttribute("editMode"); %>
<% Integer applicationId = (Integer) request.getAttribute("applicationId"); %>

<!DOCTYPE html>
<html lang="ja">
<head>
<style>
    .remove-btn {
      position: absolute;
      top: 1px;
      right: 1px;
      background: none;
      border: none;
      font-size: 1.2rem;
      color: #888;
      cursor: pointer;
    }
  </style>
  <meta charset="UTF-8">
  <title>出張費申請 - 日当・宿泊費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/businessTrip.js"></script>
</head>
<script>
  const sessionPosition = "<%= session.getAttribute("position") %>";
  const start = new Date("<%= startDate %>");
  const end = new Date("<%= endDate %>");
  const diffDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;

  window.onload = function() {
    initializeAllowanceBlock(diffDays); // ✅ gọi đúng tại thời điểm window.onload
  };
</script>
<body>
  <%
	  bean.BusinessTripBean.BusinessTripBean tripBean = (bean.BusinessTripBean.BusinessTripBean) session.getAttribute("businessTripBean");
	  out.println("<!-- DEBUG: tripBean = " + tripBean + " -->");
	  if (tripBean != null && tripBean.getStep1Data() != null) {
	    out.println("<!-- DEBUG: startDate = " + tripBean.getStep1Data().getStartDate() + " -->");
	    out.println("<!-- DEBUG: endDate = " + tripBean.getStep1Data().getEndDate() + " -->");
	  }
	%>
  <div class="page-container">
    <h2>日当・宿泊費申請</h2>

    <form action="<%= request.getContextPath() %>/businessTripStep2" method="post" enctype="multipart/form-data">
      <input type="hidden" name="totalDays" value="<%= request.getParameter("totalDays") %>">
      <input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
      
	  <% if (editMode != null && editMode) { %>
	    <input type="hidden" name="applicationId" value="<%= applicationId %>">
	  <% } %>
	  
      <!-- ✅ step input đặt đúng vị trí -->
      <input type="hidden" name="step" value="2">
	  <input type="file" name="receiptStep2Files" multiple />
      <input type="hidden" name="endDateHidden" value="<%= endDate %>">
      <input type="hidden" name="startDateHidden" value="<%= startDate %>">

      <div style="display: flex; flex-direction: column; gap: 10px" id="allowance-container">
        <div class="form-section allowance-block" style="position: relative;" data-hotel-fee="">
          <button type="button" class="remove-btn" onclick="removeAllowanceBlock(this)">×</button>

          <div class="form-group">
			  <label>地域区分</label>
			  <select name="regionType[]" onchange="updateAllowanceAndHotel(this)">
			    <option value="">選択してください</option>
			    <option value="物価高水準地域" ${s2.regionType == '物価高水準地域' ? 'selected' : ''}>東京</option>
			    <option value="上記以外" ${s2.regionType == '上記以外' ? 'selected' : ''}>東京以外</option>
			    <option value="会社施設・縁故先宿泊" ${s2.regionType == '会社施設・縁故先宿泊' ? 'selected' : ''}>会社施設・縁故先宿泊</option>
			  </select>
			</div>
			
			<div class="form-group">
			  <label >出張区分</label>
			  <select name="tripType[]" required onchange="updateAllowanceAndHotel(this)">
			    <option value="" >選択してください</option>
			    <option value="短期出張" ${s2.tripType == '短期出張' ? 'selected' : ''}>短期出張</option>
			    <option value="長期出張" ${s2.tripType == '長期出張' ? 'selected' : ''}>長期出張</option>
			    <option value="研修出張" ${s2.tripType == '研修出張' ? 'selected' : ''}>研修出張</option>
			  </select>
			</div>

          <div class="form-group">
            <label>宿泊先</label>
            <input required type="text" name="hotel[]" placeholder="例:APAホテル">
          </div>

          <div class="form-group">
            <label>負担者</label>
            <select required name="burden[]" onchange="handleBurdenChange(this)" >
              <option value="">選択してください</option>
              <option value="会社">会社</option>
              <option value="自己">自己</option>
            </select>
          </div>

          <div class="form-group">
            <label>宿泊費</label>
            <input required type="number" name="hotelFee[]" readonly>
          </div>

          <div class="form-group">
            <label>日当</label>
            <input required type="number" name="dailyAllowance[]" readonly>
          </div>

          <div class="form-group">
            <label>日数</label>
            <input required type="number" name="days[]" min="1" onchange="calcTotal(this)">
          </div>

          <div class="form-group">
            <label>合計</label>
            <input required type="number" name="expenseTotal[]" readonly>
          </div>

          <div class="form-group">
            <label>摘要</label>
            <textarea name="memo[]" placeholder="メモなど"></textarea>
          </div>

          <div class="form-group">
			  <label>領収書添付（日当・宿泊費）</label>
			  <input type="file" name="receiptStep2_0" multiple class="fileInput">
			  <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
			  <ul class="fileList"></ul>
		  </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addAllowanceBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/businessTripStep2Back'">戻る</button>
        <button type="submit">次へ</button>
      </div>
    </form>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
  <% for (int i = 0; i < 10; i++) { %>
  <%
    String fieldName = "receiptStep2_" + i + "[]";
    out.println("<!-- DEBUG: file input field: " + fieldName + " -->");
  %>
<% } %>

 
</body>
</html>