<!-- Copyright 2010 Västra Götalandsregionen This library is free software; you can redistribute it and/or modify it
 under the terms of version 2.1 of the GNU Lesser General Public License as published by the Free Software Foundation.
 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://portalen.vgregion.se/util" prefix="vgrutil" %>

<c:if test="${not empty commentsList and commentCount > maxCommentCountDisplay}">
    <liferay-portlet:renderURL var="ideaUrl" plid="${ideaPlid}" portletName="${ideaPortletName}">
        <liferay-portlet:param name="showView" value="showIdea" />
        <liferay-portlet:param name="moreComments" value="moreComments" />
        <liferay-portlet:param name="maxCommentCountDisplay" value="${maxCommentCountDisplay}" />
        <c:if test="${not idea.isPublic()}">
            <liferay-portlet:param name="type" value="private" />
        </c:if>
        <liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
    </liferay-portlet:renderURL>
    <a href="${ideaUrl}" class="action-link action-link-button">
      <span>
          <c:choose>
            <c:when test="${((commentCount - maxCommentCountDisplay) > defaultCommentCount)}">
              Visa ytterligare ${defaultCommentCount} kommentarer av totalt ${commentCount} st.
            </c:when>
            <c:otherwise>
              Visa de sista ${(commentCount - maxCommentCountDisplay)} kommentarerna av totalt ${commentCount} st.
            </c:otherwise>
          </c:choose>
      </span>
    </a>
</c:if>