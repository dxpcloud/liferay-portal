/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.timebased.otp.service.persistence.impl;

import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimeBasedOTPEntryTable;
import com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimeBasedOTPEntryImpl;
import com.liferay.multi.factor.authentication.timebased.otp.model.impl.MFATimeBasedOTPEntryModelImpl;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;

/**
 * The arguments resolver class for retrieving value from MFATimeBasedOTPEntry.
 *
 * @author Arthur Chan
 * @generated
 */
@Component(immediate = true, service = ArgumentsResolver.class)
public class MFATimeBasedOTPEntryModelArgumentsResolver
	implements ArgumentsResolver {

	@Override
	public Object[] getArguments(
		FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
		boolean original) {

		String[] columnNames = finderPath.getColumnNames();

		if ((columnNames == null) || (columnNames.length == 0)) {
			if (baseModel.isNew()) {
				return new Object[0];
			}

			return null;
		}

		MFATimeBasedOTPEntryModelImpl mfaTimeBasedOTPEntryModelImpl =
			(MFATimeBasedOTPEntryModelImpl)baseModel;

		long columnBitmask = mfaTimeBasedOTPEntryModelImpl.getColumnBitmask();

		if (!checkColumn || (columnBitmask == 0)) {
			return _getValue(
				mfaTimeBasedOTPEntryModelImpl, columnNames, original);
		}

		Long finderPathColumnBitmask = _finderPathColumnBitmasksCache.get(
			finderPath);

		if (finderPathColumnBitmask == null) {
			finderPathColumnBitmask = 0L;

			for (String columnName : columnNames) {
				finderPathColumnBitmask |=
					mfaTimeBasedOTPEntryModelImpl.getColumnBitmask(columnName);
			}

			_finderPathColumnBitmasksCache.put(
				finderPath, finderPathColumnBitmask);
		}

		if ((columnBitmask & finderPathColumnBitmask) != 0) {
			return _getValue(
				mfaTimeBasedOTPEntryModelImpl, columnNames, original);
		}

		return null;
	}

	@Override
	public String getClassName() {
		return MFATimeBasedOTPEntryImpl.class.getName();
	}

	@Override
	public String getTableName() {
		return MFATimeBasedOTPEntryTable.INSTANCE.getTableName();
	}

	private static Object[] _getValue(
		MFATimeBasedOTPEntryModelImpl mfaTimeBasedOTPEntryModelImpl,
		String[] columnNames, boolean original) {

		Object[] arguments = new Object[columnNames.length];

		for (int i = 0; i < arguments.length; i++) {
			String columnName = columnNames[i];

			if (original) {
				arguments[i] =
					mfaTimeBasedOTPEntryModelImpl.getColumnOriginalValue(
						columnName);
			}
			else {
				arguments[i] = mfaTimeBasedOTPEntryModelImpl.getColumnValue(
					columnName);
			}
		}

		return arguments;
	}

	private static final Map<FinderPath, Long> _finderPathColumnBitmasksCache =
		new ConcurrentHashMap<>();

}