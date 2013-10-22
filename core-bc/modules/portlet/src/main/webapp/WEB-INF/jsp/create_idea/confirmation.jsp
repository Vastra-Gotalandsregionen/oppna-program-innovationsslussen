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

<div class="idea-outer">
	<div class="idea create-idea create-confirmation">
		<div class="idea-inner">
			
			<h1>Tack f&ouml;r din id&eacute;!</h1>
			
			<div class="content">
				<p class="intro-text">
					Genom att du postat en id&eacute; har du just varit med om att skapa en b&auml;ttre framtid inom v&aring;rden.
				</p>
				<p>
					Tillsammans med dig kommer Innovationsslussen nu att vrida och v&auml;nda p&aring; id&eacute;en f&ouml;r att se om den g&aring;r att utveckla ytterligare.
				</p>
				<p>
					Du kommer inom kort att bli kontaktad av en id&eacute;transport&ouml;r som hj&auml;lper dig vidare med id&eacute;en.
				</p>
				<p>
					Vissa id&eacute;er kan vi b&ouml;rja jobba med direkt, medan andra tar mer tid i anspr&aring;k. Oavsett vilket kan du f&ouml;lja utvecklingen direkt h&auml;r p&aring; Innovationsslussen.
				</p>
				
				<div class="link-buttons-wrap clearfix">

	                <liferay-portlet:renderURL var="ideaPrivateUrl"plid="${ideaPlid}" portletName="${ideaPortletName}" >
	                    <liferay-portlet:param name="showView" value="showIdea" />
	                    <liferay-portlet:param name="type" value="private" />
	                    <liferay-portlet:param name="urlTitle" value="${urlTitle}" />
	                </liferay-portlet:renderURL>
	                
	                <liferay-portlet:renderURL var="faqURL"plid="${faqPlid}" />

					<a class="link-button-mod" href="${ideaPrivateUrl}">
						G&aring; till din id&eacute; <span class="text-icon">&raquo;</span>
					</a>
					<a class="link-button-mod" href="${faqURL}">
						L&auml;s mer i v&aring;r FAQ <span class="text-icon">&raquo;</span>
					</a>
				</div>

			</div>
			
		</div>
	</div>
</div>