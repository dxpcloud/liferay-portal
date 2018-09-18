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

package com.liferay.calendar.web.internal.display.context;

import com.liferay.calendar.constants.CalendarActionKeys;
import com.liferay.calendar.constants.CalendarPortletKeys;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.model.CalendarResource;
import com.liferay.calendar.recurrence.Recurrence;
import com.liferay.calendar.service.CalendarBookingLocalService;
import com.liferay.calendar.service.CalendarBookingService;
import com.liferay.calendar.service.CalendarLocalService;
import com.liferay.calendar.service.CalendarService;
import com.liferay.calendar.util.RecurrenceUtil;
import com.liferay.calendar.web.internal.security.permission.resource.CalendarPermission;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adam Brandizzi
 */
public class CalendarDisplayContext {

	public CalendarDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		GroupLocalService groupLocalService,
		CalendarBookingLocalService calendarBookingLocalService,
		CalendarBookingService calendarBookingService,
		CalendarLocalService calendarLocalService,
		CalendarService calendarService) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_groupLocalService = groupLocalService;
		_calendarBookingLocalService = calendarBookingLocalService;
		_calendarBookingService = calendarBookingService;
		_calendarLocalService = calendarLocalService;
		_calendarService = calendarService;
		_themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<CalendarBooking> getChildCalendarBookings(
			CalendarBooking calendarBooking)
		throws PortalException {

		Group group = _themeDisplay.getScopeGroup();

		return _calendarBookingService.getChildCalendarBookings(
			calendarBooking.getCalendarBookingId(), group.isStagingGroup());
	}

	public Calendar getDefaultCalendar(
			List<Calendar> groupCalendars, List<Calendar> userCalendars)
		throws PortalException {

		Calendar defaultCalendar = null;

		for (Calendar groupCalendar : groupCalendars) {
			if (groupCalendar.isDefaultCalendar() &&
				CalendarPermission.contains(
					_themeDisplay.getPermissionChecker(), groupCalendar,
					CalendarActionKeys.MANAGE_BOOKINGS)) {

				defaultCalendar = groupCalendar;
			}
		}

		if (defaultCalendar == null) {
			for (Calendar userCalendar : userCalendars) {
				if (userCalendar.isDefaultCalendar()) {
					defaultCalendar = userCalendar;
				}
			}
		}

		if (defaultCalendar == null) {
			for (Calendar groupCalendar : groupCalendars) {
				if (CalendarPermission.contains(
						_themeDisplay.getPermissionChecker(), groupCalendar,
						CalendarActionKeys.MANAGE_BOOKINGS)) {

					defaultCalendar = groupCalendar;
				}
			}
		}

		if (defaultCalendar == null) {
			for (Calendar groupCalendar : groupCalendars) {
				if (groupCalendar.isDefaultCalendar() &&
					CalendarPermission.contains(
						_themeDisplay.getPermissionChecker(), groupCalendar,
						CalendarActionKeys.VIEW_BOOKING_DETAILS)) {

					defaultCalendar = groupCalendar;
				}
			}
		}

		return defaultCalendar;
	}

	public String getEditCalendarBookingRedirectURL(
		HttpServletRequest request, String defaultURL) {

		String redirect = ParamUtil.getString(request, "redirect");

		String ppid = HttpUtil.getParameter(redirect, "p_p_id", false);

		if (ppid.equals(CalendarPortletKeys.CALENDAR)) {
			return defaultURL;
		}

		return ParamUtil.getString(request, "redirect", defaultURL);
	}

	public Recurrence getLastRecurrence(CalendarBooking calendarBooking)
		throws PortalException {

		List<CalendarBooking> calendarBookings =
			_calendarBookingLocalService.getRecurringCalendarBookings(
				calendarBooking);

		CalendarBooking lastCalendarBooking =
			RecurrenceUtil.getLastInstanceCalendarBooking(calendarBookings);

		return lastCalendarBooking.getRecurrenceObj();
	}

	public List<NavigationItem> getNavigationItems() {
		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			_renderRequest);

		String tabs1 = ParamUtil.getString(request, "tabs1", "calendar");

		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(tabs1.equals("calendar"));
						navigationItem.setHref(
							_renderResponse.createRenderURL(), "tabs1",
							"calendar");
						navigationItem.setLabel(
							LanguageUtil.get(request, "calendar"));
					});

				add(
					navigationItem -> {
						navigationItem.setActive(tabs1.equals("resources"));
						navigationItem.setHref(
							_renderResponse.createRenderURL(), "tabs1",
							"resources");
						navigationItem.setLabel(
							LanguageUtil.get(request, "resources"));
					});
			}
		};
	}

	public List<Calendar> getOtherCalendars(User user, long[] calendarIds)
		throws PortalException {

		List<Calendar> otherCalendars = new ArrayList<>();

		for (long calendarId : calendarIds) {
			Calendar calendar = null;

			try {
				calendar = _calendarService.fetchCalendar(calendarId);
			}
			catch (PrincipalException pe) {
				if (_log.isInfoEnabled()) {
					StringBundler sb = new StringBundler(4);

					sb.append("No ");
					sb.append(ActionKeys.VIEW);
					sb.append(" permission for user ");
					sb.append(user.getUserId());

					_log.info(sb.toString(), pe);
				}

				continue;
			}

			if (calendar == null) {
				continue;
			}

			CalendarResource calendarResource = calendar.getCalendarResource();

			if (!calendarResource.isActive()) {
				continue;
			}

			Group scopeGroup = _themeDisplay.getScopeGroup();

			long scopeGroupId = scopeGroup.getGroupId();
			long scopeLiveGroupId = scopeGroup.getLiveGroupId();

			Group calendarGroup = _groupLocalService.getGroup(
				calendar.getGroupId());

			long calendarGroupId = calendarGroup.getGroupId();

			if (scopeGroup.isStagingGroup()) {
				if (calendarGroup.isStagingGroup()) {
					if (scopeGroupId != calendarGroupId) {
						calendar =
							_calendarLocalService.fetchCalendarByUuidAndGroupId(
								calendar.getUuid(),
								calendarGroup.getLiveGroupId());
					}
				}
				else if (scopeLiveGroupId == calendarGroupId) {
					Group stagingGroup = calendarGroup.getStagingGroup();

					calendar =
						_calendarLocalService.fetchCalendarByUuidAndGroupId(
							calendar.getUuid(), stagingGroup.getGroupId());
				}
			}
			else if (calendarGroup.isStagingGroup()) {
				calendar = _calendarLocalService.fetchCalendarByUuidAndGroupId(
					calendar.getUuid(), calendarGroup.getLiveGroupId());
			}

			if (calendar == null) {
				continue;
			}

			otherCalendars.add(calendar);
		}

		return otherCalendars;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CalendarDisplayContext.class.getName());

	private final CalendarBookingLocalService _calendarBookingLocalService;
	private final CalendarBookingService _calendarBookingService;
	private final CalendarLocalService _calendarLocalService;
	private final CalendarService _calendarService;
	private final GroupLocalService _groupLocalService;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ThemeDisplay _themeDisplay;

}