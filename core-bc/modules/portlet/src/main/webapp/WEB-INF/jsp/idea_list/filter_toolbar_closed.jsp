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

<portlet:renderURL var="requestSearchURL" />

<div class="idea-filters-wrap">
	<aui:form action="${requestSearchURL}" name="filterForm" method="post" cssClass="idea-filters">
	   <div class="idea-filters-toolbar clearfix">

           <div class="filter-item clearfix">
               <span class="filter-item-label">
                   Visa:
               </span>
               <span class="filter-item-element">
                    <select id="<portlet:namespace />ideaPhase" name="<portlet:namespace />ideaPhase" class="select-to-dropdown">
                       <option value="1" ${ideaPhase == '1' ? 'selected="selected"' : ''}>Id&eacute;</option>
                       <option value="2" ${ideaPhase == '2' ? 'selected="selected"' : ''}>Mognad</option>
                       <option value="3" ${ideaPhase == '3' ? 'selected="selected"' : ''}>Genomf&ouml;rd</option>
                       <option value="0" ${ideaPhase == '0' ? 'selected="selected"' : ''}>Id&eacute;er i alla faser</option>
                    </select>
               </span>
           </div>

           <div class="filter-item filter-item">
               <span class="filter-item-label">
                   Visa:
               </span>
               <span class="filter-item-element">
                    <select id="<portlet:namespace />ideaVisible" name="<portlet:namespace />ideaVisible" class="select-to-dropdown">
                       <option value="1" ${ideaVisible == '1' ? 'selected="selected"' : ''}>St&auml;ngda id&eacute;er</option>
                       <option value="2" ${ideaVisible == '2' ? 'selected="selected"' : ''}>&Ouml;ppna id&eacute;er</option>
                       <option value="0" ${ideaVisible == '0' ? 'selected="selected"' : ''}>St&auml;ngda och &ouml;ppna id&eacute;er</option>
                    </select>
               </span>
           </div>

           <div class="filter-item filter-item">
               <span class="filter-item-label">
                   Innovationscoach:
               </span>
               <span class="filter-item-element">
                   <select id="<portlet:namespace />transporter" name="<portlet:namespace />transporter" class="select-to-dropdown">
                      <option value="0" ${transporter == '0' ? 'selected="selected"' : ''}>Alla innovationscoacher </option>
                       <c:forEach var="facet" items="${ideaTranspoterFacets}">
                           <option value="${facet}" ${transporter == facet ? 'selected="selected"' : ''}>${facet}</option>
                       </c:forEach>
                   </select>
               </span>
           </div>

          <div class="filter-item filter-item-last">
              <span class="filter-item-label">
                   Ordna efter:
              </span>
              <span class="filter-item-element">
                   <select id="<portlet:namespace />ideaSort" name="<portlet:namespace />ideaSort" class="select-to-dropdown">
                      <option value="0" ${ideaSort == '0' ? 'selected="selected"' : ''}>Senast skapade</option>
                      <option value="1" ${ideaSort == '1' ? 'selected="selected"' : ''}>Mest kommenterade</option>
                      <option value="2" ${ideaSort == '2' ? 'selected="selected"' : ''}>Senast kommenterade</option>
                   </select>
              </span>
          </div>

          <input type="submit" class="grey" value="<liferay-ui:message key="search" />" />

	    </div>
	</aui:form>
</div>

<%--
    Trigger form submit when search filter selects are changed
--%>
<aui:script use="aui-base">
    var filterForm = A.one('#<portlet:namespace />filterForm');
    
    filterForm.delegate('change', function(e) {
        submitForm(filterForm);    
    }, 'select');
    
</aui:script>