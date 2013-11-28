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

<portlet:defineObjects />
<liferay-theme:defineObjects />




<c:if test="${not empty errorMessage}">
    <div class="portlet-msg-error">
             ${errorMessage}
    </div>
</c:if>

<liferay-ui:search-container
	delta="${delta}"
 >
	<liferay-ui:search-container-results
		results="${ideas}"
		total="${totalCount}"
	/>

   	<liferay-ui:search-container-row
       	className="se.vgregion.portal.innovationsslussen.domain.jpa.Idea"
       	keyProperty="id"
       	modelVar="idea"
       >
       
		<liferay-portlet:renderURL var="ideaUrl" plid="${ideaPlid}" portletName="${ideaPortletName}">
			<liferay-portlet:param name="showView" value="showIdea" />
			<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
		</liferay-portlet:renderURL>       
       
		<liferay-ui:search-container-column-text
           	name="title"
           	href="${ideaUrl}"
           	target="_BLANK"
           	property="title"
           />

		<liferay-ui:search-container-column-text
           	name="id"
           	property="id"
           />
           
		<liferay-ui:search-container-column-text
           	name="userId"
           	property="userId"
           />

		<liferay-ui:search-container-column-jsp
			path="/WEB-INF/jsp/idea_admin/edit_actions.jsp"
      	/>

   	</liferay-ui:search-container-row>
   	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<div>
  <h1>Synkronisering</h1>
  <portlet:actionURL name="syncAllFromBarium" var="syncAllFromBariumURL">
    <portlet:param name="action" value="syncAllFromBarium"/>
  </portlet:actionURL>

  <form action="${syncAllFromBariumURL}" method="post">
    <input type="submit" value="Synkronisera"/><span>Gör detta för att uppdatera alla idéer i Liferay med information från Barium</span>
  </form>

</div>