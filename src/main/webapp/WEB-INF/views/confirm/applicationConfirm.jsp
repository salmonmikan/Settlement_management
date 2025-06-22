<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
%>

<jsp:include page="/WEB-INF/views/confirm/commonHeader.jsp" />

<% if ("出張費".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/confirm/businessTripConfirmBody.jsp" />
<% } else if ("交通費".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/confirm/transportConfirmBody.jsp" />
<% } else if ("立替金".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/confirm/reimbursementConfirmBody.jsp" />
<% } else { %>
  <p style="color: red;">不明な申請タイプです。</p>
<% } %>
<input type="hidden" name="editMode" value="${editMode}">
<input type="hidden" name="applicationId" value="${applicationId}">
<jsp:include page="/WEB-INF/views/confirm/commonFooter.jsp" />