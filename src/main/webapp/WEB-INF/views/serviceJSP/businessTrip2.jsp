<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>出張費申請 - 日当・宿泊費明細</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
<script src="${pageContext.request.contextPath}/static/js/businessTrip.js"></script>
<style>
.remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.2rem; color: #888; cursor: pointer; }
.fileList a { color: #0056b3; text-decoration: underline; cursor: pointer; }
.file-delete-btn{    
    background: none;
    padding:4px;
    color: red;
    border: none;
    border-radius: 4px;
    text-decoration: none;
    font-size: 1.2rem;
    cursor: pointer;
    transition: background 0.3s;
    height: 36.8px;}
    
    .file-delete-btn:hover {
      background: none;
	  transform: scale(1.2);
	  opacity: 0.8;
	}
</style>
</head>
<body>
<div class="page-container">
<h2>日当・宿泊費申請</h2>
<form action="${pageContext.request.contextPath}/businessTripStep2" method="post" enctype="multipart/form-data">
<input type="hidden" id="filesToDelete" name="filesToDelete" value="">
<div style="display: flex; flex-direction: column; gap: 10px" id="allowance-container">
<c:forEach var="detail" items="${trip.step2Details}" varStatus="loop">
<div class="form-section allowance-block" style="position: relative;">
<button type="button" class="remove-btn" onclick="removeAllowanceBlock(this)">×</button>
<div class="form-group"><label>宿泊先</label><input required type="text" name="hotel[]" placeholder="例:APAホテル" value="${detail.hotel}"></div>
<div class="form-group"><label>地域区分</label><select name="regionType[]" onchange="calculateHotelFee(this)"><option value="">選択してください</option><option value="東京" ${detail.regionType == '東京' ? 'selected' : ''}>東京</option><option value="東京以外" ${detail.regionType == '東京以外' ? 'selected' : ''}>東京以外</option><option value="会社施設・縁故先宿泊" ${detail.regionType == '会社施設・縁故先宿泊' ? 'selected' : ''}>会社施設・縁故先宿泊</option></select></div>
<div class="form-group"><label>出張区分</label><select name="tripType[]" required onchange="calculateHotelFee(this)"><option value="">選択してください</option><option value="短期出張" ${detail.tripType == '短期出張' ? 'selected' : ''}>短期出張</option><option value="長期出張" ${detail.tripType == '長期出張' ? 'selected' : ''}>長期出張</option><option value="研修出張" ${detail.tripType == '研修出張' ? 'selected' : ''}>研修出張</option></select></div>
<div class="form-group"><label>負担者</label><select required name="burden[]" onchange="calculateHotelFee(this)"><option value="">選択してください</option><option value="会社" ${detail.burden == '会社' ? 'selected' : ''}>会社</option><option value="自己" ${detail.burden == '自己' ? 'selected' : ''}>自己</option></select></div>
<div class="form-group"><label>宿泊費</label><input required type="number" name="hotelFee[]" readonly value="${detail.hotelFee}"></div>
<div class="form-group"><label>日当</label><input required type="number" name="dailyAllowance[]" readonly value="${detail.dailyAllowance}"></div>
<div class="form-group"><label>日数</label><input required type="number" name="days[]" min="1" value="${detail.days}"></div>
<div class="form-group"><label>合計</label><input required type="number" name="expenseTotal[]" readonly value="${detail.expenseTotal}"></div>
<div class="form-group"><label>摘要</label><textarea name="memo[]" placeholder="メモなど">${detail.memo}</textarea></div>
<div class="form-group">
<label>領収書添付（日当・宿泊費）</label>
<input type="file" name="receipt_allowance_${loop.index}" multiple class="fileInput" onchange="handleFileSelection(this)">

<ul class="fileList">
<%-- <c:forEach var="file" items="${detail.temporaryFiles}">
<li>
<a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">
${file.originalFileName}
</a>
</li>
</c:forEach> --%>
<c:forEach var="file" items="${detail.temporaryFiles}">
  <li data-file-type="existing" data-unique-name="${file.uniqueStoredName}">
    <a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">
      ${file.originalFileName}
    </a>
    <button type="button" class="file-delete-btn" onclick="deleteExistingFile(this)">×</button>
  </li>
</c:forEach>
</ul>
</div>
</div>
</c:forEach>
</div>
<%-- ★★★ KHÔI PHỤC NÚT ADD BLOCK ★★★ --%>
<div style="text-align: center; margin-top: 10px;">
<button type="button" class="plus-btn" onclick="addAllowanceBlock()">＋</button>
</div>

<%-- ★★★ KHỐI NÚT BẤM ĐÚNG CHUẨN ★★★ --%>
<div class="btn-section">
<button type="submit" name="action" value="go_back" class="back-btn" formnovalidate>戻る</button>
<button type="submit" name="action" value="go_next" class="next-btn">次へ</button>
</div>
</form>
</div>
<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<script>
const startDateStr = "${trip.step1Data.startDate}";
const endDateStr = "${trip.step1Data.endDate}";
const positionId = "${sessionScope.position_id}";
let diffDays = 1;
if (startDateStr && endDateStr) {
const start = new Date(startDateStr);
const end = new Date(endDateStr);
if (end >= start) {
diffDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
}
}
window.addEventListener('load', function() { initializeStep2(diffDays, positionId); });
</script>
</body>
</html>

