<!-- Copyright 2010 Västra Götalandsregionen This library is free software; you can redistribute it and/or modify it
 under the terms of version 2.1 of the GNU Lesser General Public License as published by the Free Software Foundation.
 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

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

<c:if test="${not empty ideaList}">

    <c:choose>
        <c:when test="${openView}">
            <c:set var="ideaListClass" scope="page" value="idea-list idea-list-open" />
        </c:when>
        <c:otherwise>
            <c:set var="ideaListClass" scope="page" value="idea-list idea-list-closed" />
        </c:otherwise>
    </c:choose>

    <ul class="${ideaListClass}">
        <c:forEach items="${ideaList}" var="idea" varStatus="index">

			<liferay-portlet:renderURL var="ideaUrl" plid="${ideaPlid}" portletName="${ideaPortletName}">
				<liferay-portlet:param name="showView" value="showIdea" />
				<c:if test="${not idea.isPublic()}">
				    <liferay-portlet:param name="type" value="private" />
				</c:if>
                <c:if test="${not openView}">
                    <liferay-portlet:param name="type" value="private" />
                </c:if>
				<liferay-portlet:param name="urlTitle" value="${idea.urlTitle}" />
			</liferay-portlet:renderURL>

			<c:set var="ideaItemCssClass" scope="page" value="" />
			<c:set var="ideaPhaseLabel" scope="page" value="Id&eacute;" />

            <c:choose>
                <c:when test="${idea.phase eq '3' or idea.phase eq '4'}">
                    <c:set var="ideaItemCssClass" scope="page" value="active-innovationsslussen" />
                    <c:set var="ideaPhaseLabel" scope="page" value="Mognad" />
                </c:when>
                <c:when test="${idea.phase eq '5' or idea.phase eq '6'}">
                    <c:set var="ideaItemCssClass" scope="page" value="done" />
                    <c:set var="ideaPhaseLabel" scope="page" value="F&auml;rdig" />
                </c:when>
            </c:choose>

			<li class="${ideaItemCssClass}">
				<div class="idea-item">
					<a href="${ideaUrl}">
						<div class="idea-content">
							<div class="idea-content-1">
								<div class="idea-content-inner">
									<h3>${idea.title}</h3>
								</div>

                                <c:if test="${not openView}">
                                   <ul class="idea-stats idea-stats-state clearfix">
                                        <c:choose>
                                            <c:when test="${idea.isPublic()}">
                                                <li class="first open"/>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="first closed"/>
                                            </c:otherwise>
                                        </c:choose>
							    	</ul>
                                </c:if>

                                <ul class="idea-stats clearfix">
                                    <c:if test="${openView}">
                                        <li class="likes">
                                            ${fn:length(idea.likes)}
                                        </li>
                                    </c:if>
									<li class="comments">
										${idea.commentsCount}
									</li>
								</ul>

							</div>
							<div class="idea-content-2">
								<div class="idea-content-inner">
									<c:choose>
										<c:when test="${idea.isPublic()}">
											<c:choose>
												<c:when test="${not empty idea.ideaContentPublic.intro}">
													${fn:substring(idea.ideaContentPublic.intro, 0, 175)}
												</c:when>
												<c:otherwise>
													${fn:substring(idea.ideaContentPublic.description, 0, 175)}
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											${fn:substring(idea.ideaContentPrivate.description, 0, 175)}
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="idea-label">
							${ideaPhaseLabel}
						</div>
					</a>
				</div>
			</li>
		</c:forEach>
	</ul>
	<liferay-util:include page="/WEB-INF/jsp/idea_list/tpl_paginator.jsp" servletContext="<%= application %>" />
</c:if>