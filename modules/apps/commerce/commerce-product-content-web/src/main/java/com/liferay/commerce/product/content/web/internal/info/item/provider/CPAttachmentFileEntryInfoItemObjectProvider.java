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

package com.liferay.commerce.product.content.web.internal.info.item.provider;

import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.service.CPAttachmentFileEntryLocalService;
import com.liferay.info.exception.NoSuchInfoItemException;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemIdentifier;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"info.item.identifier=com.liferay.info.item.ClassPKInfoItemIdentifier",
		"item.class.name=com.liferay.commerce.product.model.CPAttachmentFileEntry",
		"service.ranking:Integer=100"
	},
	service = InfoItemObjectProvider.class
)
public class CPAttachmentFileEntryInfoItemObjectProvider
	implements InfoItemObjectProvider<CPAttachmentFileEntry> {

	@Override
	public CPAttachmentFileEntry getInfoItem(
			InfoItemIdentifier infoItemIdentifier)
		throws NoSuchInfoItemException {

		if (!(infoItemIdentifier instanceof ClassPKInfoItemIdentifier)) {
			throw new NoSuchInfoItemException(
				"Unsupported info item identifier type " + infoItemIdentifier);
		}

		ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
			(ClassPKInfoItemIdentifier)infoItemIdentifier;

		try {
			return _cpAttachmentFileEntryLocalService.getCPAttachmentFileEntry(
				classPKInfoItemIdentifier.getClassPK());
		}
		catch (PortalException portalException) {
			throw new NoSuchInfoItemException(
				"Unable to get commerce product definition attachment file " +
					"entry " + classPKInfoItemIdentifier.getClassPK(),
				portalException);
		}
	}

	@Override
	public CPAttachmentFileEntry getInfoItem(long classPK)
		throws NoSuchInfoItemException {

		ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
			new ClassPKInfoItemIdentifier(classPK);

		return getInfoItem(classPKInfoItemIdentifier);
	}

	@Reference
	private CPAttachmentFileEntryLocalService
		_cpAttachmentFileEntryLocalService;

}