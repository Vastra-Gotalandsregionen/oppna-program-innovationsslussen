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

<c:if test="${ideaPermissionChecker.isIdeaOwner}">
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

<ul class="documents-list">
	<c:forEach items="${ideaFiles}" var="file" varStatus="counter">
		<c:set var="fileType" value="txt"/>
		<c:choose>
			<c:when test="${file.fileType eq 'application/pdf'}">
				<c:set var="fileType" value="pdf"/>
			</c:when>
			<c:when test="${file.fileType eq 'application/doc' or file.fileType eq 'application/docx'}">
				<c:set var="fileType" value="doc"/>
			</c:when>
			<c:when test="${file.fileType eq 'application/png' or file.fileType eq 'application/jpg' or file.fileType eq 'application/gif'}">
				<c:set var="fileType" value="img"/>
			</c:when>
		</c:choose>

		<portlet:resourceURL id="downloadFile" var="downloadFileUrl">
			<portlet:param name="id" value="${file.bariumId}"/>
		</portlet:resourceURL>

		<li id="<portlet:namespace />documentListItem-${counter.count}" class="${fileType}">
			<a href="${downloadFileUrl}" target="_blank">${file.name}</a>
		</li>
	</c:forEach>

</ul>