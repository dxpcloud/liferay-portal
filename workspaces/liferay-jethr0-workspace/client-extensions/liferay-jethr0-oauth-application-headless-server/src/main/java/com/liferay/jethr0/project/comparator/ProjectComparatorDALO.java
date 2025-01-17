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

package com.liferay.jethr0.project.comparator;

import com.liferay.jethr0.entity.dalo.BaseEntityDALO;
import com.liferay.jethr0.project.prioritizer.ProjectPrioritizerRepository;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Hashimoto
 */
@Configuration
public class ProjectComparatorDALO extends BaseEntityDALO<ProjectComparator> {

	@Override
	public ProjectComparator newEntity(JSONObject jsonObject) {
		long projectPrioritizerId = jsonObject.getLong(
			"r_projectPrioritizerToProjectComparators_c_projectPrioritizerId");

		return ProjectComparatorFactory.newProjectComparator(
			_projectPrioritizerRepository.getById(projectPrioritizerId),
			jsonObject);
	}

	@Override
	protected String getObjectDefinitionLabel() {
		return "Project Comparator";
	}

	@Autowired
	private ProjectPrioritizerRepository _projectPrioritizerRepository;

}