<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
  Object appIdObj = request.getAttribute("application_id");

  String editUrl = "#"; // default fallback
  if ("出張費".equals(type)) {
    editUrl = request.getContextPath() + "/editBusinessTrip";
  } else if ("交通費".equals(type)) {
    editUrl = request.getContextPath() + "/editTransport";
  } else if ("立替金".equals(type)) {
    editUrl = request.getContextPath() + "/editReimbursement";
  }
%>

<div class="btn-section">
  <!-- 戻る -->
  <form action="<%= request.getContextPath() %>/applicationList" method="get" style="display:inline;">
    <button type="submit">戻る</button>
  </form>

  <!-- 編集 -->
  <form action="<%= editUrl %>" method="get" style="display:inline;">
    <input type="hidden" name="id" value="<%= appIdObj %>">
    <button type="submit">編集</button>
  </form>
</div>

<div class="footer">
  &copy; 2025 ABC株式会社 - All rights reserved.
</div>
</div> <!-- close page-container -->