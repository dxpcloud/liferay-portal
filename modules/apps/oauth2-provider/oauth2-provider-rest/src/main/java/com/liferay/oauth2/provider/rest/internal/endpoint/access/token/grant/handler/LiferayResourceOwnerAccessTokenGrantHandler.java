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

package com.liferay.oauth2.provider.rest.internal.endpoint.access.token.grant.handler;

import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	service = AccessTokenGrantHandler.class
)
public class LiferayResourceOwnerAccessTokenGrantHandler
	extends BaseAccessTokenGrantHandler {

	@Override
	public List<String> getSupportedGrantTypes() {
		AccessTokenGrantHandler accessTokenGrantHandler =
			_getAccessTokenGrantHandler();

		return accessTokenGrantHandler.getSupportedGrantTypes();
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);
	}

	@Override
	protected ServerAccessToken doCreateAccessToken(
		Client client, MultivaluedMap<String, String> params) {

		AccessTokenGrantHandler accessTokenGrantHandler =
			_getAccessTokenGrantHandler();

		return accessTokenGrantHandler.createAccessToken(client, params);
	}

	@Override
	protected boolean hasPermission(
		Client client, MultivaluedMap<String, String> params) {

		String username = params.getFirst("username");

		if (username == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No username parameter was provided");
			}

			return false;
		}

		String password = params.getFirst("password");

		if (password == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No password parameter was provided");
			}

			return false;
		}

		UserSubject userSubject = _resourceOwnerLoginHandler.createSubject(
			client, username, password);

		OAuth2Application oAuth2Application =
			_liferayOAuthDataProvider.resolveOAuth2Application(client);

		return hasCreateTokenPermission(
			GetterUtil.getLong(userSubject.getId()), oAuth2Application);
	}

	@Override
	protected boolean isGrantHandlerEnabled() {
		return _oAuth2ProviderConfiguration.
			allowResourceOwnerPasswordCredentialsGrant();
	}

	private AccessTokenGrantHandler _getAccessTokenGrantHandler() {
		ResourceOwnerGrantHandler resourceOwnerGrantHandler =
			new ResourceOwnerGrantHandler();

		resourceOwnerGrantHandler.setDataProvider(_liferayOAuthDataProvider);
		resourceOwnerGrantHandler.setLoginHandler(_resourceOwnerLoginHandler);

		return resourceOwnerGrantHandler;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayResourceOwnerAccessTokenGrantHandler.class);

	@Reference
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ResourceOwnerLoginHandler _resourceOwnerLoginHandler;

}