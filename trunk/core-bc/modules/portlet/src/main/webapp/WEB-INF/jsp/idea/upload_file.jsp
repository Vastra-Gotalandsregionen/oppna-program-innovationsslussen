<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<div class="idea-outer">
  <div class="idea">
    <div class="idea-inner">

      <portlet:actionURL var="uploadFileUrl">
        <portlet:param name="action" value="uploadFile" />
        <portlet:param name="urlTitle" value="${urlTitle}" />
      </portlet:actionURL>

      <c:if test="${not empty errorMessage}">
        <span class="portlet-msg-error">${errorMessage}</span>
      </c:if>

      <h1>
        Ladda upp fil
      </h1>

      <form action="${uploadFileUrl}" enctype="multipart/form-data" method="post">
        <input type="hidden" name="urlTitle" value="${urlTitle}"/>
        <input type="file" name="file" width="160"/>
        <input type="submit" value="Ladda upp"/>
      </form>
    </div>
  </div>
</div>