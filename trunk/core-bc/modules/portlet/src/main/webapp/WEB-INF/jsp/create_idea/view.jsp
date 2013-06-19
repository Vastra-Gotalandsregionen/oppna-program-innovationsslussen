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

<portlet:actionURL name="submitIdea" var="submitIdeaURL" />

<div class="idea-outer">
	<div class="idea create-idea">
		<div class="idea-inner">
			
			<h1>Skicka in din id&eacute;</h1>
			<p class="sub-heading">
				Skicka in en aff&auml;rs- eller produktid&eacute; till Innovationssluss V&auml;stra G&ouml;taland
			</p>
			
			<div class="intro">
				<p>
					Har du en l&ouml;sning p&aring; ett praktiskt problem p&aring; din arbetsplats? &Auml;r du anst&auml;lld inom V&auml;stra G&ouml;talandsregionen? D&aring; &auml;r du v&auml;lkommen att skicka in din id&eacute; till oss. Fyll i formul&auml;ret och skicka in, s&aring; blir kontaktad av oss inom kort.
				</p>
				<p class="element-mandatory-desc">
					F&auml;lt markerade med <span class="element-mandatory">*</span> &auml;r obligatoriska.
				</p>
			</div>
			
			<c:choose>
				<c:when test="${isSignedIn}">
					<aui:form action="${submitIdeaURL}" name="createIdeaForm" cssClass="create-idea-form" method="post">
					
						<aui:fieldset label="Ber&auml;tta mer om din id&eacute;">
		
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />title">
						    			<span>Titel</span>
						    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="title" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in f&ouml;rslag p&aring; titel f&ouml;r ditt projekt eller din aff&auml;rsid&eacute;
								</span>
							</div>				
						
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />description">
						    			<span>Beskrivning</span>
						    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
						    		</label>
						    		<c:set var="testDescription_1" scope="page" value="" />
						    		<c:set var="testDescription_2" scope="page" value="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis eget nisl erat, sed convallis urna. Integer id nibh quis quam semper rhoncus in vel erat. In risus enim, pretium ut pretium ut, viverra ac nunc. Nunc sed risus quam, at gravida est. Quisque eget ipsum urna. Maecenas ac dui libero, et sagittis nibh. Proin eu venenatis sapien." />
						    		<c:set var="testDescription_3" scope="page" value="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis eget nisl erat, sed convallis urna. Integer id nibh quis quam semper rhoncus in vel erat. In risus enim, pretium ut pretium ut, viverra ac nunc. Nunc sed risus quam, at gravida est. Quisque eget ipsum urna. Maecenas ac dui libero, et sagittis nibh. Proin eu venenatis sapien. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis eget nisl erat, sed convallis urna. Integer id nibh quis quam semper rhoncus in vel erat. In risus enim, pretium ut pretium ut, viverra ac nunc. Nunc sed risus quam, at gravida est. Quisque eget ipsum urna. Maecenas ac dui libero, et sagittis nibh. Proin eu venenatis sapien. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis eget nisl erat, sed convallis urna. Integer id nibh quis quam semper rhoncus in vel erat. In risus enim, pretium ut pretium ut, viverra ac nunc. Nunc sed risus quam, at gravida est. Quisque eget ipsum urna. Maecenas ac dui libero, et sagittis nibh. Proin eu venenatis sapien." />
						    		<aui:input cssClass="element-field" type="textarea" name="description" value="${testDescription_1}" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Beskriv din id&eacute; kortfattat
								</span>
							</div>
		
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />solvesProblem">
						    			<span>L&ouml;ser behov / problem</span>
						    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
						    		</label>
						    		<aui:input cssClass="element-field" type="textarea" name="solvesProblem" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									F&ouml;rklara det behov eller problem som din id&eacute; l&ouml;ser
								</span>
							</div>
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />ideaTested">
						    			<span>Testning av id&eacute;</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="textarea" name="ideaTested" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Har du testat din id&eacute; p&aring; n&aring;got s&auml;tt? Vilka tester har du gjort? Vad visade testerna?
								</span>
							</div>
		
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />wantsHelpWith">
						    			<span>Vad beh&ouml;ver du hj&auml;lp med?</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="textarea" name="wantsHelpWith" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Beskriv hur du tror att Innovationsslussen kan hj&auml;lpa dig att komma vidare med din id&eacute;
								</span>
							</div>
							
						</aui:fieldset>
						
						<aui:fieldset label="Ber&auml;tta mer om dig sj&auml;lv">
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />name">
						    			<span>Namn</span>
						    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="name" value="Erik Andersson" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in ditt f&ouml;r- och efternamn
								</span>
							</div>				
		
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />email">
						    			<span>E-post</span>
						    			<span class="element-mandatory">*<span> Obligatorisk</span></span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="email" value="erik.andersson@monator.com" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in din e-postadress
								</span>
							</div>
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />phone">
						    			<span>Telefon</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="phone" value="0311234567" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in ditt telefonnummer
								</span>
							</div>
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />phoneMobile">
						    			<span>Mobiltelefon</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="phoneMobile" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in ditt mobiltelefonnummer
								</span>
							</div>				
											
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />administrativeUnit">
						    			<span>F&ouml;rvaltning</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="administrativeUnit" value="Monator" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in vilken f&ouml;rvaltning du arbetar p&aring;
								</span>
							</div>
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />jobPosition">
						    			<span>Yrkesroll</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="text" name="jobPosition" value="Konsult" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									Skriv in vilken yrkesroll du har
								</span>
							</div>
							
							<div class="element-wrap">
						    	<aui:field-wrapper cssClass="element-field-wrap">
						    		<label for="<portlet:namespace />additionalPersonInfo">
						    			<span>Fler id&eacute;givare</span>
						    		</label>
						    		<aui:input cssClass="element-field" type="textarea" name="additionalPersonInfo" value="" label ="" />			    		
						    	</aui:field-wrapper>
								<span class="element-field-help">
									&Auml;r ni fler id&eacute;givare? Fyll g&auml;rna i uppgifter om dessa personer i s&aring;dana fall.
								</span>
							</div>
							
							
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