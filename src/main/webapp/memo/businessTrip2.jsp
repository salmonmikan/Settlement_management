<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.TripCategory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
List<TripCategory> categoryList = (List<TripCategory>) request.getAttribute("tripCategoryList");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 日当・宿泊費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
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
</head>
<body>
<div class="page-container">
  <h2>日当・宿泊費申請</h2>

  <form action="<%= request.getContextPath() %>/businessTripTest" method="post" enctype="multipart/form-data">
    <input type="hidden" name="step" value="2">

    <div id="allowance-container">
      <div class="form-section allowance-block" style="position: relative;">
        <button type="button" class="remove-btn" onclick="removeBlock(this)">×</button>

        <!-- 出張区分 -->
        <div class="form-group">
          <label>出張区分</label>
          <select name="tripType[]" onchange="updateRegionType(this)">
            <option value="">選択してください</option>
            <%
              List<String> tripTypes = new ArrayList<>();
              for (TripCategory c : categoryList) {
                  if (!tripTypes.contains(c.getTripType())) {
                      tripTypes.add(c.getTripType());
            %>
            <option value="<%= c.getTripType() %>"><%= c.getTripType() %></option>
            <%   }
              }
            %>
          </select>
        </div>

        <!-- 地域区分 -->
        <div class="form-group">
          <label>地域区分</label>
          <select name="regionType[]">
            <option value="">選択してください</option>
          </select>
        </div>

        <div class="form-group">
            <label>宿泊先</label>
            <input type="text" name="hotel[]" placeholder="例:APAホテル">
          </div>

          <div class="form-group">
            <label>負担者</label>
            <select name="burden[]" onchange="handleBurdenChange(this)" >
              <option value="">選択してください</option>
              <option value="会社">会社</option>
              <option value="自己">自己</option>
            </select>
          </div>

          <div class="form-group">
            <label>宿泊費</label>
            <input type="number" name="hotelFee[]" readonly>
          </div>

          <div class="form-group">
            <label>早朝深夜</label>
            <input type="number" name="dailyAllowance[]" readonly>
          </div>

          <div class="form-group">
            <label>日数</label>
            <input type="number" name="days[]" min="1" onchange="calcTotal(this)">
          </div>

          <div class="form-group">
            <label>合計</label>
            <input type="number" name="expenseTotal[]" readonly>
          </div>

          <div class="form-group">
            <label>摘要</label>
            <textarea name="memo[]" placeholder="メモなど"></textarea>
          </div>

          <div class="form-group">
			  <label>領収書添付（日当・宿泊費）</label>
			  <input type="file" name="receiptStep2_0[]" multiple class="fileInput">
			  <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
			  <ul class="fileList"></ul>
		  </div>

    <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addAllowanceBlock()">＋</button>
          </div>
        </div>
      </div>
    
    <div class="btn-section">
      <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/businessTripStep2BackTest'">戻る</button>
      <button type="submit">次へ</button>
    </div>
  </form>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<!-- Danh sách trip_category từ DB -->

</body>
</html>