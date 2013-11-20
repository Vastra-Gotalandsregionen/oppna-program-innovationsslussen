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

<div class="idea-filters-wrap">
	<div class="idea-filters">
	   <div class="idea-filters-toolbar clearfix">
	       <div class="filter-item clearfix">
	           <span class="filter-item-label">
	               Visa:
               </span>
	           <span class="filter-item-element">
	                <select id="<portlet:namespace />filterBy" class="select-to-dropdown_">
	                   <option value="0_">Alla id&eacute;er</option>
	                   <option value="1">Id&eacute;</option>
	                   <option value="2">Mognad</option>
	                   <option value="3">Genomf&ouml;rd</option>
	                </select>
               </span>
	       </div>
           <div class="filter-item">
               <span class="filter-item-label">
                    Ordna efter:
               </span>
               <span class="filter-item-element">
                    <select id="<portlet:namespace />sortBy" class="select-to-dropdown_">
                       <option value="0">Senast skapade</option>
                       <option value="1">Mest kommenterade</option>
                       <option value="2">Senast kommenterade</option>
                       <option value="3">Mest gillade</option>
                    </select>
               </span>
           </div>
	    </div>
	</div>
</div>