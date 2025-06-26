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
%>

<div class="btn-section">
  	<form action="<%= request.getContextPath() %>/businessTripConfirmBack" method="post" style="display:inline;">
  	   <button type="submit">戻る</button>
	</form>

	  <% if (isDetailMode && appIdObj != null) { %>
	  <form action="<%= request.getContextPath() %>/editBusinessTrip" method="get" style="display:inline;">
	    <input type="hidden" name="id" value="<%= appIdObj %>">
	    <button type="submit">編集</button>
	  </form>
	<% } else { %>
	  <form action="<%= actionUrl %>" method="post" style="display:inline;">
	    <button type="submit">送信</button>
	  </form>
	<% } %>
	
	<%-- <% if ("confirm".equals(mode)) { %>
  <!-- Nhân viên xác nhận đăng ký mới -->
  <form action="<%= actionUrl %>" method="post"><button>送信</button></form>
<% } else if ("edit".equals(mode)) { %>
  <!-- Đang chỉnh sửa -->
  <form action="/editBusinessTrip" method="post"><button>更新</button></form>
<% } else if ("detail".equals(mode) && isApprover) { %>
  <!-- Trưởng phòng đang duyệt -->
  <form action="/approve" method="post"><button>承認</button></form>
  <form action="/reject" method="post"><button>差戻し</button></form>
<% } %> --%>
</div>



<div class="footer">
  &copy; 2025 ABC株式会社 - All rights reserved.
</div>
</div> <!-- close page-container -->