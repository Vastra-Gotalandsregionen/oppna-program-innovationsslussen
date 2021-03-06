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

<portlet:defineObjects />
<liferay-theme:defineObjects />

<c:set var="signinPromptMsgInfo" scope="page" value="Information om inloggning finns på sidan Fr&aring;gor och Svar." />

<c:set var="ideaItemCssClass" scope="page" value="" />

<c:choose>
	<c:when test="${idea.phase eq '3' or idea.phase eq '4'}">
		<c:set var="ideaItemCssClass" scope="page" value="active-innovationsslussen" />
	</c:when>
	<c:when test="${idea.phase eq '5' or idea.phase eq '6'}">
		<c:set var="ideaItemCssClass" scope="page" value="done" />
	</c:when>
</c:choose>


<div class="idea-outer ${ideaItemCssClass}">
	<div class="idea ">
		<div class="idea-inner">
		
			<c:choose>
				<c:when test="${not empty idea}">

					<div class="idea-hd clearfix">
					
					<c:if test="${not idea.isPublic()}">
						<div class="portlet-msg-info">
							Denna id&eacute; &auml;r inte publik &auml;nnu. Detta inneb&auml;r att id&eacute;en inte kan ses av andra sajtmedlemmar.
						</div>
					</c:if>

                    <c:if test="${not empty updateFromBariumOutcome}">
                        <c:choose>
                            <c:when test="${updateFromBariumOutcome}">
                                <div class="portlet-msg-info">
                                    H&auml;mtade f&ouml;r&auml;ndringar fr&aring;n Barium.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="portlet-msg-info">
                                    Letade efter &auml;ndringar i data hos barium. Inga funna.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

						<div class="idea-toolbar-wrap">
							<ul class="rp-toolbar clearfix">
							
								<c:if test="${ideaPermissionChecker.hasPermissionViewIdeaPrivate or ideaPermissionChecker.isIdeaOwner}">
									<li class="icon closed">
									
										<liferay-portlet:renderURL var="ideaPrivateUrl">
											<liferay-portlet:param name="showView" value="showIdea" />
											<liferay-portlet:param name="type" value="private" />
											<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
										</liferay-portlet:renderURL>
									
										<a href="${ideaPrivateUrl}">
											<span class="label">Visa st&auml;ngd beskrivning</span>
										</a>
									</li>
								</c:if>
							
								<li class="icon comment">
									<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
									<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; kommentera p&aring; en id&eacute;. ${signinPromptMsgInfo}" />
									
									<c:if test="${ideaPermissionChecker.hasPermissionAddCommentPublic}">
										<c:set var="linkCssClass" scope="page" value="" />
										<c:set var="signinPromptMsg" scope="page" value="" />
									</c:if>
								
									<a class="${linkCssClass}" href="#" data-promptmsg="${signinPromptMsg}">
										<span class="label">Kommentera</span>&nbsp;<span class="amount">(${fn:length(commentsList)})</span>
									</a>
								</li>
								
								<li class="icon like">
									<c:choose>
										<c:when test="${isIdeaUserLiked}">
											
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="removeLikeUrl" scope="page" value="#" />
											<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; sluta gilla en id&eacute;. ${signinPromptMsgInfo}" />
											
											<c:if test="${ideaPermissionChecker.hasPermissionDeleteLike}">
												<c:set var="linkCssClass" scope="page" value="" />
												<c:set var="signinPromptMsg" scope="page" value="" />
												
												<portlet:actionURL name="removeLike" var="removeLikeUrl">
													<portlet:param name="action" value="removeLike" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PUBLIC" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${removeLikeUrl}" data-promptmsg="${signinPromptMsg}">
												<span class="label">Sluta gilla</span>&nbsp;<span class="amount">(${fn:length(idea.likes)})</span>
											</a>
										</c:when>
										<c:otherwise>
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="addLikeUrl" scope="page" value="#" />
											<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; gilla en id&eacute;. ${signinPromptMsgInfo}" />
											
											<c:if test="${ideaPermissionChecker.hasPermissionAddLike}">
												<c:set var="linkCssClass" scope="page" value="" />
												<c:set var="signinPromptMsg" scope="page" value="" />
												
												<portlet:actionURL name="addLike" var="addLikeUrl">
													<portlet:param name="action" value="addLike" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PUBLIC" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${addLikeUrl}" data-promptmsg="${signinPromptMsg}">
												<span class="label">Gilla</span>&nbsp;<span class="amount">(${fn:length(idea.likes)})</span>
											</a>
										</c:otherwise>
									</c:choose>
								</li>
								
								<li class="icon favorite">
									<c:choose>
										<c:when test="${isIdeaUserFavorite}">
										
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="removeFavoriteUrl" scope="page" value="#" />
											<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; ta bort en id&eacute; som favorit. ${signinPromptMsgInfo}" />
											
											<c:if test="${ideaPermissionChecker.hasPermissionDeleteFavorite}">
												<c:set var="linkCssClass" scope="page" value="" />
												<c:set var="signinPromptMsg" scope="page" value="" />
												
												<portlet:actionURL name="removeFavorite" var="removeFavoriteUrl">
													<portlet:param name="action" value="removeFavorite" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PUBLIC" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${removeFavoriteUrl}" data-promptmsg="${signinPromptMsg}">
												<span class="label">Ta bort som favorit</span>&nbsp;<span class="amount">(${fn:length(idea.favorites)})</span>
											</a>
										</c:when>
										<c:otherwise>
										
											<c:set var="linkCssClass" scope="page" value="innovationsslussen-signin-prompt" />
											<c:set var="addFavoriteUrl" scope="page" value="#" />
											<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; l&auml;gga till en id&eacute; som favorit. ${signinPromptMsgInfo}" />
											
											<c:if test="${ideaPermissionChecker.hasPermissionAddFavorite}">
												<c:set var="linkCssClass" scope="page" value="" />
												<c:set var="signinPromptMsg" scope="page" value="" />
												
												<portlet:actionURL name="addFavorite" var="addFavoriteUrl">
													<portlet:param name="action" value="addFavorite" />
													<portlet:param name="urlTitle" value="${idea.urlTitle}" />
													<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PUBLIC" />
												</portlet:actionURL>
											</c:if>
										
											<a class="${linkCssClass}" href="${addFavoriteUrl}" data-promptmsg="${signinPromptMsg}">
												<span class="label">L&auml;gg till som favorit</span>&nbsp;<span class="amount">(${fn:length(idea.favorites)})</span>
											</a>
										</c:otherwise>
									</c:choose>
								</li>
							</ul>
						</div>
						
					</div>
				
					<h1>
						<c:out value="${idea.title}"/>
					</h1>
					
					<div class="idea-creator">
						Skapad av <span class="idea-creator-name">${idea.ideaPerson.name}</span> <span class="idea-create-date"><fmt:formatDate value="${idea.created}" pattern="yyyy-MM-dd"  /></span>
					</div>
					
					<aui:container>
						<aui:col first="true" columnWidth="60" cssClass="idea-content">
							
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
						</aui:col>
						<aui:col last="true" columnWidth="40" cssClass="idea-meta">
              				<%@ include file="document_list.jsp" %>
						</aui:col>
					</aui:container>


		            <c:choose>
                    	<c:when test="${idea.isPublic()}">
					        <%@ include file="comments_public.jsp" %>
					    </c:when>
                        <c:otherwise>
                            <div class="portlet-msg-info">
                                Det går inte att kommentera här innan denna idé blivit publik.
                            </div>
                        </c:otherwise>
                    </c:choose>
				
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