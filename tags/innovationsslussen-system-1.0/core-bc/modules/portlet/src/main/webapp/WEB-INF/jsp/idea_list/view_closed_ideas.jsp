<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />	

<div class="idea-list-wrap">

	<h2 class="title">St&auml;ngda id&eacute;er</h2>
	
	<c:choose>
		<c:when test="${isSignedIn}">
			<c:choose>
				<c:when test="${not empty ideaList}">
					<%@ include file="list_ideas.jsp" %>				
				</c:when>
				<c:otherwise>
					Det finns inga st&auml;ngda id&eacute;er &auml;nnu.
				</c:otherwise>
			</c:choose>		
		</c:when>
		<c:otherwise>
			<div class="portlet-msg-error">
				Du m&aring;ste vara inloggad f&ouml;r att kunna se st&auml;ngda id&eacute;er.
			</div>		
		</c:otherwise>
	</c:choose>

</div>