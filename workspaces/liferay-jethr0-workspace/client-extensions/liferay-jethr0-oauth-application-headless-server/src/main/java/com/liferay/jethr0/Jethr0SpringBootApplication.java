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

package com.liferay.jethr0;

import com.liferay.client.extension.util.spring.boot.ClientExtensionUtilSpringBootComponentScan;
import com.liferay.client.extension.util.spring.boot.LiferayOAuth2Util;
import com.liferay.jethr0.project.Project;
import com.liferay.jethr0.project.ProjectRepository;
import com.liferay.jethr0.project.comparator.ProjectComparator;
import com.liferay.jethr0.project.comparator.ProjectComparatorFactory;
import com.liferay.jethr0.project.comparator.ProjectComparatorRepository;
import com.liferay.jethr0.project.prioritizer.ProjectPrioritizer;
import com.liferay.jethr0.project.prioritizer.ProjectPrioritizerFactory;
import com.liferay.jethr0.project.prioritizer.ProjectPrioritizerRepository;
import com.liferay.jethr0.project.queue.ProjectQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

/**
 * @author Michael Hashimoto
 */
@Import(ClientExtensionUtilSpringBootComponentScan.class)
@SpringBootApplication
public class Jethr0SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Jethr0SpringBootApplication.class, args);
	}

	@Bean
	public OAuth2AccessToken getOAuth2AccessToken(
		AuthorizedClientServiceOAuth2AuthorizedClientManager
			authorizedClientServiceOAuth2AuthorizedClientManager) {

		return LiferayOAuth2Util.getOAuth2AccessToken(
			authorizedClientServiceOAuth2AuthorizedClientManager,
			_liferayOAuthApplicationExternalReferenceCodes);
	}

	@Bean
	public ProjectQueue getProjectQueue(
		ProjectComparatorRepository projectComparatorRepository,
		ProjectPrioritizerRepository projectPrioritizerRepository,
		ProjectRepository projectRepository) {

		ProjectQueue projectQueue = new ProjectQueue();

		projectQueue.setProjectPrioritizer(
			_getDefaultProjectPrioritizer(
				projectComparatorRepository, projectPrioritizerRepository));

		projectQueue.addProjects(
			projectRepository.getByState(Project.State.RUNNING));

		for (Project project : projectQueue.getProjects()) {
			System.out.println(project);
		}

		return projectQueue;
	}

	private ProjectPrioritizer _getDefaultProjectPrioritizer(
		ProjectComparatorRepository projectComparatorRepository,
		ProjectPrioritizerRepository projectPrioritizerRepository) {

		ProjectPrioritizer projectPrioritizer =
			projectPrioritizerRepository.getByName(_liferayProjectPrioritizer);

		if (projectPrioritizer != null) {
			return projectPrioritizer;
		}

		projectPrioritizer = ProjectPrioritizerFactory.newProjectPrioritizer(
			_liferayProjectPrioritizer);

		projectPrioritizer = projectPrioritizerRepository.add(
			projectPrioritizer);

		projectComparatorRepository.add(
			ProjectComparatorFactory.newProjectComparator(
				projectPrioritizer, 1, ProjectComparator.Type.PROJECT_PRIORITY,
				null));
		projectComparatorRepository.add(
			ProjectComparatorFactory.newProjectComparator(
				projectPrioritizer, 2, ProjectComparator.Type.FIFO, null));

		return projectPrioritizer;
	}

	@Value("${liferay.oauth.application.external.reference.codes}")
	private String _liferayOAuthApplicationExternalReferenceCodes;

	@Value("${liferay.jethr0.project.prioritizer}")
	private String _liferayProjectPrioritizer;

}