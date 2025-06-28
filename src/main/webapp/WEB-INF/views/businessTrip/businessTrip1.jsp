<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - Step 1</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <script src="${pageContext.request.contextPath}/static/js/businessTrip.js"></script>
</head>
<body>
  <div class="page-container">
    <h2>出張情報</h2>
    
    <%-- 
      GHI CHÚ QUAN TRỌNG:
      - form action sẽ trỏ đến Servlet xử lý Step 1, ví dụ /businessTripStep1
      - Dữ liệu sẽ được đọc từ đối tượng 'trip' trong session (hoặc request).
      - 'trip' là tên gợi ý cho đối tượng BusinessTripBean của bạn.
    --%>
    <form action="${pageContext.request.contextPath}/businessTripStep1" method="post">
      
      <%-- 
        THAY THẾ: Loại bỏ các hidden input không cần thiết như totalDays, startDateHidden, endDateHidden.
        Dữ liệu này sẽ được quản lý hoàn toàn trong đối tượng BusinessTripBean trong session.
      --%>
      
      <div class="form-section">
        <div class="form-group">
          <label>出張期間</label>
          <div style="display: flex; gap: 1rem;">
            <%-- Dữ liệu được điền lại từ bean trong session/request --%>
            <input type="date" name="startDate" id="startDate" value="${trip.step1Data.startDate}" required>
            <span>～</span>
            <input type="date" name="endDate" id="endDate" value="${trip.step1Data.endDate}" required>
          </div>
        </div>

        <div class="form-group">
          <label>PJコード</label>
          <select name="projectCode" required>
            <option value="">選択してください</option>
            <c:forEach var="p" items="${projectList}">
              <%-- Logic chọn 'selected' được đơn giản hóa --%>
              <option value="${p.id}" ${p.id == trip.step1Data.projectCode ? 'selected' : ''}>
                ${p.id}：${p.name}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="form-group">
          <label>出張報告</label>
          <textarea name="tripReport" class="hokoku-text" placeholder="業務内容や目的を入力してください">${trip.step1Data.tripReport}</textarea>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/home'">戻る</button>
        <button type="submit">次へ</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
  <script>
      window.addEventListener('load', function() {
        // Có thể gọi hàm initializeTransBlock nếu bạn có logic khởi tạo cho Step 3
        // initializeTransBlock(); 
      });
  </script>
</body>
</html>