<!-- Copyright 2010 VÃ¤stra GÃ¶talandsregionen This library is free software; you can redistribute it and/or modify it
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
			
	            <p class="sub-heading">
	                Skicka in en aff&auml;rs- eller produktid&eacute; till Innovationssluss V&auml;stra G&ouml;taland
	            </p>
			
				<p>
					Har du en l&ouml;sning p&aring; ett praktiskt problem p&aring; din arbetsplats? &Auml;r du anst&auml;lld inom V&auml;stra G&ouml;talandsregionen? D&aring; &auml;r du v&auml;lkommen att skicka in din id&eacute; till oss. Fyll i formul&auml;ret och skicka in, s&aring; blir kontaktad av oss inom kort.
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

                            <aui:fieldset label="L&auml;s in annan anv&auml;ndare;">
                                <spring:bind path="idea.ideaPerson.vgrId">
                                    <c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
                                    <c:if test="${status.error}">
                                        <c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
                                    </c:if>
                                    <div class="${elementWrapCssClass}">
                                        <aui:field-wrapper cssClass="element-field-wrap">
                                            <label for="<portlet:namespace />title">
                                                <span>VGR-Id</span>
                                            </label>
                                            <aui:input name="otherUserVgrId" cssClass="element-field" type="text" label="" value=" ${otherUserVgrId}"/>
                                        </aui:field-wrapper>
                                        <span class="element-field-help">
                                            Det g&aringr att skapa en id&eacute f&ouml;r en annan anv&auml;ndare genom att ange denna anv&auml;ndarens VGR-Id i detta f&auml;ltet.
                                        </span>
                                    </div>
                                </spring:bind>
                                <div class="link-button-wrap">
                                    <aui:button type="submit" value="L&auml;s in anv&auml;ndare" />
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

						<aui:fieldset label="Ber&auml;tta mer om din id&eacute;">
							<spring:bind path="idea.title">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />title">
							    			<span>Titel</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="title" cssClass="element-field" type="text" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										Skriv in f&ouml;rslag p&aring; titel f&ouml;r ditt projekt eller din aff&auml;rsid&eacute; (Max 200 tecken).
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
							    			<span>Beskrivning</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="ideaContentPrivate.description" cssClass="element-field" type="textarea" label ="" />			    		
							    	</aui:field-wrapper>
									<span class="element-field-help">
										Beskriv din id&eacute; kortfattat. Efter att du har skapat idéen kan du gå in på den och ladda upp ytterligare information i form av bilder, skisser och dokument. (Max 2000 tecken).
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
							    			<span>L&ouml;ser behov / problem</span>
							    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
							    		</label>
							    		<aui:input name="ideaContentPrivate.solvesProblem" cssClass="element-field" type="textarea" label="" />			    		
							    	</aui:field-wrapper>
									<span class="element-field-help">
										F&ouml;rklara det behov eller problem som din id&eacute; l&ouml;ser (Max 2000 tecken).
									</span>
								</div>
							</spring:bind>
							
							<spring:bind path="idea.ideaContentPrivate.ideaTested">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />ideaContentPrivate.ideaTested">
							    			<span>Testning av id&eacute;</span>
							    		</label>
							    		<aui:input name="ideaContentPrivate.ideaTested"  cssClass="element-field" type="textarea" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										Har du testat din id&eacute; p&aring; n&aring;got s&auml;tt? Vilka tester har du gjort? Vad visade testerna? (Max 2000 tecken).
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
							    		</label>
							    		<aui:input name="ideaContentPrivate.wantsHelpWith" cssClass="element-field" type="textarea" label="" />			    		
							    	</aui:field-wrapper>
									<span class="element-field-help">
										Beskriv hur du tror att Innovationsslussen kan hj&auml;lpa dig att komma vidare med din id&eacute; (Max 2000 tecken).
									</span>
								</div>
							</spring:bind>
							
						</aui:fieldset>
						
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
										Skriv in ditt f&ouml;r- och efternamn
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
										Skriv in din e-postadress
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
										Skriv in ditt telefonnummer
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
										Skriv in ditt mobiltelefonnummer
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
						    		<aui:input cssClass="element-field" type="text" name="ideaPerson.administrativeUnit" value="foo" label="" />
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in vilken f&ouml;rvaltning du arbetar p&aring;
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
										Skriv in vilken yrkesroll du har
									</span>
								</div>
							</spring:bind>
							
							<spring:bind path="idea.ideaContentPrivate.additionalIdeaOriginators">
								<c:set var="elementWrapCssClass" scope="page" value="element-wrap" />
								<c:if test="${status.error}">
									<c:set var="elementWrapCssClass" scope="page" value="element-wrap element-has-errors" />
								</c:if>
								<div class="${elementWrapCssClass}">
							    	<aui:field-wrapper cssClass="element-field-wrap">
							    		<label for="<portlet:namespace />ideaPerson.additionalPersonInfo">
							    			<span>Fler id&eacute;givare</span>
							    		</label>
							    		<aui:input name="idea.ideaContentPrivate.additionalIdeaOriginators" cssClass="element-field" type="textarea" label="" />
							    	</aui:field-wrapper>
									<span class="element-field-help">
										&Auml;r ni fler id&eacute;givare? Fyll g&auml;rna i uppgifter om dessa personer i s&aring;dana fall. (Max 800 tecken).
									</span>
								</div>
							</spring:bind>

                            <spring:bind path="idea.ideaPerson.vgrId">
                                <aui:input name="ideaPerson.vgrId" cssClass="element-field" type="hidden" label="" />
                            </spring:bind>
							
							
						</aui:fieldset>
						
						<aui:button-row>
							<aui:button type="submit" value="Skicka in din id&eacute; &raquo;" cssClass="rp-button" />
						</aui:button-row>
					
					</aui:form>				
				</c:when>
				<c:otherwise>
					<div class="portlet-msg-error">
						Du m&aring;ste vara inloggad f&ouml;r att f&aring; skapa en id&eacute;.
					</div>
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
</div>