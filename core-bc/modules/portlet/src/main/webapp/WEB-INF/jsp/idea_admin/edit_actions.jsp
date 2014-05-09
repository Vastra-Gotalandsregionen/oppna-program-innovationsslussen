<!-- Copyright 2010 Västra Götalandsregionen This library is free software; you can redistribute it and/or modify it
 under the terms of version 2.1 of the GNU Lesser General Public License as published by the Free Software Foundation.
 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

<%@page contentType="text/html" pageEncoding="UTF-8" %>
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

    <c:choose>
        <c:when test="${curIdea.hidden}">
            <portlet:actionURL name="unhideEntry" var="unhideEntryURL">
                <portlet:param name="action" value="unhideEntry" />
                <portlet:param name="entryId" value="${curIdea.id}" />
            </portlet:actionURL>
            <liferay-ui:icon image="install_more" url="${unhideEntryURL}" message="Gör synlig" />
        </c:when>
        <c:otherwise>
            <portlet:actionURL name="hideEntry" var="hideEntryURL">
                <portlet:param name="action" value="hideEntry" />
                <portlet:param name="entryId" value="${curIdea.id}" />
            </portlet:actionURL>
            <liferay-ui:icon image="install_more" url="${hideEntryURL}" message="Göm" />
        </c:otherwise>
    </c:choose>

</liferay-ui:icon-menu>