<%@page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<c:choose>
	<c:when test="${ideaType eq 'public'}">
		<c:set var="ideaFiles" value="${idea.ideaContentPublic.ideaFiles}"/>
	</c:when>
	<c:otherwise>
		<c:set var="ideaFiles" value="${idea.ideaContentPrivate.ideaFiles}"/>
	</c:otherwise>
</c:choose>

<h2>
	<span>Dokument</span>
</h2>

<c:set var="isAllowedToUploadDocuments" scope="page" value="false" />

<c:choose>
    <c:when test="${ideaPermissionChecker.isIdeaOwner}">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
    <c:when test="${ideaPermissionChecker.hasPermissionAddDocumentPublic and (ideaType eq 'public') }">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
    <c:when test="${ideaPermissionChecker.hasPermissionAddDocumentPrivate and (ideaType eq 'private') }">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
    <c:when test="${ideaPermissionChecker.userPrioCouncilMember}">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
    <c:when test="${ideaPermissionChecker.userInnovationsslussenEmployee}">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
    <c:when test="${ideaPermissionChecker.userIdeaTransporter}">
        <c:set var="isAllowedToUploadDocuments" scope="page" value="true" />
    </c:when>
</c:choose>


<c:if test="${isAllowedToUploadDocuments}">
	<portlet:renderURL var="uploadFile">
		<portlet:param name="urlTitle" value="${param.urlTitle}"/>
		<portlet:param name="showView" value="showUploadFile"/>
		<portlet:param name="ideaType" value="${ideaType}"/>
   	</portlet:renderURL>
   	<div class="link-button-wrap clearfix">
		<a href="${uploadFile}" class="link-button-mod-1 link-button-mod-1-add">
			<span>
				L&auml;gg till dokument
			</span>
		</a>
	</div>
</c:if>

<c:if test="${not empty ideaFiles}">
	<ul class="documents-list">
		<c:forEach items="${ideaFiles}" var="file" varStatus="counter">
			<portlet:resourceURL id="downloadFile" var="downloadFileUrl">
				<portlet:param name="id" value="${file.bariumId}"/>
			</portlet:resourceURL>
	
			<li id="<portlet:namespace />documentListItem-${counter.count}" class="${file.fileType}">
				<a href="${downloadFileUrl}" target="_blank">${file.name}</a>
			</li>
		</c:forEach>
	
	</ul>
</c:if>