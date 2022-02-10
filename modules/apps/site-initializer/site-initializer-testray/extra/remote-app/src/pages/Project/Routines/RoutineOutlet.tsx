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

import {useQuery} from '@apollo/client';
import {useEffect} from 'react';
import {
	Outlet,
	useLocation,
	useOutletContext,
	useParams,
} from 'react-router-dom';

import {getTestrayRoutine} from '../../../graphql/queries';
import useHeader from '../../../hooks/useHeader';

const RoutineOutlet = () => {
	const {pathname} = useLocation();
	const {projectId, routineId} = useParams();
	const {testrayProject}: any = useOutletContext();
	const {data} = useQuery(getTestrayRoutine, {
		variables: {
			testrayRoutineId: routineId,
		},
	});

	const testrayRoutine = data?.c?.testrayRoutine;

	const basePath = `/project/${projectId}/routines/${routineId}`;

	const {setHeading} = useHeader({
		useTabs: [
			{
				active: pathname === basePath,
				path: basePath,
				title: 'Current',
			},
			{
				active: pathname !== basePath,
				path: `${basePath}/archived`,
				title: 'Archived',
			},
		],
	});

	useEffect(() => {
		if (testrayProject && testrayRoutine) {
			setHeading([
				{category: 'PROJECT', title: testrayProject.name},
				{category: 'ROUTINE', title: testrayRoutine.name},
			]);
		}
	}, [setHeading, testrayProject, testrayRoutine]);

	if (testrayProject && testrayRoutine) {
		return <Outlet />;
	}

	return null;
};

export default RoutineOutlet;
