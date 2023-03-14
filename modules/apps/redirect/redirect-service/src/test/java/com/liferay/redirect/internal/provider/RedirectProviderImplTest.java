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

package com.liferay.redirect.internal.provider;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.redirect.model.RedirectPatternEntry;
import com.liferay.redirect.provider.RedirectProvider;
import com.liferay.redirect.service.RedirectEntryLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Adolfo Pérez
 */
public class RedirectProviderImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		_redirectProviderImpl.setRedirectEntryLocalService(
			_redirectEntryLocalService);

		Mockito.when(
			_redirectEntryLocalService.fetchRedirectEntry(
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyBoolean())
		).thenReturn(
			null
		);
	}

	@Test
	public void testControlPanelURLs() {
		_setupRedirectPatternEntries(
			Collections.singletonList(
				new RedirectPatternEntry(
					Pattern.compile("^.*/control_panel/manage"), "xyz",
					StringPool.BLANK)));

		Assert.assertNull(
			_getRedirectProviderRedirect("/control_panel/manage"));

		Mockito.verify(
			_redirectEntryLocalService, Mockito.never()
		).fetchRedirectEntry(
			Mockito.anyLong(), Mockito.anyString(), Mockito.anyBoolean()
		);
	}

	@Test
	public void testEmptyPatterns() {
		_setupRedirectPatternEntries(Collections.emptyList());

		Assert.assertNull(
			_getRedirectProviderRedirect(StringUtil.randomString()));

		_verifyMockInvocations();
	}

	@Test
	public void testFirstReplacementPatternMatches() {
		List<RedirectPatternEntry> redirectPatternEntries = new ArrayList<>();

		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^a(b)c"), "u$1w", StringPool.BLANK));
		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^abc"), "xyz", StringPool.BLANK));

		_setupRedirectPatternEntries(redirectPatternEntries);

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("ubw", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testFirstSimplePatternMatches() {
		List<RedirectPatternEntry> redirectPatternEntries = new ArrayList<>();

		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^abc"), "xyz", StringPool.BLANK));
		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^a(b)c"), "u$1w", StringPool.BLANK));

		_setupRedirectPatternEntries(redirectPatternEntries);

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("xyz", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testLastReplacementPatternMatches() {
		List<RedirectPatternEntry> redirectPatternEntries = new ArrayList<>();

		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^uvw"), "xyz", StringPool.BLANK));
		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^a(b)c"), "u$1w", StringPool.BLANK));

		_setupRedirectPatternEntries(redirectPatternEntries);

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("ubw", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testLastSimplePatternMatches() {
		List<RedirectPatternEntry> redirectPatternEntries = new ArrayList<>();

		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^u(v)w"), "x$1z", StringPool.BLANK));
		redirectPatternEntries.add(
			new RedirectPatternEntry(
				Pattern.compile("^abc"), "123", StringPool.BLANK));

		_setupRedirectPatternEntries(redirectPatternEntries);

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("123", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testRewritePatternSingleMatch() {
		_setupRedirectPatternEntries(
			Collections.singletonList(
				new RedirectPatternEntry(
					Pattern.compile("^a(b)c"), "x$1z", StringPool.BLANK)));

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("xbz", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testRewritePatternSingleMismatch() {
		_setupRedirectPatternEntries(
			Collections.singletonList(
				new RedirectPatternEntry(
					Pattern.compile("^a(b)c"), "x$1z", StringPool.BLANK)));

		Assert.assertNull(_getRedirectProviderRedirect("123"));

		_verifyMockInvocations();
	}

	@Test
	public void testSimplePatternSingleMatch() {
		_setupRedirectPatternEntries(
			Collections.singletonList(
				new RedirectPatternEntry(
					Pattern.compile("^abc"), "xyz", StringPool.BLANK)));

		RedirectProvider.Redirect redirect = _getRedirectProviderRedirect(
			"abc");

		Assert.assertEquals("xyz", redirect.getDestinationURL());

		_verifyMockInvocations();
	}

	@Test
	public void testSimplePatternSingleMismatch() {
		_setupRedirectPatternEntries(
			Collections.singletonList(
				new RedirectPatternEntry(
					Pattern.compile("^abc"), "xyz", StringPool.BLANK)));

		Assert.assertNull(_getRedirectProviderRedirect("123"));

		_verifyMockInvocations();
	}

	private RedirectProvider.Redirect _getRedirectProviderRedirect(
		String friendlyURL) {

		return _redirectProviderImpl.getRedirect(
			_GROUP_ID, friendlyURL, StringUtil.randomString());
	}

	private void _setupRedirectPatternEntries(
		List<RedirectPatternEntry> redirectPatternEntries) {

		_redirectProviderImpl.setPatternStrings(
			HashMapBuilder.put(
				_GROUP_ID, redirectPatternEntries
			).build());
	}

	private void _verifyMockInvocations() {
		Mockito.verify(
			_redirectEntryLocalService, Mockito.times(1)
		).fetchRedirectEntry(
			Mockito.eq(_GROUP_ID), Mockito.anyString(), Mockito.eq(false)
		);

		Mockito.verify(
			_redirectEntryLocalService, Mockito.times(1)
		).fetchRedirectEntry(
			Mockito.eq(_GROUP_ID), Mockito.anyString(), Mockito.eq(true)
		);
	}

	private static final long _GROUP_ID = RandomTestUtil.randomLong();

	private final RedirectEntryLocalService _redirectEntryLocalService =
		Mockito.mock(RedirectEntryLocalService.class);
	private final RedirectProviderImpl _redirectProviderImpl =
		new RedirectProviderImpl();

}