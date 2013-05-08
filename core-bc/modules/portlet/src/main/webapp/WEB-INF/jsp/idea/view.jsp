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

<%-- Should be moved out of jsp --%>
<liferay-util:html-bottom>
	<script type="text/javascript">

		AUI().ready('aui-tooltip', function(A) {

			var portletNode = A.one('#p_p_id' + '<portlet:namespace />');

			if(portletNode) {
				var flowNodes = portletNode.all('.idea-flow-list li');

				new A.Tooltip({
			         trigger: flowNodes,
			         align: { points: [ 'tc', 'bc' ] },
			         cssClass: 'rp-tooltip',
			         showArrow: false,
			         title: true
			 	}).render();

				
			}
			
		});
	
	</script>
</liferay-util:html-bottom>

<div class="idea-outer">
	<div class="idea">
		<div class="idea-inner">
			
			<div class="idea-hd clearfix">
			
				<div class="idea-toolbar-wrap">
					<ul class="rp-toolbar clearfix">
						<li class="icon comment">
							<a href="#">
								<span>Kommentera (7)</span>
							</a>
						</li>
						<li class="icon like">
							<a href="#">
								<span>Gilla (10)</span>
							</a>
						</li>
						<li class="icon favourite last">
							<a href="#">
								<span>L&auml;gg till som favorit (4)</span>
							</a>
						</li>
						
					</ul>
				</div>
				
				<div class="idea-flow-wrap">
					<ul class="idea-flow-list">
						<li class="done" title="This is step 1. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>1</span>
						</li>
						<li class="done" title="This is step 2. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>2</span>
						</li>
						<li title="This is step 3. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>3</span>
						</li>
						<li title="This is step 4. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>4</span>
						</li>
						<li title="This is step 5. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>5</span>
						</li>
						<li title="This is step 6. Lorem ipsum dolor sit amet, consectetur adipiscing elit.">
							<span>6</span>
						</li>
					</ul>
				</div>
			
			</div>
		
			<h1>Lorem ipsum dolarem sit amet</h1>
			
			<aui:layout>
				<aui:column first="true" columnWidth="60" cssClass="idea-content">
					<p class="intro">
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta ante ut tortor rutrum facilisis. Sed varius sodales tellus, vel dictum risus imperdiet eu.
					</p>
					<div class="description">
						<p>
							Donec posuere enim ut lectus semper feugiat.Nunc libero velit, venenatis et gravida ac, feugiat a lacus. Integer eget mauris in purus aliquam volutpat. Mauris dictum tortor sit amet lacus tincidunt pretium eu sed nibh. Donec est ligula, ultrices eget porttitor vitae, bibendum ac orci. Nulla hendrerit, dui rhoncus vulputate feugiat, diam nisl scelerisque mi, in adipiscing odio quam vel magna...
						</p>
					</div>
				</aui:column>
				<aui:column last="true" columnWidth="40" cssClass="idea-meta">
					<h2><span>Dokument</span></h2>
					<ul class="documents-list">
						<li class="pdf">
							<a href="#">Ett dokument h&auml;r som beskriver n&aring;got</a>
						</li>
						<li class="doc">
							<a href="#">H&auml;r &auml;r ett annat dokument</a>
						</li>
						<li class="img">
							<a href="#">Sen en bild p&aring; n&aring;got</a>
						</li>
						<li class="img">
							<a href="#">Och en bild till h&auml;r</a>
						</li>
					</ul>
				</aui:column>
			</aui:layout>

			<%@ include file="comments.jsp" %>
			
		</div>
	</div>
</div>