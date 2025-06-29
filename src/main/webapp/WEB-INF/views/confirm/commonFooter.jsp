<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
.btn-like-back {
        background: var(--primary-color);
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
    transition: background 0.2s;
}
</style>
<div class="btn-section">

  <%-- 
    SỬA LỖI:
    - Nút "Back" giờ là một thẻ <a> đơn giản trỏ về Servlet của Step 3.
    - Chúng ta thêm một class CSS "btn-like" để nó trông giống một cái nút.
  --%>
  <c:if test="${showBackButton}">
    <a href="${pageContext.request.contextPath}${backActionUrl}" class="btn-like-back">戻る</a>
  </c:if>

  <%-- Nút Edit sẽ chỉ hiển thị khi được cho phép --%>
  <c:if test="${showEditButton}">
    <form action="${pageContext.request.contextPath}${editActionUrl}" method="get" style="display:inline;">
      <button type="submit">編集</button>
    </form>
  </c:if>

  
  <c:if test="${showSubmitButton}">
	<form action="${pageContext.request.contextPath}${submitActionUrl}" method="post" style="display:inline;">
	  <button type="submit">送信</button>
	</form>
  </c:if>

</div>

<div class="footer">
  &copy; 2025 ABC株式会社 - All rights reserved.
</div>
</div> ```