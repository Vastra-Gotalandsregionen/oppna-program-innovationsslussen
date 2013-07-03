<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<liferay-util:html-top>
	<style type="text/css">

		.innovation-settings-wrap input[type="text"] {
			min-width: 300px;
		}
		
	</style>
</liferay-util:html-top>

<portlet:actionURL name="saveSettings" var="saveSettingsURL">
    <portlet:param name="action" value="save"/>
</portlet:actionURL>

<div class="innovation-settings-wrap">
	<h1><liferay-ui:message key="settings" /></h1>  
	<aui:form action="${saveSettingsURL}" method="post" name="setSettings">
	
		<aui:fieldset label="Inst&auml;llningar f&ouml;r Barium">
			<aui:field-wrapper>
				<aui:input type="text" name="bariumDetailsViewUrlPrefix" value="${bariumDetailsViewUrlPrefix}" label="URL-prefix f&ouml;r detaljvyn i Barium" helpMessage="Anv&auml;nds f&ouml;r l&auml;nk till detaljvyn I Barium tillsammans med instans-id f&ouml;r en processinstans (dvs en id&ecaute;)" />
			</aui:field-wrapper>
		</aui:fieldset>
	
		<aui:button-row>
			<aui:button type="submit" value="save" />
		</aui:button-row> 
	</aui:form>
</div>