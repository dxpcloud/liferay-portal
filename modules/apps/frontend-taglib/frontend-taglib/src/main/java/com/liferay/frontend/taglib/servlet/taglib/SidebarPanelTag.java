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

package com.liferay.frontend.taglib.servlet.taglib;

import com.liferay.frontend.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * @author Carlos Lancha
 */
public class SidebarPanelTag extends IncludeTag {

	@Override
	public int doEndTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		HttpServletRequest httpServletRequest = getRequest();

		setNamespacedAttribute(
			httpServletRequest, "searchContainerId", _searchContainerId);
		setNamespacedAttribute(httpServletRequest, "resourceURL", _resourceURL);

		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		setNamespacedAttribute(getRequest(), "closeButton", _closeButton);
		setNamespacedAttribute(getRequest(), "title", _title);

		super.doStartTag();

		return EVAL_BODY_INCLUDE;
	}

	public boolean getCloseButton() {
		return _closeButton;
	}

	public String getResourceURL() {
		return _resourceURL;
	}

	public String getSearchContainerId() {
		return _searchContainerId;
	}

	public String getTitle() {
		return _title;
	}

	public void setCloseButton(boolean closeButton) {
		_closeButton = closeButton;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		setServletContext(ServletContextUtil.getServletContext());
	}

	public void setResourceURL(String resourceURL) {
		_resourceURL = resourceURL;
	}

	public void setSearchContainerId(String searchContainerId) {
		_searchContainerId = searchContainerId;
	}

	public void setTitle(String title) {
		_title = title;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_closeButton = true;
		_resourceURL = null;
		_searchContainerId = null;
		_title = null;
	}

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	private static final String _ATTRIBUTE_NAMESPACE =
		"liferay-frontend:sidebar-panel:";

	private static final String _END_PAGE = "/sidebar_panel/end.jsp";

	private static final String _START_PAGE = "/sidebar_panel/start.jsp";

	private boolean _closeButton = true;
	private String _resourceURL;
	private String _searchContainerId;
	private String _title;

}