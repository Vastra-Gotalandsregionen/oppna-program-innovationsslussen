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
	<c:choose>
		<c:when test="${ideaListType == '1'}">
			<h2 class="title">Mina id&eacute;er</h2>
		</c:when>
		<c:when test="${ideaListType == '2'}">
			<h2 class="title">Mina favoriter</h2>
		</c:when>
		<c:when test="${ideaListType == '3'}">
			<h2 class="title">St&auml;ngda id&eacute;er</h2>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	
	<ul class="idea-list">
		
		<c:forEach items="${ideaList}" var="idea" varStatus="index">
		
			<liferay-portlet:renderURL var="ideaUrl" plid="${ideaPlid}" portletName="${ideaPortletName}">
				<liferay-portlet:param name="showView" value="showIdea" />
				<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
			</liferay-portlet:renderURL>
		
			<li>
				<div class="idea">
					<div class="idea-content">
	
						<h2 class="title">
							<a href="${ideaUrl}">${idea.title}</a>
						</h2>
						
						<div class="clearfix">
							<ul class="idea-stats">
								<li class="likes">
								(${fn:length(idea.likes)})
								</li>
							</ul>
							<div class="idea-info">
								<div class="description">
									<a href="${ideaUrl}">
										<a href="${ideaUrl}">${fn:substring(idea.ideaContentPrivate.description, 0, 150)}</a>
									</a>
								</div>
							</div>
						</div>
						
						<ul class="idea-flow-list">
							<li class="done"><span>1</span></li>
							<li class="done"><span>2</span></li>
							<li><span>3</span></li>
							<li><span>4</span></li>
							<li><span>5</span></li>
							<li><span>6</span></li>
						</ul>
					</div>
				</div>
			</li>
		</c:forEach>	
	
	</ul>
</div>