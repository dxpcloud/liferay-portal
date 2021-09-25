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

package com.liferay.object.service;

import com.liferay.object.model.ObjectActionEntry;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for ObjectActionEntry. This utility wraps
 * <code>com.liferay.object.service.impl.ObjectActionEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Marco Leo
 * @see ObjectActionEntryLocalService
 * @generated
 */
public class ObjectActionEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.object.service.impl.ObjectActionEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the object action entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectActionEntry the object action entry
	 * @return the object action entry that was added
	 */
	public static ObjectActionEntry addObjectActionEntry(
		ObjectActionEntry objectActionEntry) {

		return getService().addObjectActionEntry(objectActionEntry);
	}

	/**
	 * Creates a new object action entry with the primary key. Does not add the object action entry to the database.
	 *
	 * @param objectActionEntryId the primary key for the new object action entry
	 * @return the new object action entry
	 */
	public static ObjectActionEntry createObjectActionEntry(
		long objectActionEntryId) {

		return getService().createObjectActionEntry(objectActionEntryId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the object action entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectActionEntryId the primary key of the object action entry
	 * @return the object action entry that was removed
	 * @throws PortalException if a object action entry with the primary key could not be found
	 */
	public static ObjectActionEntry deleteObjectActionEntry(
			long objectActionEntryId)
		throws PortalException {

		return getService().deleteObjectActionEntry(objectActionEntryId);
	}

	/**
	 * Deletes the object action entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectActionEntry the object action entry
	 * @return the object action entry that was removed
	 */
	public static ObjectActionEntry deleteObjectActionEntry(
		ObjectActionEntry objectActionEntry) {

		return getService().deleteObjectActionEntry(objectActionEntry);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static ObjectActionEntry fetchObjectActionEntry(
		long objectActionEntryId) {

		return getService().fetchObjectActionEntry(objectActionEntryId);
	}

	/**
	 * Returns the object action entry with the matching UUID and company.
	 *
	 * @param uuid the object action entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object action entry, or <code>null</code> if a matching object action entry could not be found
	 */
	public static ObjectActionEntry fetchObjectActionEntryByUuidAndCompanyId(
		String uuid, long companyId) {

		return getService().fetchObjectActionEntryByUuidAndCompanyId(
			uuid, companyId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns a range of all the object action entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.object.model.impl.ObjectActionEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of object action entries
	 * @param end the upper bound of the range of object action entries (not inclusive)
	 * @return the range of object action entries
	 */
	public static List<ObjectActionEntry> getObjectActionEntries(
		int start, int end) {

		return getService().getObjectActionEntries(start, end);
	}

	public static List<ObjectActionEntry> getObjectActionEntries(
		long objectDefinitionId, String objectActionTriggerKey) {

		return getService().getObjectActionEntries(
			objectDefinitionId, objectActionTriggerKey);
	}

	/**
	 * Returns the number of object action entries.
	 *
	 * @return the number of object action entries
	 */
	public static int getObjectActionEntriesCount() {
		return getService().getObjectActionEntriesCount();
	}

	/**
	 * Returns the object action entry with the primary key.
	 *
	 * @param objectActionEntryId the primary key of the object action entry
	 * @return the object action entry
	 * @throws PortalException if a object action entry with the primary key could not be found
	 */
	public static ObjectActionEntry getObjectActionEntry(
			long objectActionEntryId)
		throws PortalException {

		return getService().getObjectActionEntry(objectActionEntryId);
	}

	/**
	 * Returns the object action entry with the matching UUID and company.
	 *
	 * @param uuid the object action entry's UUID
	 * @param companyId the primary key of the company
	 * @return the matching object action entry
	 * @throws PortalException if a matching object action entry could not be found
	 */
	public static ObjectActionEntry getObjectActionEntryByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return getService().getObjectActionEntryByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the object action entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect ObjectActionEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param objectActionEntry the object action entry
	 * @return the object action entry that was updated
	 */
	public static ObjectActionEntry updateObjectActionEntry(
		ObjectActionEntry objectActionEntry) {

		return getService().updateObjectActionEntry(objectActionEntry);
	}

	public static ObjectActionEntryLocalService getService() {
		return _service;
	}

	private static volatile ObjectActionEntryLocalService _service;

}