<!-- Copyright 2010 Västra Götalandsregionen This library is free software; you can redistribute it and/or modify it
 under the terms of version 2.1 of the GNU Lesser General Public License as published by the Free Software Foundation.
 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

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

		.innovation-settings-wrap textarea {
            min-width: 450px;
            min-height: 180px;
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
		
		<aui:fieldset label="Sidor">
			<aui:field-wrapper>
				<aui:input type="text" name="createIdeaFriendlyURL" value="${createIdeaFriendlyURL}" label="Skapa-id&eacute; friendly-URL" helpMessage="Skriv in friendly url f&ouml;r sidan f&ouml;r att skapa id&eacute; (t.ex. /create-idea) ." />
			</aui:field-wrapper>
			<aui:field-wrapper>
				<aui:input type="text" name="faqFriendlyURL" value="${faqFriendlyURL}" label="FAQ friendly-URL" helpMessage="Skriv in friendly url f&ouml;r sidan f&ouml;r FAQ (t.ex. /faq) ." />
			</aui:field-wrapper>
		</aui:fieldset>

        <aui:fieldset label="Notifieringar via E-post">
            <aui:field-wrapper>
                <aui:input type="checkbox" name="notificationEmailActive" value="${notificationEmailActive}" label="Aktivera epost-notifieringen." />
            </aui:field-wrapper>
            <aui:field-wrapper>
                <aui:input type="text" name="notificationEmailForm" value="${notificationEmailForm}" label="Ange den e-postadress som notifieringen ska skickas fr&aring;n." />
            </aui:field-wrapper>
            <aui:field-wrapper>
                <aui:input type="text" name="notificationEmailSubject" value="${notificationEmailSubject}" label="Subjekt f&ouml;r notifieringen" />
            </aui:field-wrapper>
            <aui:field-wrapper>
                <aui:input type="textarea" name="notificationEmailPublicBody" value="${notificationEmailPublicBody}" label="Body f&ouml;r &ouml;ppna notifieringen" />
            </aui:field-wrapper>
            <aui:field-wrapper>
                <aui:input type="textarea" name="notificationEmailPrivateBody" value="${notificationEmailPrivateBody}" label="Body f&ouml;r s&auml;ngda notifieringen" />
            </aui:field-wrapper>
        </aui:fieldset>

        <aui:fieldset label="Server">
             <aui:field-wrapper>
                <aui:input type="text" name="serverNameUrl" value="${serverNameUrl}" label="Ange url f&ouml;r idea view portlet" />
             </aui:field-wrapper>
        </aui:fieldset>

		<aui:button-row>
			<aui:button type="submit" value="save" />
		</aui:button-row> 
	</aui:form>
</div>