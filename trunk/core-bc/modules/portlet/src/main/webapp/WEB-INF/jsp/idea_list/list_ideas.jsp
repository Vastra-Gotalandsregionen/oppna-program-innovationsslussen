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

<%-- 
<c:if test="${not empty ideaList}">
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
										<a href="${ideaUrl}">
										
											<c:choose>
												<c:when test="${idea.isPublic}">
													${fn:substring(idea.ideaContentPublic.description, 0, 150)}
												</c:when>
												<c:otherwise>
													${fn:substring(idea.ideaContentPrivate.description, 0, 150)}
												</c:otherwise>
											</c:choose>
										</a>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</li>
		</c:forEach>
		<liferay-util:include page="/WEB-INF/jsp/idea_list/tpl_paginator.jsp" servletContext="<%= application %>" />	
	</ul>
</c:if>
--%>

<c:if test="${not empty ideaList}">
	<ul class="idea-list-new">
		<c:forEach items="${ideaList}" var="idea" varStatus="index">
		
			<liferay-portlet:renderURL var="ideaUrl" plid="${ideaPlid}" portletName="${ideaPortletName}">
				<liferay-portlet:param name="showView" value="showIdea" />
				<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
			</liferay-portlet:renderURL>
		
			<li>
				<a href="#">
					<div class="idea-content">
						<div class="idea-content-1">
							<h3>Content 1. Lorem ipsum dolarem sit amet.</h3>
						</div>
						<div class="idea-content-2">
							Content 2. Lorem ipsum dolarem sit amet.
						</div>
					</div>
					<div class="idea-label">
						Under utveckling
					</div>
				</a>
			
				<%-- 
			
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
										<a href="${ideaUrl}">
										
											<c:choose>
												<c:when test="${idea.isPublic}">
													${fn:substring(idea.ideaContentPublic.intro, 0, 150)}
												</c:when>
												<c:otherwise>
													${fn:substring(idea.ideaContentPrivate.description, 0, 150)}
												</c:otherwise>
											</c:choose>
										</a>
									</a>
								</div>
							</div>
						</div>
						
					</div>
				</div>
				--%>
				
			</li>
		</c:forEach>
		<liferay-util:include page="/WEB-INF/jsp/idea_list/tpl_paginator.jsp" servletContext="<%= application %>" />	
	</ul>
</c:if>