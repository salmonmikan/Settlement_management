<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
  String mode = (String) request.getAttribute("mode");
  if (mode == null) mode = "confirm";

  String actionUrl = "";
  if ("出張費".equals(type)) {
    actionUrl = request.getContextPath() + "/submitBusinessTrip";
  } else if ("交通費".equals(type)) {
    actionUrl = request.getContextPath() + "/submitTransport";
  } else if ("立替金".equals(type)) {
    actionUrl = request.getContextPath() + "/submitReimbursement";
  }

  Boolean isDetailMode = (Boolean) request.getAttribute("isDetailMode");
  isDetailMode = (isDetailMode != null) ? isDetailMode : false;

  Object appIdObj = request.getAttribute("application_id");
  Boolean isApprovalScreen = (Boolean) request.getAttribute("isApprovalScreen");
  isApprovalScreen = (isApprovalScreen != null) ? isApprovalScreen : false;
%>

<div class="btn-section">
  <form action="<%= request.getContextPath() %>/businessTripConfirmBack" method="post" style="display:inline;">
    <button type="submit">戻る</button>
  </form>

  <% if (isApprovalScreen && appIdObj != null) { %>
    <!-- 差し戻し -->
    <form action="<%= request.getContextPath() %>/rejectApplication" method="post" style="display:inline;">
      <input type="hidden" name="id" value="<%= appIdObj %>">
      <button type="submit">差し戻し</button>
    </form>

  <% } else if (isDetailMode && appIdObj != null) { %>
    <!-- 編集 -->
    <form action="<%= request.getContextPath() %>/editBusinessTrip" method="get" style="display:inline;">
      <input type="hidden" name="id" value="<%= appIdObj %>">
      <button type="submit">編集</button>
    </form>

  <% } else { %>
    <!-- 送信 -->
    <form action="<%= actionUrl %>" method="post" style="display:inline;">
      <button type="submit">送信</button>
    </form>
  <% } %>
</div>



<div class="footer">
  &copy; 2025 ABC株式会社 - All rights reserved.
</div>
</div> <!-- close page-container -->