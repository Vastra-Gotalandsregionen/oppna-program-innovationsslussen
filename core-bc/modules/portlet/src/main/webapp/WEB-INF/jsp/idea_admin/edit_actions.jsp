<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="se.vgregion.portal.innovationsslussen.domain.jpa.IdeaRestricted" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />	



<%
		ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
		IdeaRestricted curEntry = (IdeaRestricted)row.getObject();
	%>

<c:set var="curIdea" scope="page" value="<%= curEntry %>" />

<liferay-ui:icon-menu cssClass="">
	<%-- 
	<c:if test="<%= true /* permissionChecker.hasPermission(groupId, name, primKey, ActionKeys.EDIT) */ %>">
		<portlet:renderURL var="editEntryURL">
			<portlet:param name="entryId" value="<%= String.valueOf(curEntry.getEntryId()) %>" />
			<portlet:param name="jspPage" value="/labs_entry_admin/edit_entry.jsp" />
			<portlet:param name="toolbarItem" value="edit" />
		</portlet:renderURL>
		<liferay-ui:icon image="edit" url="<%=editEntryURL.toString() %>" />
	</c:if>
	--%>
	<portlet:actionURL name="deleteEntry" var="deleteEntryURL">
		<portlet:param name="action" value="deleteEntry" />
		<portlet:param name="entryId" value="${curIdea.id}" />
	</portlet:actionURL>
	<liferay-ui:icon image="delete" url="${deleteEntryURL}" />
</liferay-ui:icon-menu>