<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 
  File này hiển thị toàn bộ nội dung của đơn "Công tác phí" (出張費)
  với cấu trúc hiển thị mới, rõ ràng và đồng nhất.
--%>

<style>
  /* Style chung cho trang confirm */
  .confirm-table { width: 100%; border-collapse: collapse; }
  .confirm-table th, .confirm-table td { border: 1px solid #ddd; padding: 10px; text-align: left; vertical-align: top; }
  .confirm-table th { background-color: #f8f9fa; font-weight: bold; width: 150px; }
  
  /* Khung lớn cho từng phần (Thông tin cơ bản, Phụ cấp, Chi phí đi lại) */
  .confirm-section { 
    margin-bottom: 20px; 
    padding: 20px; 
    border: 1px solid #e0e0e0; 
    border-radius: 8px; 
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  }
  .confirm-section h3 { 
    margin-top: 0; 
    padding-bottom: 10px;
    border-bottom: 2px solid var(--primary-color); 
    font-size: 1.2em;
    color: var(--primary-color);
  }

  /* --- STYLE MỚI CHO TỪNG BLOCK CHI TIẾT --- */
  .sub-section {
    border: 1px solid #f0f0f0;
    border-radius: 5px;
    padding: 15px;
    margin-top: 15px;
    background-color: #fafcfe;
  }
  .sub-section h4 {
    margin-top: 0;
    font-size: 1em;
    font-weight: bold;
    color: #333;
  }
  /* ------------------------------------------- */

  .detail-extra-info { margin-top: 15px; }
  .memo-block { padding: 10px; background-color: #f0f0f0; border-radius: 4px; white-space: pre-wrap; margin-top: 5px; }
  
  .receipt-list { list-style-type: none; padding-left: 0; margin-top: 5px; }
  .receipt-list li { display: flex; align-items: center; margin-bottom: 5px; }
  .receipt-list li::before { content: '📎'; margin-right: 8px; font-size: 1.2em; }
  
  .confirm-page-total { 
    margin-top: 20px; text-align: right; background-color: #e9f5ff; 
    padding: 12px 18px; font-weight: bold; font-size: 1.25em; border-radius: 5px;
  }
</style>

<div class="page-container" style="display: flex; flex-direction: column; gap: 15px;">

  <%-- ======================================================= --%>
  <%-- PHẦN 1: THÔNG TIN CƠ BẢN (STEP 1) - GIỮ NGUYÊN GIAO DIỆN --%>
  <%-- ======================================================= --%>
  <div class="confirm-section">
    <h3>基本情報</h3>
    <table class="confirm-table">
      <tr><th>出張期間</th><td>${trip.step1Data.startDate} ～ ${trip.step1Data.endDate}</td></tr>
      <tr><th>PJコード</th><td>${trip.step1Data.projectCode}</td></tr>
      <tr><th>出張報告</th><td style="white-space: pre-wrap;">${trip.step1Data.tripReport}</td></tr>
      <%-- <tr><th>合計日数</th><td>${trip.step1Data.totalDays} 日間</td></tr> --%>
    </table>
  </div>

  <%-- =================================================================== --%>
  <%-- PHẦN 2: CHI TIẾT PHỤ CẤP & CHỖ Ở (STEP 2) - ĐÃ TÁI CẤU TRÚC --%>
  <%-- =================================================================== --%>
  <div class="confirm-section">
    <h3>日当・宿泊費</h3>
    <c:if test="${empty trip.step2Details}"><p>登録なし</p></c:if>
    
    <%-- Bắt đầu vòng lặp, mỗi lần lặp tạo một "thẻ" chi tiết --%>
    <c:forEach var="detail" items="${trip.step2Details}" varStatus="loop">
      <div class="sub-section"> <%-- Khung con cho mỗi chi tiết --%>
        <h4>明細 ${loop.count}</h4>
        <table class="confirm-table">
          <tr><th>地域区分</th><td>${detail.regionType}</td></tr>
          <tr><th>出張区分</th><td>${detail.tripType}</td></tr>
          <tr><th>負担者</th><td>${detail.burden}</td></tr>
          <tr><th>宿泊先</th><td>${detail.hotel}</td></tr>
          <tr><th>日当</th><td><fmt:formatNumber value="${detail.dailyAllowance}" type="number" />円</td></tr>
          <tr><th>宿泊費</th><td><fmt:formatNumber value="${detail.hotelFee}" type="number" />円</td></tr>
          <tr><th>日数</th><td>${detail.days}</td></tr>
          <tr><th>合計</th><td><fmt:formatNumber value="${detail.expenseTotal}" type="number" />円</td></tr>
        </table>
        <div class="detail-extra-info">
          <c:if test="${not empty detail.memo}">
            <div><strong>摘要:</strong></div>
            <div class="memo-block">${detail.memo}</div>
          </c:if>
          <c:if test="${not empty detail.temporaryFiles}">
            <div style="margin-top: 10px;"><strong>領収書ファイル:</strong></div>
            <ul class="receipt-list">
              <c:forEach var="file" items="${detail.temporaryFiles}">
                <li><a href="${pageContext.request.contextPath}${file.temporaryPath}" target="_blank">${file.originalFileName}</a></li>
              </c:forEach>
            </ul>
          </c:if>
        </div>
      </div>
    </c:forEach>
  </div>

  <%-- =================================================================== --%>
  <%-- PHẦN 3: CHI TIẾT CHI PHÍ ĐI LẠI (STEP 3) - ĐÃ TÁI CẤU TRÚC --%>
  <%-- =================================================================== --%>
  <div class="confirm-section">
    <h3>交通費</h3>
    <c:if test="${empty trip.step3Details}"><p>登録なし</p></c:if>

    <c:forEach var="detail" items="${trip.step3Details}" varStatus="loop">
      <div class="sub-section">
        <h4>精算明細 ${loop.count}</h4>
        <table class="confirm-table">
          <tr><th>訪問先</th><td>${detail.transProject}</td></tr>
          <tr><th>出発</th><td>${detail.departure}</td></tr>
          <tr><th>到着</th><td>${detail.arrival}</td></tr>
          <tr><th>交通機関</th><td>${detail.transport}</td></tr>
          <tr><th>金額</th><td><fmt:formatNumber value="${detail.fareAmount}" type="number" />円</td></tr>
          <tr><th>区分</th><td>${detail.transTripType}</td></tr>
          <tr><th>負担者</th><td>${detail.transBurden}</td></tr>
          <tr><th>合計</th><td><fmt:formatNumber value="${detail.transExpenseTotal}" type="number" />円</td></tr>
        </table>
         <div class="detail-extra-info">
          <c:if test="${not empty detail.transMemo}">
            <div><strong>摘要:</strong></div>
            <div class="memo-block">${detail.transMemo}</div>
          </c:if>
          <c:if test="${not empty detail.temporaryFiles}">
            <div style="margin-top: 10px;"><strong>領収書ファイル:</strong></div>
            <ul class="receipt-list">
              <c:forEach var="file" items="${detail.temporaryFiles}">
                <li><a href="${pageContext.request.contextPath}${file.temporaryPath}" target="_blank">${file.originalFileName}</a></li>
              </c:forEach>
            </ul>
          </c:if>
        </div>
      </div>
    </c:forEach>
  </div>

  <%-- PHẦN 4: TỔNG SỐ TIỀN TOÀN BỘ ĐƠN - GIỮ NGUYÊN --%>
  <div class="confirm-page-total">
    総合計金額: <fmt:formatNumber value="${trip.totalAmount}" type="number" /> 円
  </div>
</div>