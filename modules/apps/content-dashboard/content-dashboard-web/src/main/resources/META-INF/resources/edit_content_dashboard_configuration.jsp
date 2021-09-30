<%-- 
/** * Copyright (c) 2000-present Liferay, Inc. All rights reserved. * *
This library is free software; you can redistribute it and/or modify it under *
the terms of the GNU Lesser General Public License as published by the Free *
Software Foundation; either version 2.1 of the License, or (at your option) *
any later version. * * This library is distributed in the hope that it will be
useful, but WITHOUT * ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS * FOR A PARTICULAR PURPOSE. See the GNU Lesser
General Public License for more * details. */ 
--%> 

<%@ include file="/init.jsp" %>

<%
ContentDashboardAdminConfigurationDisplayContext contentDashboardAdminConfigurationDisplayContext = (ContentDashboardAdminConfigurationDisplayContext)request.getAttribute(ContentDashboardWebKeys.CONTENT_DASHBOARD_ADMIN_CONFIGURATION_DISPLAY_CONTEXT);
%>

<liferay-util:html-top>
	<link href="<%= PortalUtil.getStaticResourceURL(request, application.getContextPath() + "/css/vocabularies_selection.css") %>" rel="stylesheet" type="text/css" />
</liferay-util:html-top>

<liferay-frontend:edit-form
	action="<%= contentDashboardAdminConfigurationDisplayContext.getActionURL() %>"
	method="post"
	name="fm"
	onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "saveConfiguration();" %>'
>
	<aui:input name="redirect" type="hidden" value="<%= contentDashboardAdminConfigurationDisplayContext.getRedirect() %>" />

	<aui:input name="assetVocabularyNames" type="hidden" />

	<liferay-frontend:edit-form-body>
		<c:if test='<%= GetterUtil.getBoolean(SessionMessages.get(renderRequest, "emptyAssetVocabularyNames")) %>'>
			<clay:alert
				dismissible="<%= true %>"
				displayType="warning"
				message="you-have-not-selected-any-vocabularies"
			/>
		</c:if>

		<liferay-frontend:fieldset-group>
			<liferay-frontend:fieldset>
				<aui:field-wrapper>
					<p class="sheet-text">
						<liferay-ui:message key="select-vocabularies-description" />
					</p>

					<%
					List<KeyValuePair> availableVocabularies = new ArrayList<KeyValuePair>();

					availableVocabularies = contentDashboardAdminConfigurationDisplayContext.getAvailableVocabularyNames();

					List<KeyValuePair> currentVocabularies = new ArrayList<KeyValuePair>();

					currentVocabularies = contentDashboardAdminConfigurationDisplayContext.getCurrentVocabularyNames();

					HashMap<String, Object> componentData = new HashMap<String, Object>();

					componentData.put("leftBoxName", "availableAssetVocabularyNames");
					componentData.put("leftList", JSONFactoryUtil.createJSONArray(availableVocabularies));
					componentData.put("rightBoxName", "currentAssetVocabularyNames");
					componentData.put("rightList", JSONFactoryUtil.createJSONArray(currentVocabularies));
					%>
					<div>
						<react:component
							module="js/VocabulariesSelectionBox"
							props="<%= componentData %>"
						/>
					</div>
				</aui:field-wrapper>
			</liferay-frontend:fieldset>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<aui:button type="submit" />

		<aui:button type="cancel" />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>

<aui:script>
	function <portlet:namespace />saveConfiguration() {
		var form = document.<portlet:namespace />fm;
		Liferay.Util.postForm(form, {
			data: {
				assetVocabularyNames: Liferay.Util.listSelect(
					Liferay.Util.getFormElement(form, 'currentAssetVocabularyNames')
				),
			},
		});
	}
</aui:script>