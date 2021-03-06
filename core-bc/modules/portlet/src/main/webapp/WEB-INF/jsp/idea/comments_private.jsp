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

<div class="idea-comments">

	<h2>Kommentarer</h2>

	<portlet:actionURL name="addComment" var="addCommentUrl">
		<portlet:param name="action" value="addComment" />
		<portlet:param name="urlTitle" value="${idea.urlTitle}" />
		<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PRIVATE" />
	</portlet:actionURL>
	
	<div class="add-comment">
		<aui:form action="${addCommentUrl}" cssClass="add-comment-form clearfix" method="POST">
			
			<div class="field-wrap">
				<label for="<portlet:namespace />comment">
					L&auml;gg till din kommentar
				</label>
				<div class="field-element-wrap">
					<textarea name="<portlet:namespace />comment" id="<portlet:namespace />comment"></textarea>
				</div>
			</div>
			<aui:button-row>
				<aui:button type="submit" value="Posta" cssClass="rp-button" />
			</aui:button-row>
		</aui:form>
	</div>


    <aui:input name="commentsDelta" type="hidden" value="${commentsDelta}" />

	<c:choose>
		<c:when test="${not empty commentsList}">
			<c:forEach items="${commentsList}" var="comment" varStatus="status">
				
				<c:set var="commentItemCssClass" scope="page" value="comment" />
				<c:if test="${comment.userPrioCouncilMember or comment.userInnovationsslussenEmployee}">
					<c:set var="commentItemCssClass" scope="page" value="comment comment-innovationsslussen" />
				</c:if>

				<div class="${commentItemCssClass} clearfix">
					<div class="comment-author">
						<div class="comment-author-name">
							<c:out value="${comment.name}"/>
						</div>
						<div class="comment-author-title">
							<c:set var="commentUserLabel" scope="page" value="Sajtmedlem" />
							<c:choose>
								<c:when test="${comment.userCreator}">
									<c:set var="commentUserLabel" scope="page" value="Id&eacute;givare" />
								</c:when>
								<c:when test="${comment.userPrioCouncilMember}">
									<c:set var="commentUserLabel" scope="page" value="Prioriteringsr&aring;d" />
								</c:when>
								<c:when test="${comment.userInnovationsslussenEmployee}">
									<c:set var="commentUserLabel" scope="page" value="Innovationsslussen" />
								</c:when>
								<c:when test="${comment.userIdeaTransporter}">
									<c:set var="commentUserLabel" scope="page" value="Innovationscoach" />
								</c:when>
							</c:choose>
							${commentUserLabel}
						</div>
					</div>
					<div class="comment-entry">
						<div class="comment-entry-date">
							<fmt:formatDate pattern="yyyy-MM-dd" value="${comment.createDate}" /> kl. <fmt:formatDate type="time" timeStyle="short" value="${comment.createDate}" /> 
							<% 
							//2013-04-23 kl. 12.15
							%>
						</div>
						<div class="comment-entry-text">
              				${vgrutil:escapeHtmlWithLineBreaks(comment.commentText)}
						</div>
					</div>
					<div class="comment-controls">
           				<c:if test="${ideaPermissionChecker.hasPermissionDeleteCommentPrivate || userId eq comment.userId}">
							<portlet:actionURL name="deleteComment" var="deleteCommentUrl">
								<portlet:param name="action" value="deleteComment" />
								<portlet:param name="urlTitle" value="${idea.urlTitle}" />
								<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PRIVATE" />
								<portlet:param name="commentId" value="${comment.id}" />
							</portlet:actionURL>
            				<a class="requires-confirmation comment-control comment-control-delete" href="${deleteCommentUrl}" title="Ta bort kommentar" data-confirm-msg="&Auml;r du s&auml;ker p&aring; att du vill ta bort kommentaren?">Ta bort kommentar</a>
           				</c:if>
					</div>
				</div>
			</c:forEach>
		</c:when>

	</c:choose>
    <%@ include file="comments_pagination.jsp" %>
</div>