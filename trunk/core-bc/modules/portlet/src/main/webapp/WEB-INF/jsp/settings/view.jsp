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
		
		<aui:fieldset label="Inst&auml;llningar f&ouml;r Social Sharing">
			<aui:field-wrapper>
				<aui:input type="text" name="addThisCode" value="${addThisCode}" label="Kod f&ouml;r AddThis" helpMessage="Skriv in kod som anv&auml;nds f&ouml;r social sharing via AddThis. Koden m&aring;ste vara kopplad till den dom&auml;n siten k&ouml;rs p&aring;. Om f&auml;ltet l&auml;mnas blankt kommer inte social sharing via AddThis att aktiveras." />
			</aui:field-wrapper>
		</aui:fieldset>

		<aui:fieldset label="Inst&auml;llningar f&ouml;r Piwik">
			<aui:field-wrapper>
				<aui:input type="text" name="piwikCode" value="${piwikCode}" label="Kod f&ouml;r Piwik" helpMessage="Skriv in kod som anv&auml;nds f&ouml;r web analytics via Piwik. Koden m&aring;ste vara kopplad till den dom&auml;n siten k&ouml;rs p&aring;. Om f&auml;ltet l&auml;mnas blankt kommer inte web analytics via Piwik att aktiveras." />
			</aui:field-wrapper>
		</aui:fieldset>
		
	
		<aui:button-row>
			<aui:button type="submit" value="save" />
		</aui:button-row> 
	</aui:form>
</div>