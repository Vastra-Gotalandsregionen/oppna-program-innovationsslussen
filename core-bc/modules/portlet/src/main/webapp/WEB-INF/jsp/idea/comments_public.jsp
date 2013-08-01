<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<div class="idea-comments">

	
	<div class="add-comment">
	
		<c:set var="textareaCssClass" scope="page" value="innovationsslussen-signin-prompt" />
		<c:set var="addCommentUrl" scope="page" value="#" />
		<c:set var="signinPromptMsg" scope="page" value="Du m&aring;ste vara inloggad f&ouml;r att f&aring; kommentera p&aring; en id&eacute;" />
		
		<c:if test="${ideaPermissionChecker.hasPermissionAddCommentPublic}">
			<c:set var="textareaCssClass" scope="page" value="" />
			<c:set var="signinPromptMsg" scope="page" value="" />
			
			<portlet:actionURL name="addComment" var="addCommentUrl">
				<portlet:param name="action" value="addComment" />
				<portlet:param name="urlTitle" value="${idea.urlTitle}" />
				<portlet:param name="ideaContentType" value="0" />
			</portlet:actionURL>
		</c:if>
	
	
		<aui:form action="${addCommentUrl}" cssClass="add-comment-form clearfix" method="POST">
			<div class="field-wrap">
				<label for="<portlet:namespace />comment">
					L&auml;gg till din kommentar
				</label>
				<div class="field-element-wrap">
					<textarea class="${textareaCssClass}" name="<portlet:namespace />comment" id="<portlet:namespace />comment" data-promptmsg="${signinPromptMsg}"></textarea>
					<p class="notice">
						Notera att denna kommentar kommer att synas &ouml;ppet f&ouml;r alla bes&ouml;kare p&aring; siten.
					</p>	
				</div>
			</div>
			<aui:button-row>
				<aui:button type="submit" value="Posta" cssClass="rp-button" />
			</aui:button-row>
		</aui:form>
	</div>
	
	<c:choose>
		<c:when test="${not empty commentsList}">
			<c:forEach items="${commentsList}" var="comment" varStatus="status">
				<c:set var="commentItemCssClass" scope="page" value="comment" />
				
				<c:if test="${comment.isUserPrioCouncilMember or comment.isUserInnovationsslussenEmployee}">
					<c:set var="commentItemCssClass" scope="page" value="comment comment-innovationsslussen" />
				</c:if>
			
				<div class="${commentItemCssClass} clearfix">
					<div class="comment-author">
						<div class="comment-author-name">
                            <c:out value="${comment.name}"/>
						</div>
						<div class="comment-author-title">
							<c:choose>
								<c:when test="${comment.isUserCreator}">
									Id&eacute;givare
								</c:when>
								<c:when test="${comment.isUserPrioCouncilMember}">
									Prioriteringsr&aring;d
								</c:when>
								<c:when test="${comment.isUserInnovationsslussenEmployee}">
									Innovationsslussen
								</c:when>
								<c:otherwise>
									Sajtmedlem
								</c:otherwise>
							</c:choose>
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
              <c:out value="${comment.commentText}" escapeXml="false"/>
            </div>
					</div>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p>Det finns inga kommentarer p&aring; denna id&eacute; &auml;nnu. <c:if test="${isSignedIn}">Posta din kommentar och bli f&ouml;rst!</c:if></p>
		</c:otherwise>
	</c:choose>
</div>	
	
	<%-- 
	<div class="comment clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Anders Andersson
			</div>
			<div class="comment-author-title">
				Id&eacute;givare
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment comment-alt clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Bengt Bengtsson
			</div>
			<div class="comment-author-title">
				Medlem
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Carl Carlsson
			</div>
			<div class="comment-author-title">
				Medlem
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment comment-alt clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Anders Andersson
			</div>
			<div class="comment-author-title">
				Id&eacute;givare
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	
	
</div>
--%>

<%-- 
<div class="rp-paging clearfix">
	<ul>
		<li class="previous">
			<a class="arrowleft" href="" title="F&ouml;reg&aring;ende">F&ouml;reg&aring;ende</a>
		</li>
		<li>
			<span class="current">1</span>
		</li>
		<li>
			<a href="">2</a>
		</li>
		<li>
			<a href="">3</a>
		</li>
		<li>
			<a href="">4</a>
		</li>
		<li>
			<a href="">5</a>
		</li>
		<li class="next">
			<a class="arrowright" href="" title="N&auml;sta">N&auml;sta</a>
		</li>
	</ul>
</div>			
--%>