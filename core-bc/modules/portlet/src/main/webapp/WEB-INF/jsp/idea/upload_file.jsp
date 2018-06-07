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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<div class="idea-outer idea-outer-${ideaType}">
  <div class="idea">
    <div class="idea-inner">

      <portlet:renderURL var="backUrl">
        <portlet:param name="urlTitle" value="${urlTitle}" />
        <portlet:param name="showView" value="showIdea" />
        <portlet:param name="type" value="${ideaType}" />
      </portlet:renderURL>
      <p><a href="${backUrl}">Tillbaka</a></p>

      <c:if test="${not empty errorMessage}">
        <span class="portlet-msg-error">${errorMessage}</span>
      </c:if>

      <portlet:actionURL var="uploadFileUrl">
        <portlet:param name="action" value="uploadFile" />
        <portlet:param name="fileType" value="${ideaType}" />
        <portlet:param name="urlTitle" value="${urlTitle}" />
      </portlet:actionURL>

      <h1>Ladda upp dokument</h1>

      <form action="${uploadFileUrl}" enctype="multipart/form-data" method="post">
        <input type="file" name="file" width="160"/>
        <input type="submit" value="Ladda upp"/>
      </form>
    </div>
  </div>
</div>