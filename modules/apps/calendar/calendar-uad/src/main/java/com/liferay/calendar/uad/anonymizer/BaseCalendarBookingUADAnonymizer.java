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

package com.liferay.calendar.uad.anonymizer;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.uad.constants.CalendarUADConstants;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.user.associated.data.anonymizer.DynamicQueryUADAnonymizer;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the calendar booking UAD anonymizer.
 *
 * <p>
 * This implementation exists only as a container for the default methods
 * generated by ServiceBuilder. All custom service methods should be put in
 * {@link CalendarBookingUADAnonymizer}.
 * </p>
 *
 * @author Eduardo Lundgren
 * @generated
 */
public abstract class BaseCalendarBookingUADAnonymizer
	extends DynamicQueryUADAnonymizer<CalendarBooking> {

	@Override
	public void autoAnonymize(
			CalendarBooking calendarBooking, long userId, User anonymousUser)
		throws PortalException {

		if (calendarBooking.getUserId() == userId) {
			calendarBooking.setUserId(anonymousUser.getUserId());
			calendarBooking.setUserName(anonymousUser.getFullName());

			autoAnonymizeAssetEntry(calendarBooking, anonymousUser);
		}

		if (calendarBooking.getStatusByUserId() == userId) {
			calendarBooking.setStatusByUserId(anonymousUser.getUserId());
			calendarBooking.setStatusByUserName(anonymousUser.getFullName());
		}

		calendarBookingLocalService.updateCalendarBooking(calendarBooking);
	}

	@Override
	public void delete(CalendarBooking calendarBooking) throws PortalException {
		calendarBookingLocalService.deleteCalendarBooking(calendarBooking);
	}

	@Override
	public Class<CalendarBooking> getTypeClass() {
		return CalendarBooking.class;
	}

	protected void autoAnonymizeAssetEntry(
		CalendarBooking calendarBooking, User anonymousUser) {

		AssetEntry assetEntry = fetchAssetEntry(calendarBooking);

		if (assetEntry != null) {
			assetEntry.setUserId(anonymousUser.getUserId());
			assetEntry.setUserName(anonymousUser.getFullName());

			assetEntryLocalService.updateAssetEntry(assetEntry);
		}
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return calendarBookingLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return CalendarUADConstants.USER_ID_FIELD_NAMES_CALENDAR_BOOKING;
	}

	protected AssetEntry fetchAssetEntry(CalendarBooking calendarBooking) {
		return assetEntryLocalService.fetchEntry(
			CalendarBooking.class.getName(),
			calendarBooking.getCalendarBookingId());
	}

	@Reference
	protected AssetEntryLocalService assetEntryLocalService;

	@Reference
	protected CalendarBookingLocalService calendarBookingLocalService;

}