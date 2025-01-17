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

import {toggleDisabled} from 'frontend-js-web';

import {previewSeoFireChange} from './PreviewSeoEvents.es';

export default function ({namespace}) {
	const canonicalURLEnabledCheck = document.getElementById(
		`${namespace}canonicalURLEnabled`
	);
	const canonicalURLField = document.getElementById(
		`${namespace}canonicalURL`
	);
	const canonicalURLFieldDefaultLocale = document.getElementById(
		`${namespace}canonicalURL_${Liferay.ThemeDisplay.getLanguageId()}`
	);
	const canonicalURLAlert = document.getElementById(
		`${namespace}canonicalURLAlert`
	);
	const canonicalURLSettings = document.getElementById(
		`${namespace}customCanonicalURLSettings`
	);

	canonicalURLEnabledCheck.addEventListener('click', (event) => {
		const disabled = !event.target.checked;

		canonicalURLAlert.classList.toggle('hide');

		const label = canonicalURLSettings.querySelector('label');

		toggleDisabled(
			[canonicalURLField, canonicalURLFieldDefaultLocale, label],
			disabled
		);

		if (!canonicalURLField.value && canonicalURLField.placeholder) {
			canonicalURLField.value = canonicalURLField.placeholder;
		}

		previewSeoFireChange(namespace, {
			disabled,
			type: 'url',
			value: canonicalURLField.value,
		});
	});
}
