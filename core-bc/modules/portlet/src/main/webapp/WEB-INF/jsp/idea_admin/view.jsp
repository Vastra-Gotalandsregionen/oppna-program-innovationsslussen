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

<liferay-ui:search-container
	delta="${fn:length(ideas)}"
 >
	<liferay-ui:search-container-results
		results="${ideas}"
		total="${fn:length(ideas)}"
	/>

   	<liferay-ui:search-container-row
       	className="se.vgregion.portal.innovationsslussen.domain.jpa.Idea"
       	keyProperty="id"
       	modelVar="idea"
       >

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