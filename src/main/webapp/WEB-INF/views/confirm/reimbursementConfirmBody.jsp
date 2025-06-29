<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
  .confirm-table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }
  .confirm-table th, .confirm-table td { border: 1px solid #ddd; padding: 8px; text-align: left; }
  .confirm-table th { background-color: #f8f9fa; font-weight: bold; width: 150px; }
  .confirm-section { margin-bottom: 20px; padding: 15px; border: 1px solid #eee; border-radius: 5px; }
  .report-block { padding: 6px 12px; background-color: #f9f9f9; border-left: 4px solid #ccc; font-size: 0.95em; margin-top: 4px; white-space: pre-wrap; }
  .receipt-list { list-style-type: '📎'; padding-left: 20px; }
  .confirm-page-total { margin-top: 10px; text-align: right; background-color: #e0f7fa; padding: 10px 15px; font-weight: bold; font-size: 1.2em; }
</style>

<div class="page-container" style="display: flex; flex-direction: column; gap: 15px;">

  <div class="confirm-section">
    <h3>精算明細</h3>
    <c:if test="${not empty reimbursementApp.details}">
      <table class="confirm-table">
        <tr>
          <th>日付</th>
          <th>訪問先</th>
          <th>PJコード</th>
          <th>勘定科目</th>
          <th>金額</th>
        </tr>
        <c:forEach var="detail" items="${reimbursementApp.details}">
          <tr>
            <td>${detail.date}</td>
            <td>${detail.destinations}</td>
            <td>${detail.projectCode}</td>
            <td>${detail.accountingItem}</td>
            <td><fmt:formatNumber value="${detail.amount}" type="number" />円</td>
          </tr>
          <c:if test="${not empty detail.report}">
            <tr><td colspan="5"><div class="report-block"><b>摘要:</b> ${detail.report}</div></td></tr>
          </c:if>
        </c:forEach>
      </table>

      <h4>領収書ファイル:</h4>
      <ul class="receipt-list">
        <c:forEach var="detail" items="${reimbursementApp.details}">
          <c:forEach var="file" items="${detail.temporaryFiles}">
            <li>
              <%-- ★★★ SỬA LỖI 3: Hiển thị link xem file đúng cách ★★★ --%>
              <a href="${pageContext.request.contextPath}${file.temporaryPath}" target="_blank">${file.originalFileName}</a>
            </li>
          </c:forEach>
        </c:forEach>
      </ul>
    </c:if>
    <c:if test="${empty reimbursementApp.details}"><p>登録なし</p></c:if>
  </div>

  <%-- ★★★ SỬA LỖI 4: Lấy tổng tiền trực tiếp từ Bean đã được tính sẵn ★★★ --%>
  <div class="confirm-page-total">
    総合計金額: <fmt:formatNumber value="${reimbursementApp.totalAmount}" type="number" /> 円
  </div>
</div>