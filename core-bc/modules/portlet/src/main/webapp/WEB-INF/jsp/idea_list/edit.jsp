<!-- Copyright 2010 Västra Götalandsregionen This library is free software; you can redistribute it and/or modify it
 under the terms of version 2.1 of the GNU Lesser General Public License as published by the Free Software Foundation.
 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

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

<p>This is the edit view</p>

 <portlet:actionURL name="save" var="saveURL">
    <portlet:param name="action" value="save"/>
</portlet:actionURL>

<aui:form action="${saveURL}" method="post" name="preferencesForm" cssClass="idea-list-preferences-form">
	
	<%-- All different types of ideaListType are listed in se.vgregion.portal.innovationsslussen.util.IdeaPortletsConstants --%>
	<aui:select name="ideaListType" label="Visa f&ouml;ljande typ av id&eacute;er">
	
		<c:set var="isOptionSelected" target="page" value="false" />
		<c:if test="${ideaListType == '0'}">
			<c:set var="isOptionSelected" target="page" value="true" />
		</c:if>
		<aui:option value="0" selected="${isOptionSelected}" label="Alla &ouml;ppna id&eacute;er" />
		
		<c:set var="isOptionSelected" target="page" value="false" />
		<c:if test="${ideaListType == '1'}">
			<c:set var="isOptionSelected" target="page" value="true" />
		</c:if>
		<aui:option value="1" selected="${isOptionSelected}" label="Alla id&eacute;er skapade av den inloggade anv&auml;ndaren (b&ouml;r bara visas p&aring; anv&auml;ndarens egna sida)" />

		<c:set var="isOptionSelected" target="page" value="false" />
		<c:if test="${ideaListType == '2'}">
			<c:set var="isOptionSelected" target="page" value="true" />
		</c:if>
		<aui:option value="2" selected="${isOptionSelected}" label="Alla id&eacute;er som den inloggade anv&auml;ndaren har lagt till som favorit (b&ouml;r bara visas p&aring; anv&auml;ndarens egna sida)" />

		<c:set var="isOptionSelected" target="page" value="false" />
		<c:if test="${ideaListType == '3'}">
			<c:set var="isOptionSelected" target="page" value="true" />
		</c:if>
		<aui:option value="3" selected="${isOptionSelected}" label="Alla st&auml;ngda id&eacute;er (b&ouml;r bara visas p&aring; innovationscoachernas sida)" />

        <c:set var="isOptionSelected" target="page" value="false" />
		<c:if test="${ideaListType == '4'}">
			<c:set var="isOptionSelected" target="page" value="true" />
		</c:if>
		<aui:option value="4" selected="${isOptionSelected}" label="Alla Id&eacute;er f&ouml;r id&eacute;transport&ouml;rernas (b&ouml;r bara visas p&aring; innovationscoachernas sida)" />
	</aui:select>

    <aui:select name="entryCount" label="entry-count">
        <c:forEach  begin="1" end="20" step="1" varStatus="countStatus">
            <aui:option selected="${entryCount == countStatus.index ? 'true' : 'false'}" value="${countStatus.index}">${countStatus.index} st</aui:option>
        </c:forEach>
    </aui:select>

	
	<aui:button-row>
		<aui:button type="submit" value="save" />
	</aui:button-row>
</aui:form>