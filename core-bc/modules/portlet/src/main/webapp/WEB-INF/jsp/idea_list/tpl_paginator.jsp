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

<c:if test="${not empty pageIterator}">
	<c:if test="${pageIterator.showPaginator}">
		<div class="paginator-wrap">
		
			<div class="rp-paging clearfix">
				<ul>
				
					<c:if test="${pageIterator.showFirst}">
						<li class="first">
							<liferay-portlet:renderURL portletMode="VIEW" var="firstUrl" windowState="normal">
								<portlet:param name="renderType" value="isPaginatorCall" />
								<portlet:param name="pageNumber" value="1" />
								<portlet:param name="ideaPhase" value="${ideaPhase}" />
                                <portlet:param name="ideaSort" value="${ideaSort}" />
                                <portlet:param name="ideaVisible" value="${ideaVisible}" />
								<portlet:param name="transporter" value="${transporter}" />
							</liferay-portlet:renderURL>
							<a href="${firstUrl}" title="F&ouml;rsta">F&ouml;rsta</a>
						</li>
					</c:if>
				
					<c:if test="${pageIterator.showPrevious}">
						<li class="previous">
							<liferay-portlet:renderURL portletMode="VIEW" var="previousUrl" windowState="normal">
								<portlet:param name="renderType" value="isPaginatorCall" />
								<portlet:param name="pageNumber" value="${pageIterator.previous}" />
								<portlet:param name="ideaPhase" value="${ideaPhase}" />
                                <portlet:param name="ideaSort" value="${ideaSort}" />
                                <portlet:param name="ideaVisible" value="${ideaVisible}" />
								<portlet:param name="transporter" value="${transporter}" />
							</liferay-portlet:renderURL>
							<a href="${previousUrl}" title="F&ouml;reg&aring;ende">F&ouml;reg&aring;ende</a>
						</li>
						<li class="sep">
							...
						</li>
					</c:if>
					
					<c:forEach var="page" items="${pageIterator.pages}">
						
						<c:set var="pageItemCssClass" scope="page" value="" />
						
						<c:if test="${page.selected}">
							<c:set var="pageItemCssClass" scope="page" value="current" />
						</c:if>
						<li class="${pageItemCssClass}">
							<c:choose>
								<c:when test="${page.selected}">
									<span>${page.pagenumber}</span>
								</c:when>
								<c:otherwise>
									<liferay-portlet:renderURL portletMode="VIEW" var="url" windowState="normal">
										<portlet:param name="renderType" value="isPaginatorCall" />
										<portlet:param name="pageNumber" value="${page.pagenumber}" />
										<portlet:param name="ideaPhase" value="${ideaPhase}" />
										<portlet:param name="ideaSort" value="${ideaSort}" />
										<portlet:param name="ideaVisible" value="${ideaVisible}" />
										<portlet:param name="transporter" value="${transporter}" />
									</liferay-portlet:renderURL>
									<a href="${url}">${page.pagenumber}</a>
								</c:otherwise>
							</c:choose>
						</li>
					</c:forEach>
					
					<c:if test="${pageIterator.showNext}">
						<li class="sep">
							...
						</li>
						<li class="next">
							<liferay-portlet:renderURL portletMode="VIEW" var="nextUrl" windowState="normal">
								<portlet:param name="renderType" value="isPaginatorCall" />
								<portlet:param name="pageNumber" value="${pageIterator.next}" />
								<portlet:param name="ideaPhase" value="${ideaPhase}" />
                                <portlet:param name="ideaSort" value="${ideaSort}" />
                                <portlet:param name="ideaVisible" value="${ideaVisible}" />
								<portlet:param name="transporter" value="${transporter}" />
							</liferay-portlet:renderURL>
							<a href="${nextUrl}" title="N&auml;sta">N&auml;sta</a>
						</li>
					</c:if>
					
					<c:if test="${pageIterator.showLast}">
						<li class="last">
							<liferay-portlet:renderURL portletMode="VIEW" var="lastUrl" windowState="normal">
								<portlet:param name="renderType" value="isPaginatorCall" />
								<portlet:param name="pageNumber" value="${pageIterator.last}" />
								<portlet:param name="ideaPhase" value="${ideaPhase}" />
                                <portlet:param name="ideaSort" value="${ideaSort}" />
                                <portlet:param name="ideaVisible" value="${ideaVisible}" />
								<portlet:param name="transporter" value="${transporter}" />
							</liferay-portlet:renderURL>
							<a href="${lastUrl}" title="Sista">Sista</a>
						</li>
					</c:if>
					
					
				<ul>
			</div>
			<c:if test="${pageIterator.showSummary}">
				<p class="search-info">Visar tr&auml;ff ${pageIterator.currentHitsStart} till ${pageIterator.currentHitsEnd} av totalt ${pageIterator.totalHits} tr&auml;ffar.</p>
			</c:if>
		
		</div>	
	</c:if>	
</c:if>	