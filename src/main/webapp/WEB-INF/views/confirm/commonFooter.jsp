<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
  String actionUrl = "";

  if ("出張費".equals(type)) {
    actionUrl = request.getContextPath() + "/submitBusinessTrip";
  } else if ("交通費".equals(type)) {
    actionUrl = request.getContextPath() + "/submitTransport";
  } else if ("立替金".equals(type)) {
    actionUrl = request.getContextPath() + "/submitReimbursement";
  }
%>

<div class="btn-section">
  <!-- 戻る -->
  <form action="<%= request.getContextPath() %>/businessTripConfirmBack" method="post" style="display:inline;">
    <button type="submit">戻る</button>
  </form>

  <!-- 送信 -->
  <form action="<%= actionUrl %>" method="post" style="display:inline;">
    <button type="submit">送信</button>
  </form>
</div>

<div class="footer">
  &copy; 2025 ABC株式会社 - All rights reserved.
</div>
</div> <!-- close page-container -->