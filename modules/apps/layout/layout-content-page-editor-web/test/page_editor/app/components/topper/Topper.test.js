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

import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';
import React from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import Row from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/layout-data-items/Row';
import Topper from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/topper/Topper';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/viewportSizes';
import {ControlsProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/ControlsContext';
import {StoreAPIContextProvider} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';

const renderTopper = ({
	hasUpdatePermissions = true,
	lockedExperience = false,
	rowConfig = {styles: {}},
} = {}) => {
	const row = {
		children: [],
		config: rowConfig,
		itemId: 'row',
		parentId: null,
		type: LAYOUT_DATA_ITEM_TYPES.row,
	};

	const layoutData = {
		items: {row},
	};

	return render(
		<DndProvider backend={HTML5Backend}>
			<ControlsProvider>
				<StoreAPIContextProvider
					getState={() => ({
						fragmentEntryLinks: {},
						layoutData,
						permissions: {
							LOCKED_SEGMENTS_EXPERIMENT: lockedExperience,
							UPDATE: hasUpdatePermissions,
						},
						selectedViewportSize: VIEWPORT_SIZES.desktop,
					})}
				>
					<Topper item={row} layoutData={layoutData}>
						<Row item={row} layoutData={layoutData}></Row>
					</Topper>
				</StoreAPIContextProvider>
			</ControlsProvider>
		</DndProvider>
	);
};

describe('Topper', () => {
	it('does not render Topper if user has no permissions', () => {
		const {baseElement} = renderTopper({hasUpdatePermissions: false});

		expect(baseElement.querySelector('.page-editor__topper')).toBe(null);
	});

	it('renders Topper if user has permissions', () => {
		const {baseElement} = renderTopper();

		expect(
			baseElement.querySelector('.page-editor__topper')
		).toBeInTheDocument();
	});

	it('renders name of the fragment', () => {
		renderTopper();

		expect(screen.queryByLabelText('grid')).toBeInTheDocument();
	});

	it('renders custom name of the fragment', () => {
		renderTopper({rowConfig: {name: 'customName'}});

		expect(screen.queryByLabelText('customName')).toBeInTheDocument();
	});
});
