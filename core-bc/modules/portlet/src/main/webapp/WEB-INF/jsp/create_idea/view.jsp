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

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />	

<portlet:actionURL name="submitIdea" var="submitIdeaURL" />

<div class="idea-outer">
	<div class="idea create-idea">
		<div class="idea-inner">
		
			<h1>Skicka in din id&eacute;</h1>
			
			<div class="intro">

				<p>
					Fyll i VGR-ID och beskriv din idé så kontaktar vi dig inom kort.
				</p>
				<p class="element-mandatory-desc">
					F&auml;lt markerade med <span class="element-mandatory">*</span> &auml;r obligatoriska.
				</p>
			</div>

			<c:choose>
				<c:when test="${isSignedIn}">
                    <c:if test="${ideaPermissionChecker.hasPermissionCreateIdeaForOtherUser}">

                        <portlet:actionURL name="loadOtherUser" var="loadOtherUserURL" />

				        <aui:form action="${loadOtherUserURL}" name="createIdeaForOtherUser" cssClass="create-idea-form" method="post">

                            <aui:fieldset>
                                <spring:bind path="idea.ideaPerson.vgrId">
                                    <c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
                                    <c:if test="${status.error}">
                                        <c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
                                    </c:if>
                                    <div class="${elementWrapCssClass} clearfix">
                                        <aui:field-wrapper cssClass="element-field-wrap">
                                            <label for="<portlet:namespace />title">
                                                <span>VGR-Id</span>
                                            </label>
                                            <aui:input name="otherUserVgrId" cssClass="element-field" type="text" label="" value=" ${otherUserVgrId}"/>
                                        </aui:field-wrapper>
                                        <span class="element-field-help">
                                        </span>
                                    </div>
                                </spring:bind>
                                <div class="link-button-wrap">
                                    <aui:button type="submit" value="Hämta uppgifter" />
                                </div>
                            </aui:fieldset>

				        </aui:form>
                    </c:if>

					<aui:form action="${submitIdeaURL}" name="createIdeaForm" cssClass="create-idea-form" method="post">

						<aui:model-context bean="${idea}" model="${ideaClass}" />

						<%--
					    <spring:hasBindErrors name="idea">
					        <h2>Errors</h2>
					        <div class="formerror">
					            <ul>
					            <c:forEach var="error" items="${errors.allErrors}">
					                <li>${error.defaultMessage}</li>
					            </c:forEach>
					            </ul>
					        </div>
					    </spring:hasBindErrors>
					    --%>
					    
						<% // display all errors (both global and all field errors) %>
						<spring:bind path="idea.*">
							<c:if test="${status.error}">
								<div class="portlet-msg-error">
									<c:forEach items="${status.errorMessages}" var="error">
										<div>${error}</div>
									</c:forEach>
								</div>
							</c:if>
						</spring:bind>

						<aui:fieldset label="Ber&auml;tta mer om dig sj&auml;lv">

							<spring:bind path="idea.ideaPerson.name">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
									<aui:field-wrapper cssClass="element-field-wrap">
										<label for="<portlet:namespace />ideaPerson.name">
											<span>Namn</span>
											<span class="element-mandatory">*<span> Obligatorisk</span></span>
										</label>
										<aui:input name="ideaPerson.name" cssClass="element-field" type="text" label="" />
									</aui:field-wrapper>
									<span class="element-field-help">
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaPerson.email">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
									<aui:field-wrapper cssClass="element-field-wrap">
										<label for="<portlet:namespace />ideaPerson.email">
											<span>E-post</span>
											<span class="element-mandatory">*<span> Obligatorisk</span></span>
										</label>
										<aui:input name="ideaPerson.email" cssClass="element-field" type="text" label="" />
									</aui:field-wrapper>
									<span class="element-field-help">
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaPerson.phone">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
									<aui:field-wrapper cssClass="element-field-wrap">
										<label for="<portlet:namespace />ideaPerson.phone">
											<span>Telefon</span>
											<span class="element-mandatory">*<span> Obligatorisk</span></span>
										</label>
										<aui:input name="ideaPerson.phone" cssClass="element-field" type="text" label="" />
									</aui:field-wrapper>
									<span class="element-field-help">
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaPerson.phoneMobile">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
									<aui:field-wrapper cssClass="element-field-wrap">
										<label for="<portlet:namespace />ideaPerson.phoneMobile">
											<span>Mobiltelefon</span>
										</label>
										<aui:input name="ideaPerson.phoneMobile" cssClass="element-field" type="text" label="" />
									</aui:field-wrapper>
									<span class="element-field-help">
									</span>
								</div>
							</spring:bind>

							<%-- Todo - add spring:bind --%>
							<div class="element-wrap">
								<aui:field-wrapper cssClass="element-field-wrap">
									<label for="<portlet:namespace />administrativeUnit">
										<span>F&ouml;rvaltning</span>
									</label>
									<%-- Todo - switch to use model context --%>
									<aui:input cssClass="element-field" type="text" name="ideaPerson.administrativeUnit" label="" />
								</aui:field-wrapper>
								<span class="element-field-help">
								</span>
							</div>

							<spring:bind path="idea.ideaPerson.jobPosition">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
									<aui:field-wrapper cssClass="element-field-wrap">
										<label for="<portlet:namespace />ideaPerson.jobPosition">
											<span>Yrkesroll</span>
										</label>
										<aui:input name="ideaPerson.jobPosition" cssClass="element-field" type="text" label="" />
									</aui:field-wrapper>
									<span class="element-field-help">
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaPerson.vgrId">
								<aui:input name="ideaPerson.vgrId" cssClass="element-field" type="hidden" label="" />
							</spring:bind>


						</aui:fieldset>

						<aui:fieldset label="Ber&auml;tta mer om din id&eacute;">
							<spring:bind path="idea.title">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />title">
							    			<span>Kortfattad titel på din idé</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="title" cssClass="element-field" type="text" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										(Max 200 tecken)
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaContentPrivate.description">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />ideaContentPrivate.description">
							    			<span>Beskriv din idé</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="ideaContentPrivate.description" cssClass="element-field" type="textarea" label ="" placeholder="Tänk på att vi som myndighet lyder under offentlighetsprincipen. Undvik därför att röja sådana uppgifter om din idé som du anser är skyddsvärda. Det är inte i detta skede nödvändigt att du lämnar detaljer om din idé, utan det räcker att du beskriver den översiktligt." />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										(Max 2000 tecken)
									</span>
								</div>
							</spring:bind>

							<spring:bind path="idea.ideaContentPrivate.solvesProblem">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />ideaContentPrivate.solvesProblem">
							    			<span>Vilket/vilka behov löser din idé?</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="ideaContentPrivate.solvesProblem" cssClass="element-field" type="textarea" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										(Max 2000 tecken)
									</span>
								</div>
							</spring:bind>
							
							<spring:bind path="idea.ideaContentPrivate.wantsHelpWith">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />ideaContentPrivate.wantsHelpWith">
							    			<span>Vad beh&ouml;ver du hj&auml;lp med?</span>
                                            <span class="element-mandatory">*<span> Obligatorisk</span></span>
                                        </label>
							    		<aui:input name="ideaContentPrivate.wantsHelpWith" cssClass="element-field" type="textarea" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										(Max 2000 tecken)
									</span>
								</div>
							</spring:bind>
							
						</aui:fieldset>

						<aui:button-row>
							<aui:button type="submit" value="Skicka in din id&eacute; &raquo;" cssClass="rp-button" />
						</aui:button-row>
					
					</aui:form>				
				</c:when>
				<c:otherwise>
					<div class="portlet-msg-error">
						Du m&aring;ste vara inloggad f&ouml;r att f&aring; skicka in en id&eacute;, information om inloggning finns p&aring; sidan Fr&aring;gor och Svar.
					</div>
				</c:otherwise>
			</c:choose>



		</div>
	</div>
</div>
