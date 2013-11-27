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

<portlet:actionURL name="requestSearch" var="requestSearchURL" />

<div class="idea-list-wrap">

    <aui:form action="${requestSearchURL}" name="filterForm" method="post" cssClass="firstpage-form clearfix">
            <aui:select name="ideaPhase" label="Filter">
                <c:set var="ideaPhase" target="page" value="false" />
                <c:if test="${ideaListType == '0'}">
                    <c:set var="ideaPhase" target="page" value="true" />
                </c:if>
                <aui:option value="0" selected="${ideaPhase}" label="Alla" />

                <c:set var="ideaPhase" target="page" value="false" />
                <c:if test="${ideaListType == '2'}">
                    <c:set var="ideaPhase" target="page" value="true" />
                </c:if>
                <aui:option value="2" selected="${ideaPhase}" label="id&eacute;er" />

                <c:set var="ideaPhase" target="page" value="false" />
                <c:if test="${ideaListType == '3'}">
                    <c:set var="ideaPhase" target="page" value="true" />
                </c:if>
                <aui:option value="3" selected="${ideaPhase}" label="Mognad" />

                <c:set var="ideaPhase" target="page" value="false" />
                <c:if test="${ideaListType == '5'}">
                    <c:set var="ideaPhase" target="page" value="true" />
                </c:if>
                <aui:option value="5" selected="${ideaPhase}" label="F&aumlrdig" />
            </aui:select>

            <aui:select name="ideaSort" label="Sortering">
                <c:set var="ideaSort" target="page" value="false" />
                <c:if test="${ideaListType == '0'}">
                    <c:set var="ideaSort" target="page" value="true" />
                </c:if>
                <aui:option value="0" selected="${ideaSort}" label="Senast skapade" />

                <c:set var="ideaSort" target="page" value="false" />
                <c:if test="${ideaListType == '1'}">
                    <c:set var="ideaSort" target="page" value="true" />
                </c:if>
                <aui:option value="1" selected="${ideaSort}" label="Mest gillade" />

                <c:set var="ideaSort" target="page" value="false" />
                <c:if test="${ideaListType == '2'}">
                    <c:set var="ideaSort" target="page" value="true" />
                </c:if>
                <aui:option value="2" selected="${ideaSort}" label="Mest kommenterad" />

                <c:set var="ideaSort" target="page" value="false" />
                <c:if test="${ideaListType == '3'}">
                    <c:set var="ideaSort" target="page" value="true" />
                </c:if>
                <aui:option value="3" selected="${ideaSort}" label="Senast kommenterad" />
            </aui:select>

            <input type="submit" class="grey" value="<liferay-ui:message key="search" />" />
    </aui:form>

	<c:choose>
		<c:when test="${not empty ideaList}">
		     <%-- 
                <%@ include file="filter_toolbar.jsp" %>
            --%>
			<%@ include file="list_ideas.jsp" %>
		</c:when>
		<c:otherwise>
			Det finns inga &ouml;ppna id&eacute;er &auml;nnu.
		</c:otherwise>
	</c:choose>

</div>
