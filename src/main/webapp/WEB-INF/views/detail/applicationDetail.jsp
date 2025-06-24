<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String type = (String) request.getAttribute("application_type");
%>

<jsp:include page="/WEB-INF/views/detail/detailHeader.jsp" />

<% if ("出張費".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/detail/businessTripDetail.jsp" />
<% } else if ("交通費".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/detail/transportDetail.jsp" />
<% } else if ("立替金".equals(type)) { %>
  <jsp:include page="/WEB-INF/views/detail/reimbursementDetail.jsp" />
<% } else { %>
  <p style="color: red;">不明な申請タイプです。</p>
<% } %>

<jsp:include page="/WEB-INF/views/detail/detailFooter.jsp" />