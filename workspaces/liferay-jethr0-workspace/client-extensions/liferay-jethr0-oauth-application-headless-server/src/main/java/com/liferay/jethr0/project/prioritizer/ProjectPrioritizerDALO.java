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

package com.liferay.jethr0.project.prioritizer;

import com.liferay.jethr0.dalo.ProjectPrioritizerToProjectComparatorsDALO;
import com.liferay.jethr0.entity.dalo.BaseEntityDALO;
import com.liferay.jethr0.project.comparator.ProjectComparator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Hashimoto
 */
@Configuration
public class ProjectPrioritizerDALO extends BaseEntityDALO<ProjectPrioritizer> {

	@Override
	public ProjectPrioritizer create(ProjectPrioritizer projectPrioritizer) {
		projectPrioritizer = super.create(projectPrioritizer);

		_projectPrioritizerToProjectComparatorsDALO.updateRelationships(
			projectPrioritizer);

		return projectPrioritizer;
	}

	@Override
	public void delete(ProjectPrioritizer projectPrioritizer) {
		for (ProjectComparator projectComparator :
				projectPrioritizer.getProjectComparators()) {

			_projectPrioritizerToProjectComparatorsDALO.deleteRelationship(
				projectPrioritizer, projectComparator);
		}

		super.delete(projectPrioritizer);
	}

	@Override
	public List<ProjectPrioritizer> getAll() {
		List<ProjectPrioritizer> projectPrioritizers = new ArrayList<>();

		for (ProjectPrioritizer projectPrioritizer : super.getAll()) {
			projectPrioritizer.addProjectComparators(
				_projectPrioritizerToProjectComparatorsDALO.
					retrieveProjectComparators(projectPrioritizer));

			projectPrioritizers.add(projectPrioritizer);
		}

		return projectPrioritizers;
	}

	@Override
	public ProjectPrioritizer update(ProjectPrioritizer projectPrioritizer) {
		projectPrioritizer = super.update(projectPrioritizer);

		_projectPrioritizerToProjectComparatorsDALO.updateRelationships(
			projectPrioritizer);

		return projectPrioritizer;
	}

	@Override
	protected String getObjectDefinitionLabel() {
		return "Project Prioritizer";
	}

	@Override
	protected ProjectPrioritizer newEntity(JSONObject jsonObject) {
		return ProjectPrioritizerFactory.newProjectPrioritizer(jsonObject);
	}

	@Autowired
	private ProjectPrioritizerToProjectComparatorsDALO
		_projectPrioritizerToProjectComparatorsDALO;

}