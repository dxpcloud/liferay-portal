<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
DDMStructure structure = (DDMStructure)request.getAttribute(DDMWebKeys.DYNAMIC_DATA_MAPPING_STRUCTURE);

DDMStructureVersion structureVersion = structure.getStructureVersion();

long classNameId = PortalUtil.getClassNameId(DDMStructure.class);
long classPK = BeanParamUtil.getLong(structure, request, "structureId");
%>

<portlet:actionURL name="/dynamic_data_mapping/copy_structure" var="copyStructureURL">
	<portlet:param name="mvcPath" value="/copy_structure.jsp" />
</portlet:actionURL>

<aui:form action="<%= copyStructureURL %>" cssClass="container-fluid container-fluid-max-xl" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= ddmDisplay.getViewTemplatesBackURL(liferayPortletRequest, liferayPortletResponse, classPK) %>" />
	<aui:input name="classNameId" type="hidden" value="<%= String.valueOf(classNameId) %>" />
	<aui:input name="classPK" type="hidden" value="<%= String.valueOf(classPK) %>" />
	<aui:input name="resourceClassNameId" type="hidden" value="<%= String.valueOf(scopeClassNameId) %>" />
	<aui:input name="status" type="hidden" value="<%= structureVersion.getStatus() %>" />

	<liferay-ui:error exception="<%= StructureNameException.class %>" message="please-enter-a-valid-name" />

	<c:if test='<%= ParamUtil.getBoolean(request, "showBackURL", true) %>'>

		<%
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(ddmDisplay.getViewTemplatesBackURL(liferayPortletRequest, liferayPortletResponse, classPK));

		renderResponse.setTitle(LanguageUtil.format(request, "copy-x", ddmDisplay.getStructureName(locale), false));
		%>

	</c:if>

	<aui:model-context bean="<%= structure %>" model="<%= DDMStructure.class %>" />

	<div class="sheet">
		<div class="panel-group panel-group-flush">
			<aui:fieldset>
				<aui:input name="name" />

				<aui:input name="description" />

				<c:if test="<%= Validator.isNull(templateTypeValue) || templateTypeValue.equals(DDMTemplateConstants.TEMPLATE_TYPE_FORM) %>">
					<aui:input checked='<%= ParamUtil.getBoolean(request, "copyFormTemplates") %>' label='<%= Validator.isNull(templateTypeValue) ? "copy-form-templates" : "copy-templates" %>' name="copyFormTemplates" type="checkbox" />
				</c:if>

				<c:if test="<%= Validator.isNull(templateTypeValue) || templateTypeValue.equals(DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY) %>">
					<aui:input checked='<%= ParamUtil.getBoolean(request, "copyDisplayTemplates") %>' label='<%= Validator.isNull(templateTypeValue) ? "copy-display-templates" : "copy-templates" %>' name="copyDisplayTemplates" type="checkbox" />
				</c:if>
			</aui:fieldset>
		</div>
	</div>

	<aui:button-row>
		<aui:button type="submit" value="copy" />

		<aui:button href="<%= ddmDisplay.getViewTemplatesBackURL(liferayPortletRequest, liferayPortletResponse, classPK) %>" type="cancel" />
	</aui:button-row>
</aui:form>