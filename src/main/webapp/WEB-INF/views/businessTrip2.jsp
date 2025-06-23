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
<%@ page import="java.util.List" %>
<%@ page import="model.Receipt" %>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 日当・宿泊費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
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

    <form action="<%= request.getContextPath() %>/businessTrip" method="post" enctype="multipart/form-data">
      <input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
	  <% if (editMode != null && editMode) { %>
	    <input type="hidden" name="applicationId" value="<%= applicationId %>">
	  <% } %>
	  
      <!-- ✅ step input đặt đúng vị trí -->
      <input type="hidden" name="step" value="2">
      <input type="hidden" name="endDateHidden" value="<%= endDate %>">
      <input type="hidden" name="startDateHidden" value="<%= startDate %>">

      <div style="display: flex; flex-direction: column; gap: 10px" id="allowance-container">
        <div class="form-section allowance-block" style="position: relative;" data-hotel-fee="">
          <button type="button" class="remove-btn" onclick="removeBlock(this)">×</button>

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
			  <label>出張区分</label>
			  <select name="tripType[]" onchange="updateAllowanceAndHotel(this)">
			    <option value="">選択してください</option>
			    <option value="短期出張" ${s2.tripType == '短期出張' ? 'selected' : ''}>短期出張</option>
			    <option value="長期出張" ${s2.tripType == '長期出張' ? 'selected' : ''}>長期出張</option>
			    <option value="研修出張" ${s2.tripType == '研修出張' ? 'selected' : ''}>研修出張</option>
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
            <label>日当</label>
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
		  <%
			  List<model.Receipt> receipts = (List<model.Receipt>) request.getAttribute("receipts");
			  Boolean isDetailMode = (Boolean) request.getAttribute("isDetailMode");
			  isDetailMode = (isDetailMode != null) ? isDetailMode : false;
			  if (isDetailMode && receipts != null && !receipts.isEmpty()) {
			%>
			  <div class="form-group">
			    <label>添付ファイル（確認用）</label>
			    <ul>
			      <% for (model.Receipt r : receipts) { %>
			        <li>
			          <a href="<%= request.getContextPath() + "/uploads/" + r.getStoredFilePath() %>" target="_blank">
			            <%= r.getOriginalFileName() %>
			          </a>
			        </li>
			      <% } %>
			    </ul>
			  </div>
			<% } %>
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

  <script>
  const startDate = new Date("<%= startDate != null ? startDate : "" %>");
  const endDate = new Date("<%= endDate != null ? endDate : "" %>");
  const diffDays = Math.floor((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
    

    function updateAllowanceAndHotel(elem) {
      const block = elem.closest(".allowance-block");
      const tripType = block.querySelector("select[name='tripType[]']").value;
      const region = block.querySelector("select[name='regionType[]']").value;
      const rankType = '<%= session.getAttribute("position") %>';
      const isManager = !(rankType.includes("一般社員"));

      let hotelFee = 0, dailyFee = 0;
      if (tripType === "短期出張") {
        if (region === "物価高水準地域") {
          hotelFee = 10000;
          dailyFee = isManager ? 4000 : 2200;
        } else if (region === "上記以外") {
          hotelFee = 8000;
          dailyFee = isManager ? 4000 : 2200;
        } else if (region === "会社施設・縁故先宿泊") {
          hotelFee = 0;
          dailyFee = 5000;
        }
      } else if (tripType === "長期出張") {
        hotelFee = 0;
        dailyFee = (region === "会社施設・縁故先宿泊") ? 3500 : 1000;
      } else if (tripType === "研修出張") {
        hotelFee = 0;
        dailyFee = 1000;
      }

      block.setAttribute('data-hotel-fee', hotelFee);
      block.querySelector("input[name='dailyAllowance[]']").value = dailyFee;
      handleBurdenChange(block.querySelector("select[name='burden[]']"));
    }

    function calcTotal(elem) {
      const block = elem.closest(".allowance-block");
      const burden = block.querySelector("select[name='burden[]']").value;
      const days = parseInt(block.querySelector("input[name='days[]']").value || 0);
      const hotelFee = parseInt(block.querySelector("input[name='hotelFee[]']").value || 0);
      const daily = parseInt(block.querySelector("input[name='dailyAllowance[]']").value || 0);
      const total = (burden === "自己") ? (hotelFee + daily) * days : daily * days;
      block.querySelector("input[name='expenseTotal[]']").value = total;
    }

    function handleBurdenChange(select) {
      const block = select.closest(".allowance-block");
      const burden = select.value;
      const hotelFeeInput = block.querySelector("input[name='hotelFee[]']");
      const calculated = parseInt(block.getAttribute('data-hotel-fee') || 0);

      if (burden === "自己") {
        hotelFeeInput.value = calculated;
      } else {
        hotelFeeInput.value = "";
      }

      calcTotal(select);
    }

    function addAllowanceBlock() {
    	  const container = document.getElementById("allowance-container");
    	  const blocks = document.querySelectorAll(".allowance-block");
    	  const index = blocks.length;

    	  const template = document.querySelector(".allowance-block");
    	  const clone = template.cloneNode(true);

    	  clone.querySelectorAll("input, textarea").forEach(el => el.value = "");
    	  clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

    	  const fileInput = clone.querySelector(".fileInput");
    	  fileInput.name = `receiptStep2_${index}[]`;
    	  fileInput.setAttribute("multiple", true);

    	  container.appendChild(clone);

    	  fileInput.addEventListener("change", function(e) {
    	    const fileList = clone.querySelector(".fileList");
    	    fileList.innerHTML = "";
    	    Array.from(e.target.files).forEach(file => {
    	      const li = document.createElement("li");
    	      li.textContent = file.name;
    	      fileList.appendChild(li);
    	    });
    	  });

    	  // ✅ Gọi sau khi gán xong để tránh lỗi
    	  updateAllowanceAndHotel(clone.querySelector("select[name='regionType[]']"));
    	}

    function removeBlock(btn) {
      const blocks = document.querySelectorAll(".allowance-block");
      if (blocks.length > 1) {
        btn.closest(".allowance-block").remove();
      } else {
        alert("最低1つの明細が必要です");
      }
    }

    window.onload = function() {
    	  const firstBlock = document.querySelector(".allowance-block");
    	  if (firstBlock) {
    	    const daysInput = firstBlock.querySelector("input[name='days[]']");
    	    if (daysInput.value === "" || parseInt(daysInput.value) <= 1) {
    	      daysInput.value = diffDays;
    	    }
    	    console.log("auto-set daysInput =", daysInput);
    	    console.log("before value =", daysInput.value);
    	    updateAllowanceAndHotel(firstBlock.querySelector("select[name='regionType[]']"));

    	    // ✅ Gán sự kiện change cho input file đầu tiên
    	    const fileInput = firstBlock.querySelector("input[type='file']");
    	    const fileList = firstBlock.querySelector(".fileList");
    	    fileInput.addEventListener("change", function(e) {
    	      fileList.innerHTML = "";
    	      Array.from(e.target.files).forEach(file => {
    	        const li = document.createElement("li");
    	        li.textContent = file.name;
    	        fileList.appendChild(li);
    	      });
    	    });
    	  }
    	};
  </script>
</body>
</html>