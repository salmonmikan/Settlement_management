<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 
  File này giờ đây cực kỳ "sạch sẽ".
  Nó chỉ nhận dữ liệu từ Servlet và hiển thị, không tính toán gì cả.
  Chúng ta giả định Servlet đã đặt một đối tượng BusinessTripBean vào request với tên là "trip".
--%>
<style>
  .confirm-table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }
  .confirm-table th, .confirm-table td { border: 1px solid #ddd; padding: 8px; text-align: left; }
  .confirm-table th { background-color: #f8f9fa; font-weight: bold; width: 150px; }
  .confirm-section { margin-bottom: 20px; padding: 15px; border: 1px solid #eee; border-radius: 5px; }
  .confirm-section h3 { color: var(--primary-color); margin-top: 0; border-bottom: 2px solid var(--primary-color); padding-bottom: 5px; }
  .memo-block { padding: 6px 12px; background-color: #f9f9f9; border-left: 4px solid #ccc; font-size: 0.95em; margin-top: 4px; white-space: pre-wrap; }
  .receipt-list { list-style-type: '📎'; padding-left: 20px; }
  .confirm-page-total { margin-top: 10px; text-align: right; background-color: #e0f7fa; padding: 10px 15px; font-weight: bold; font-size: 1.2em; }
</style>

<div class="page-container" style="display: flex; flex-direction: column; gap: 15px;">

  <div class="confirm-section">
    <h3>基本情報</h3>
    <table class="confirm-table">
      <tr><th>出張期間</th><td>${trip.step1Data.startDate} ～ ${trip.step1Data.endDate}</td></tr>
      <tr><th>PJコード</th><td>${trip.step1Data.projectCode}</td></tr>
      <tr><th>出張報告</th><td style="white-space: pre-wrap;">${trip.step1Data.tripReport}</td></tr>
      <tr><th>合計日数</th><td>${trip.step1Data.totalDays} 日間</td></tr>
    </table>
  </div>

  <div class="confirm-section">
    <h3>日当・宿泊費</h3>
    <c:if test="${not empty trip.step2Details}">
      <table class="confirm-table">
        <tr><th>地域区分</th><th>出張区分</th><th>負担者</th><th>宿泊先</th><th>日当</th><th>宿泊費</th><th>日数</th><th>合計</th></tr>
        <c:forEach var="detail" items="${trip.step2Details}">
          <tr>
            <td>${detail.regionType}</td>
            <td>${detail.tripType}</td>
            <td>${detail.burden}</td>
            <td>${detail.hotel}</td>
            <td><fmt:formatNumber value="${detail.dailyAllowance}" type="number" />円</td>
            <td><fmt:formatNumber value="${detail.hotelFee}" type="number" />円</td>
            <td>${detail.days}</td>
            <td><fmt:formatNumber value="${detail.expenseTotal}" type="number" />円</td>
          </tr>
          <c:if test="${not empty detail.memo}">
            <tr><td colspan="8"><div class="memo-block"><b>摘要:</b> ${detail.memo}</div></td></tr>
          </c:if>
        </c:forEach>
      </table>

      <%-- Hiển thị các file đã đính kèm --%>
      <h4>日当・宿泊費 領収書ファイル:</h4>
      <ul class="receipt-list">
		  <c:forEach var="detail" items="${trip.step2Details}">
		    <c:forEach var="file" items="${detail.temporaryFiles}">
		      <li>
		        <a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">
		          ${file.originalFileName}
		        </a>
		      </li>
		    </c:forEach>
		  </c:forEach>
		</ul>
    </c:if>
    <c:if test="${empty trip.step2Details}"><p>登録なし</p></c:if>
  </div>

  <div class="confirm-section">
    <h3>交通費</h3>
    <c:if test="${not empty trip.step3Details}">
      <table class="confirm-table">
        <tr><th>訪問先</th><th>出発</th><th>到着</th><th>交通機関</th><th>金額</th><th>区分</th><th>負担者</th><th>合計</th></tr>
        <c:forEach var="detail" items="${trip.step3Details}">
          <tr>
            <td>${detail.transProject}</td>
            <td>${detail.departure}</td>
            <td>${detail.arrival}</td>
            <td>${detail.transport}</td>
            <td><fmt:formatNumber value="${detail.fareAmount}" type="number" />円</td>
            <td>${detail.transTripType}</td>
            <td>${detail.transBurden}</td>
            <td><fmt:formatNumber value="${detail.transExpenseTotal}" type="number" />円</td>
          </tr>
          <c:if test="${not empty detail.transMemo}">
            <tr><td colspan="8"><div class="memo-block"><b>摘要:</b> ${detail.transMemo}</div></td></tr>
          </c:if>
        </c:forEach>
      </table>
      
      <%-- Hiển thị các file đã đính kèm --%>
      <h4>交通費 領収書ファイル:</h4>
      <ul class="receipt-list">
		  <c:forEach var="detail" items="${trip.step3Details}">
		    <c:forEach var="file" items="${detail.temporaryFiles}">
		      <li>
		        <a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">
		          ${file.originalFileName}
		        </a>
		      </li>
		    </c:forEach>
		  </c:forEach>
		</ul>
    </c:if>
    <c:if test="${empty trip.step3Details}"><p>登録なし</p></c:if>
  </div>

  <div class="confirm-page-total">
    総合計金額: <fmt:formatNumber value="${trip.totalAmount}" type="number" /> 円
  </div>
</div>