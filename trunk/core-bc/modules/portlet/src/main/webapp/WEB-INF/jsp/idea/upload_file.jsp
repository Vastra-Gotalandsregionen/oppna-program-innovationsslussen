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

<div class="idea-outer">
  <div class="idea">
    <div class="idea-inner">

      <portlet:renderURL var="backUrl">
        <portlet:param name="urlTitle" value="${urlTitle}" />
        <portlet:param name="showView" value="showIdea" />
      </portlet:renderURL>
      <p><a href="${backUrl}">Tillbaka</a></p>

      <c:if test="${not empty errorMessage}">
        <span class="portlet-msg-error">${errorMessage}</span>
      </c:if>

      <div id="<portlet:namespace />myTab">

        <ul id="<portlet:namespace />tabList" class="nav nav-tabs">
          <li><a href="#tab-1">Stängd</a></li>
          <li><a href="#tab-2">Öppen</a></li>
        </ul>

        <div id="<portlet:namespace />tabContent" class="tab-content">
          <div id="tab-1" class="tab-pane">

            <portlet:actionURL var="uploadFileUrl">
              <portlet:param name="action" value="uploadFile" />
              <portlet:param name="fileType" value="liferayClosed" />
              <portlet:param name="urlTitle" value="${urlTitle}" />
            </portlet:actionURL>

            <h1>
              Ladda upp dokument (stängt)
            </h1>

            <p>Här laddar du upp ett dokument som <i>inte</i> ska synas för alla.</p>

            <form action="${uploadFileUrl}" enctype="multipart/form-data" method="post">
              <input type="file" name="file" width="160"/>
              <input type="submit" value="Ladda upp"/>
            </form>
          </div>
          <div id="tab-2">
            <portlet:actionURL var="uploadFileUrl">
              <portlet:param name="action" value="uploadFile" />
              <portlet:param name="fileType" value="liferayOpen" />
              <portlet:param name="urlTitle" value="${urlTitle}" />
            </portlet:actionURL>

            <h1>
              Ladda upp dokument (öppet)
            </h1>

            <p>Här laddar du upp ett dokument som <i>ska</i> synas för alla.</p>

            <form action="${uploadFileUrl}" enctype="multipart/form-data" method="post">
              <input type="file" name="file" width="160"/>
              <input type="submit" value="Ladda upp"/>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<liferay-util:html-bottom>
  <script type="text/javascript">
    AUI().use(
            'aui-tabs',
            function(A) {
              new A.TabView(
                      {
                        boundingBox: '#<portlet:namespace />myTab',
                        listNode: '#<portlet:namespace />tabList',
                        contentNode: '#<portlet:namespace />tabContent'
                      }
              ).render();
            }
    );
  </script>
</liferay-util:html-bottom>