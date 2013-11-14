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

<div class="idea-list-wrap">

	<h2 class="title">St&auml;ngda id&eacute;er</h2>
	
	<c:choose>
		<c:when test="${isSignedIn}">
			<c:choose>
				<c:when test="${not empty ideaList}">
					<%@ include file="list_ideas.jsp" %>				
				</c:when>
				<c:otherwise>
					Det finns inga st&auml;ngda id&eacute;er &auml;nnu.
				</c:otherwise>
			</c:choose>		
		</c:when>
		<c:otherwise>
			<div class="portlet-msg-error">
	a			Du m&aring;ste vara inloggad f&ouml;r att kunna se st&auml;ngda id&eacute;er.
				Information om inloggning finns på sidan Fr&aring;gor och Svar.
			</div>		
		</c:otherwise>
	</c:choose>

</div>