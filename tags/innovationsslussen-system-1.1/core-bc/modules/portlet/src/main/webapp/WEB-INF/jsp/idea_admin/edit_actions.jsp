<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="se.vgregion.portal.innovationsslussen.domain.jpa.Idea" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />	

<%
	ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Idea curEntry = (Idea)row.getObject();
%>

<c:set var="curIdea" scope="page" value="<%= curEntry %>" />

<liferay-ui:icon-menu cssClass="">

	<portlet:actionURL name="syncIdeaFromBarium" var="syncIdeaFromBariumUrl">
		<portlet:param name="action" value="syncIdeaFromBarium" />
		<portlet:param name="entryId" value="${curIdea.id}" />
	</portlet:actionURL>
	<liferay-ui:icon image="install_more" url="${syncIdeaFromBariumUrl}" message="Uppdatera fr&aring;n Barium" />


	<portlet:actionURL name="deleteEntry" var="deleteEntryURL">
		<portlet:param name="action" value="deleteEntry" />
		<portlet:param name="entryId" value="${curIdea.id}" />
	</portlet:actionURL>
	<liferay-ui:icon-delete url="${deleteEntryURL}" confirmation="Är du säker på att du vill ta bort denna idé?" />
	
	
</liferay-ui:icon-menu>