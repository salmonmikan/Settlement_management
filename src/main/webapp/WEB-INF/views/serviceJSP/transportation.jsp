<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>交通費申請</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="${pageContext.request.contextPath}/static/js/transpotation.js"></script>
  <style>
    /* Các style quan trọng được giữ lại */
    .remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.5rem; color: #888; cursor: pointer; font-weight: bold; }
    .fileList { list-style-type: none; padding-left: 0; margin-top: 8px; }
    .file-delete-btn { background: none; border: none; color: #d9534f; cursor: pointer; font-weight: bold; font-size: 1.2rem; padding: 0 5px; }
    #transportation-template { display: none; }
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
    <h2>交通費申請</h2>
    
    <%-- TODO: Đảm bảo Servlet cung cấp các thuộc tính này nếu có --%>
    <c:if test="${not empty successCount && successCount > 0}">
        <p style="color: red;text-align:center">${successCount} 件の申請を登録しました。</p>
    </c:if>

    <%-- Action URL cần được xác nhận lại từ TransportationServlet --%>
    <form action="<%= request.getContextPath() %>/transportationRequest" method="post" enctype="multipart/form-data">
        <input type="hidden" id="filesToDelete" name="filesToDelete" value="">
        <%-- Các hidden input cho chế độ edit --%>
        <c:if test="${not empty editMode && editMode}">
            <input type="hidden" name="editMode" value="true">
            <input type="hidden" name="applicationId" value="${applicationId}">
        </c:if>

      <div style="display: flex; flex-direction: column; gap: 10px" id="transportation-container">
        
        
        <c:forEach var="detail" items="${transportationApp.details}" varStatus="loop">
            <div class="form-section transportation-block" style="position: relative;">
              <button type="button" class="remove-btn" onclick="removeTransportationBlock(this)">×</button>
              
              <div class="form-group">
                  <label>PJコード</label>
                  <select name="projectCode[]" required>
                    <option value="">選択してください</option>
                    <c:forEach var="p" items="${projectList}">
                      <option value="${p.id}" ${p.id == detail.projectCode ? 'selected' : ''}>${p.id}：${p.name}</option>
                    </c:forEach>
                  </select>
              </div>
              <div class="form-group">
                <label>訪問先</label>
                <input type="text" name="destination[]" placeholder="例: ABC株式会社" value="${detail.destination}" required>
              </div>
              <div class="form-group">
                  <label>訪問月・日</label>
                  <input type="date" name="date[]" value="${detail.date}" required>
              </div>
              <div class="form-group"><label>出発</label><input type="text" name="departure[]" placeholder="例:東京駅" value="${detail.departure}"></div>
              <div class="form-group"><label>到着</label><input type="text" name="arrival[]" placeholder="例:大阪駅" value="${detail.arrival}"></div>
              <div class="form-group">
                <label>交通機関</label>
                <select name="transport[]">
                  <option value="">選択してください</option>
                  <option value="新幹線" ${'新幹線' == detail.transport ? 'selected' : ''}>新幹線</option>
                  <option value="電車" ${'電車' == detail.transport ? 'selected' : ''}>電車</option>
                  <option value="タクシー" ${'タクシー' == detail.transport ? 'selected' : ''}>タクシー</option>
                  <option value="飛行機" ${'飛行機' == detail.transport ? 'selected' : ''}>飛行機</option>
                  <option value="自家用車" ${'自家用車' == detail.transport ? 'selected' : ''}>自家用車</option>
                  <option value="レンタカー" ${'レンタカー' == detail.transport ? 'selected' : ''}>レンタカー</option>
                  <option value="他の" ${'他の' == detail.transport ? 'selected' : ''}>他の</option>
                </select>
              </div>
              <div class="form-group"><label>金額（税込）</label><input type="number" name="fareAmount[]" value="${detail.fareAmount}"></div>
              <div class="form-group"><label>区分</label>
                <select name="transTripType[]">
                  <option value="">選択してください</option>
                  <option value="片道" ${'片道' == detail.transTripType ? 'selected' : ''}>片道</option>
                  <option value="往復" ${'往復' == detail.transTripType ? 'selected' : ''}>往復</option>
                </select>
              </div>
              <div class="form-group"><label>負担者</label>
                <select name="burden[]">
                  <option value="">選択してください</option>
                  <option value="会社" ${'会社' == detail.burden ? 'selected' : ''}>会社</option>
                  <option value="自己" ${'自己' == detail.burden ? 'selected' : ''}>自己</option>
                </select>
              </div>
              <div class="form-group"><label>合計</label><input type="number" name="expenseTotal[]" value="${detail.expenseTotal}" readonly></div>
              <div class="form-group"><label>摘要</label><textarea name="transMemo[]" placeholder="メモなど">${detail.transMemo}</textarea></div>
              <div class="form-group">
                <label>領収書添付（交通費）</label>
                <input type="file" name="receipt_transportation_${loop.index}" multiple class="fileInput" onchange="handleFileSelection(this)">
                <ul class="fileList">
                  <c:forEach var="file" items="${detail.temporaryFiles}">
                    <li data-file-type="existing" data-unique-name="${file.uniqueStoredName}">
                        <a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">${file.originalFileName}</a>
                        <button type="button" class="file-delete-btn" onclick="deleteExistingFile(this)">×</button>
                    </li>
                  </c:forEach>
                </ul>
              </div>
            </div>
        </c:forEach>
      </div>

      <div style="text-align: center; margin-top: 10px;">
        <button type="button" class="plus-btn" onclick="addTransportationBlock()">＋</button>
      </div>

      <div class="btn-section">
        <button type="button" class="back-btn" onclick="window.location.href='<%= request.getContextPath() %>/home'">戻る</button>
        <button type="submit" class="next-btn">確認</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
  
  <%-- ========================================================================================= --%>
  <%-- PHẦN 2: TEMPLATE HTML CHO JAVASCRIPT SỬ DỤNG --%>
  <%-- ========================================================================================= --%>
  <template id="transportation-template">
    <div class="form-section transportation-block" style="position: relative;">
        <button type="button" class="remove-btn" onclick="removeTransportationBlock(this)">×</button>
        
        <div class="form-group">
            <label>PJコード</label>
            <select name="projectCode[]" required>
                <option value="">選択してください</option>
                <c:forEach var="p" items="${projectList}">
                <option value="${p.id}">${p.id}：${p.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>訪問月・日</label>
            <input type="date" name="date[]" required>
        </div>
        <div class="form-group">
                <label>訪問先</label>
                <input type="text" name="destination[]" placeholder="例: ABC株式会社" value="${detail.destination}" required>
        </div>
        
        <div class="form-group"><label>出発</label><input type="text" name="departure[]" placeholder="例:東京駅"></div>
        <div class="form-group"><label>到着</label><input type="text" name="arrival[]" placeholder="例:大阪駅"></div>
        <div class="form-group">
            <label>交通機関</label>
            <select name="transport[]">
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
        <div class="form-group"><label>金額（税込）</label><input type="number" name="fareAmount[]"></div>
        <div class="form-group"><label>区分</label>
            <select name="transTripType[]">
                <option value="">選択してください</option>
                <option value="片道">片道</option>
                <option value="往復">往復</option>
            </select>
        </div>
        <div class="form-group"><label>負担者</label>
            <select name="burden[]">
                <option value="">選択してください</option>
                <option value="会社">会社</option>
                <option value="自己">自己</option>
            </select>
        </div>
        <div class="form-group"><label>合計</label><input type="number" name="expenseTotal[]" readonly></div>
        <div class="form-group"><label>摘要</label><textarea name="transMemo[]" placeholder="メモなど"></textarea></div>
        <div class="form-group">
            <label>領収書添付（交通費）</label>
            <input type="file" name="receipt_transportation_" multiple class="fileInput" onchange="handleFileSelection(this)">
            <ul class="fileList"></ul>
        </div>
    </div>
  </template>

 
</body>
</html>