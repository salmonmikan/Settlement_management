<!-- /WEB-INF/views/common/header.jsp -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<nav>
		精算管理システム
		<form class="logoutForm"
			action="<%=request.getContextPath()%>/logOutServlet" method="post">
			<button type="submit" title="Log out">
				<i class="fa-solid fa-right-from-bracket"></i>
			</button>
		</form>
	</nav>