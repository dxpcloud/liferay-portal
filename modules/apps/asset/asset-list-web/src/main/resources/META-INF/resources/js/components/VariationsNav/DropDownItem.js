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

import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React from 'react';

const DropDownItem = ({direction, disabled, icon, index, onClick, text}) => {
	const handleClick = () => {
		onClick({direction, index});
	};

	return (
		<ClayDropDown.Item>
			<ClayButton
				block
				className="align-items-center d-flex justify-content-between"
				disabled={disabled}
				displayType={null}
				onClick={handleClick}
				small
			>
				{text}
				<ClayIcon symbol={icon} />
			</ClayButton>
		</ClayDropDown.Item>
	);
};

DropDownItem.propTypes = {
	direction: PropTypes.number.isRequired,
	disabled: PropTypes.bool.isRequired,
	icon: PropTypes.string.isRequired,
	index: PropTypes.number.isRequired,
	onClick: PropTypes.func.isRequired,
	text: PropTypes.string.isRequired,
};

export default DropDownItem;
