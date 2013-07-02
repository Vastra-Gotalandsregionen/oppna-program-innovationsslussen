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
	<div class="idea">
		<div class="idea-inner">
		
			<c:choose>
				<c:when test="${not empty idea}">

					<div class="idea-hd clearfix">
					
						<div class="idea-toolbar-wrap">
							<ul class="rp-toolbar clearfix">
							
								<c:if test="${ideaPermissionChecker.hasPermissionViewIdeaPrivate}">
									<li class="icon closed">
									
										<liferay-portlet:renderURL var="ideaPrivateUrl">
											<liferay-portlet:param name="showView" value="showIdea" />
											<liferay-portlet:param name="type" value="private" />
											<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
										</liferay-portlet:renderURL>
									
										<a href="${ideaPrivateUrl}">
											<span>Visa st&auml;ngd beskrivning</span>
										</a>
									</li>
								</c:if>
							
								<li class="icon comment">
									<a href="#">
										<span>Kommentera (${fn:length(idea.ideaContentPublic.comments)})</span>
									</a>
								</li>
								
								<li class="icon like">
									<c:choose>
										<c:when test="${isIdeaUserLiked}">
											
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="removeLikeUrl" scope="page" value="#" />
											<c:if test="${ideaPermissionChecker.hasPermissionDeleteLike}">
												<c:set var="linkCssClass" scope="page" value="" />
												<portlet:actionURL name="removeLike" var="removeLikeUrl">
													<portlet:param name="action" value="removeLike" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="0" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${removeLikeUrl}">
												<span>Sluta gilla (${fn:length(idea.likes)})</span>
											</a>
										</c:when>
										<c:otherwise>
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="addLikeUrl" scope="page" value="#" />
											<c:if test="${ideaPermissionChecker.hasPermissionAddLike}">
												<c:set var="linkCssClass" scope="page" value="" />
												<portlet:actionURL name="addLike" var="addLikeUrl">
													<portlet:param name="action" value="addLike" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="0" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${addLikeUrl}">
												<span>Gilla (${fn:length(idea.likes)})</span>
											</a>
										</c:otherwise>
									</c:choose>
								</li>
								
								<li class="icon favorite last">
									<c:choose>
										<c:when test="${isIdeaUserFavorite}">
										
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="removeFavoriteUrl" scope="page" value="#" />
											<c:if test="${ideaPermissionChecker.hasPermissionDeleteFavorite}">
												<c:set var="linkCssClass" scope="page" value="" />
												<portlet:actionURL name="removeFavorite" var="removeFavoriteUrl">
													<portlet:param name="action" value="removeFavorite" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="0" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${removeFavoriteUrl}">
												<span>Ta bort som favorit (${fn:length(idea.favorites)})</span>
											</a>
										</c:when>
										<c:otherwise>
										
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="addFavoriteUrl" scope="page" value="#" />
											<c:if test="${ideaPermissionChecker.hasPermissionAddFavorite}">
												<c:set var="linkCssClass" scope="page" value="" />
												<portlet:actionURL name="addFavorite" var="addFavoriteUrl">
													<portlet:param name="action" value="addFavorite" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="0" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${addFavoriteUrl}">
												<span>L&auml;gg till som favorit (${fn:length(idea.favorites)})</span>
											</a>
										</c:otherwise>
									</c:choose>
								</li>
							</ul>
						</div>
						
						<div class="idea-flow-wrap">
							<ul class="idea-flow-list">
								<li class="done" title="This is step 1. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>1</span>
								</li>
								<li class="done" title="This is step 2. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>2</span>
								</li>
								<li title="This is step 3. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>3</span>
								</li>
								<li title="This is step 4. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>4</span>
								</li>
								<li title="This is step 5. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>5</span>
								</li>
								<li title="This is step 6. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
									<span>6</span>
								</li>
							</ul>
						</div>
					
					</div>
				
					<h1>
						${idea.title}
					</h1>
					
					<aui:layout>
						<aui:column first="true" columnWidth="60" cssClass="idea-content">
							
							<c:if test="${fn:length(idea.ideaContentPublic.intro) gt 0}">
								<p class="intro">
									${idea.ideaContentPublic.intro}
								</p>
							</c:if>
							
							<div class="description">
								<c:if test="${fn:length(idea.ideaContentPublic.description) gt 0}">
									<p>
										${idea.ideaContentPublic.description}
									</p>
								</c:if>
							</div>
						</aui:column>
						<aui:column last="true" columnWidth="40" cssClass="idea-meta">
							<h2><span>Dokument</span></h2>
							<ul class="documents-list">
								<li class="pdf">
									<a href="#">Ett dokument h&auml;r som beskriver n&aring;got</a>
								</li>
								<li class="doc">
									<a href="#">H&auml;r &auml;r ett annat dokument</a>
								</li>
								<li class="img">
									<a href="#">Sen en bild p&aring; n&aring;got</a>
								</li>
								<li class="img">
									<a href="#">Och en bild till h&auml;r</a>
								</li>
							</ul>
						</aui:column>
					</aui:layout>
		
					<%@ include file="comments_public.jsp" %>
				
				</c:when>
				<c:otherwise>
					<div class="portlet-msg-error">
						Oops nu gick n&aring;got fel. Hittade inte den id&eacute; du fr&aring;gade efter.
					</div>
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
</div>

<liferay-util:html-bottom>
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/innovationsslussen-idea.js"></script>
	<script type="text/javascript">
		AUI().ready('aui-base','innovationsslussen-idea', function (A) {
			var innovationsslussenIdea = new A.InnovationsslussenIdea({
				commentsInput: '#<portlet:namespace />comment',
				portletNamespace: '<portlet:namespace />',
				portletNode: '#p_p_id<portlet:namespace />'
			});
			innovationsslussenIdea.render();
		});
	</script>
</liferay-util:html-bottom>