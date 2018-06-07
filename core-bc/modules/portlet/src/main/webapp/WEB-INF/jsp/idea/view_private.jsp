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
<%@ taglib uri="http://portalen.vgregion.se/util" prefix="vgrutil" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<div class="idea-outer idea-outer-private">
	<div class="idea">
		<div class="idea-inner">

			<c:choose>
				<c:when test="${not empty idea}">

					<div class="idea-hd clearfix">

					<c:if test="${not idea.isPublic()}">
						<div class="portlet-msg-info">
							Informationen är inte offentlig, utan kan endast läsas och kommenteras av behörig personal.
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
								<c:if test="${ideaPermissionChecker.hasPermissionAddCommentPrivate}">
									<li class="icon comment">
										<a class="" href="#">
											<span class="label">Kommentera</span>&nbsp;<span class="amount">(${fn:length(commentsList)})</span>
										</a>
									</li>
								</c:if>
							</ul>
							<ul class="rp-toolbar clearfix">
								<%--<c:if test="${ideaPermissionChecker.hasPermissionViewInBarium}">
									<li class="icon barium">
										<a href="${idea.bariumUrl}" target="_BLANK">
										  <span class="label">Visa i Barium</span>
									  </a>
									</li>
								</c:if>--%>

								<c:if test="${ideaPermissionChecker.hasPermissionUpdateFromBarium}">
									<li class="icon reload">
										<portlet:actionURL name="updateFromBarium" var="updateFromBariumUrl">
											<portlet:param name="action" value="updateFromBarium" />
											<portlet:param name="urlTitle" value="${idea.urlTitle}" />
											<portlet:param name="id" value="${idea.id}" />
											<portlet:param name="ideaContentType" value="IDEA_CONTENT_TYPE_PRIVATE" />
										</portlet:actionURL>
										<a href="${updateFromBariumUrl}">
										  <span class="label">Uppdatera fr&aring;n Barium</span>
									   </a>
									</li>
								</c:if>
							</ul>
						</div>



						<div class="idea-flow-wrap">
							<ul class="idea-flow-list">
								<li class="${idea.phase >= 1 ? 'done' : ''}" title="<div><strong><liferay-ui:message key='innovationsslussen.idea.tooltip.step-1.title' /></strong></div><%--<liferay-ui:message key='innovationsslussen.idea.tooltip.step-1.content' />--%>">
									<span>1</span>
								</li>
								<li class="${idea.phase >= 2 ? 'done' : ''}" title="<div><strong><liferay-ui:message key='innovationsslussen.idea.tooltip.step-2.title' /></strong></div><%--<liferay-ui:message key='innovationsslussen.idea.tooltip.step-2.content' />--%>">
									<span>2</span>
								</li>
								<li class="${idea.phase >= 3 ? 'done' : ''}" title="<div><strong><liferay-ui:message key='innovationsslussen.idea.tooltip.step-3.title' /></strong></div><%--<liferay-ui:message key='innovationsslussen.idea.tooltip.step-3.content' />--%>">
									<span>3</span>
								</li>
								<li class="${idea.phase >= 4 ? 'done' : ''}" title="<div><strong><liferay-ui:message key='innovationsslussen.idea.tooltip.step-4.title' /></strong></div><%--<liferay-ui:message key='innovationsslussen.idea.tooltip.step-4.content' />--%>">
									<span>4</span>
								</li>
								<li class="${idea.phase >= 5 ? 'done' : ''}" title="<div><strong><liferay-ui:message key='innovationsslussen.idea.tooltip.step-5.title' /></strong></div><%--<liferay-ui:message key='innovationsslussen.idea.tooltip.step-5.content' />--%>">
									<span>5</span>
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

					<c:if test="${not empty tansportor}">
                        <div class="idea-creator">
                            Id&eacute;transport&ouml;r <span class="idea-creator-name">${tansportor}</span>
                        </div>
                    </c:if>

					<aui:layout>
						<aui:column first="true" columnWidth="60" cssClass="idea-content">

							<div class="idea-content-item">
								<div class="label">
									Beskrivning
								</div>
								<p>
									${idea.ideaContentPrivate.description}
								</p>
							</div>
							<div class="idea-content-item">
								<div class="label">
									L&ouml;ser behov
								</div>
								<p>
									${idea.ideaContentPrivate.solvesProblem}
								</p>
							</div>
							<div class="idea-content-item">
								<div class="label">
									Beh&ouml;ver hj&auml;lp med
								</div>
								<p>
									${idea.ideaContentPrivate.wantsHelpWith}
								</p>
							</div>
							<div class="idea-content-item">
								<div class="label">
									Har du testat din id&eacute;?
								</div>
								<p>
									${idea.ideaContentPrivate.ideaTested}
								</p>
							</div>
							<div class="idea-content-item">
								<div class="label">
								    &Auml;r ni fler id&eacute;givare?
								</div>
								<p>
									${idea.ideaContentPrivate.additionalIdeaOriginators}
								</p>
							</div>
						</aui:column>
						<aui:column last="true" columnWidth="40" cssClass="idea-meta">
              				<%@ include file="document_list.jsp" %>
						</aui:column>
					</aui:layout>

					<%@ include file="comments_private.jsp" %>

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

	   // Add css class to document body
	   document.body.className += ' ' + 'idea-private-view';

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