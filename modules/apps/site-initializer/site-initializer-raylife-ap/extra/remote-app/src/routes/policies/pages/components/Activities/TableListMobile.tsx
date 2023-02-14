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

import ClayTable from '@clayui/table';
import classnames from 'classnames';
import React, {useEffect, useState} from 'react';

import Panel from '../PolicyDetail/Panel';
import dataActivities from './DataActivities';

import './index.scss';

type Props = {
	headers: TableHeaders[];
	rows: InfoRowContent[];
};

type TableHeaders = {
	bold?: boolean;
	key: string;
	value: string;
};

const {Body, Cell, Head, Row} = ClayTable;

type InfoRowContent = {[keys: string]: string};

const TableListMobile: React.FC<Props> = ({headers, rows}) => {
	const [isPanelExpanded, setIsPanelExpanded] = useState<boolean[]>([]);
	const [isRowSelected, setIsRowSelected] = useState<boolean>(false);

	const ContentDescription = ({
		columnOne,
		columnTwo,
	}: {
		columnOne: string;
		columnTwo: string;
	}) => (
		<div className="d-flex ml-3 my-3 w-100">
			<div className="d-flex font-table ml-2 text-nowrap w-50">
				{columnOne}
			</div>

			<div className="font-table-bold text-nowrap">{columnTwo}</div>
		</div>
	);

	const toggleSelectedRow = (index: number) => {
		isPanelExpanded[index] === false
			? setIsRowSelected(true)
			: setIsRowSelected(false);
	};

	const resetExpandedPanel = dataActivities.map(() => {
		return false;
	});

	const displayHistoryPanel = (index: number) => {
		const supportArray = [...isPanelExpanded];
		toggleSelectedRow(index);

		if (isRowSelected === isPanelExpanded[index]) {
			supportArray[index] = !supportArray[index];
			setIsPanelExpanded(supportArray);
		}
		else {
			resetExpandedPanel[index] = !resetExpandedPanel[index];
			setIsPanelExpanded(resetExpandedPanel);
		}
	};

	useEffect(() => {
		setIsPanelExpanded(resetExpandedPanel);
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	return (
		<div className="table-list-container">
			<div className="w-100">
				<div className="bg-neutral-0 box-activites col d-flex">
					<h2 className="border-link-active font ml-1 mt-2">
						Activities
					</h2>
				</div>

				<table className="border-right box-table-mobile w-100">
					<Head>
						<Row className="bg-neutral-0 border border-header">
							{headers.map(
								(header: TableHeaders, index: number) => (
									<Cell
										className="pl-4 py-3 text-paragraph-sm"
										headingCell
										key={index}
									>
										{header.value}
									</Cell>
								)
							)}
						</Row>
					</Head>

					<Body>
						<div className="h-100 w-100">
							{rows.map((row, index: number) => {
								return (
									<Row key={index}>
										<div
											className={classnames(
												'cursor-pointer bg-neutral-0 position-relative',
												{
													'dotted-line pt-3':
														isPanelExpanded[
															index
														] === false,
													'mb-2 pt-0':
														isPanelExpanded[
															index
														] === true,
												}
											)}
											key={index}
										>
											<Panel
												Description={
													<ContentDescription
														columnOne={row.date}
														columnTwo={row.activity}
													/>
												}
												hasExpandedButton={false}
												isPanelExpanded={
													isPanelExpanded[index]
												}
												key={index}
												setIsPanelExpanded={() =>
													displayHistoryPanel(index)
												}
											>
												<div className="bg-message bg-neutral-0 d-flex font-table m-2 mr-3 p-2 rounded">
													{row.message}
												</div>
											</Panel>
										</div>
									</Row>
								);
							})}
						</div>
					</Body>
				</table>
			</div>
		</div>
	);
};

export default TableListMobile;
