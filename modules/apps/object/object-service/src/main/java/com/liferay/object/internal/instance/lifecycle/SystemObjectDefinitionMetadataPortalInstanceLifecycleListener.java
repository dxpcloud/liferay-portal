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

package com.liferay.object.internal.instance.lifecycle;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewDescriptorRenderer;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.notification.handler.NotificationHandler;
import com.liferay.notification.term.evaluator.NotificationTermEvaluator;
import com.liferay.object.internal.item.selector.SystemObjectEntryItemSelectorView;
import com.liferay.object.internal.notification.handler.ObjectDefinitionNotificationHandler;
import com.liferay.object.internal.notification.term.contributor.ObjectDefinitionNotificationTermEvaluator;
import com.liferay.object.internal.persistence.ObjectDefinitionTableArgumentsResolver;
import com.liferay.object.internal.related.models.SystemObject1toMObjectRelatedModelsProviderImpl;
import com.liferay.object.internal.related.models.SystemObjectMtoMObjectRelatedModelsProviderImpl;
import com.liferay.object.internal.rest.context.path.RESTContextPathResolverImpl;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.related.models.ObjectRelatedModelsProvider;
import com.liferay.object.related.models.ObjectRelatedModelsProviderRegistry;
import com.liferay.object.rest.context.path.RESTContextPathResolver;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataRegistry;
import com.liferay.osgi.service.tracker.collections.EagerServiceTrackerCustomizer;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.EveryNodeEveryStartup;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ListTypeLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
@Component(service = PortalInstanceLifecycleListener.class)
public class SystemObjectDefinitionMetadataPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener
	implements EveryNodeEveryStartup {

	@Override
	public void portalInstanceRegistered(Company company) {
		if (_log.isDebugEnabled()) {
			_log.debug("Registered portal instance " + company);
		}

		for (SystemObjectDefinitionMetadata systemObjectDefinitionMetadata :
				_serviceTrackerList) {

			_apply(company.getCompanyId(), systemObjectDefinitionMetadata);
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		if (_log.isDebugEnabled()) {
			_log.debug("Activate " + bundleContext);
		}

		_bundleContext = bundleContext;

		_openingThreadLocal.set(Boolean.TRUE);

		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, SystemObjectDefinitionMetadata.class, null,
			new EagerServiceTrackerCustomizer
				<SystemObjectDefinitionMetadata,
				 SystemObjectDefinitionMetadata>() {

				@Override
				public SystemObjectDefinitionMetadata addingService(
					ServiceReference<SystemObjectDefinitionMetadata>
						serviceReference) {

					SystemObjectDefinitionMetadata
						systemObjectDefinitionMetadata =
							bundleContext.getService(serviceReference);

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Adding service " + systemObjectDefinitionMetadata);
					}

					if (!_openingThreadLocal.get()) {
						_companyLocalService.forEachCompanyId(
							companyId -> _apply(
								companyId, systemObjectDefinitionMetadata));
					}

					return systemObjectDefinitionMetadata;
				}

				@Override
				public void modifiedService(
					ServiceReference<SystemObjectDefinitionMetadata>
						serviceReference,
					SystemObjectDefinitionMetadata
						systemObjectDefinitionMetadata) {
				}

				@Override
				public void removedService(
					ServiceReference<SystemObjectDefinitionMetadata>
						serviceReference,
					SystemObjectDefinitionMetadata
						systemObjectDefinitionMetadata) {

					bundleContext.ungetService(serviceReference);
				}

			});

		_openingThreadLocal.set(Boolean.FALSE);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	private void _apply(
		long companyId,
		SystemObjectDefinitionMetadata systemObjectDefinitionMetadata) {

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					"Applying ", systemObjectDefinitionMetadata, " to company ",
					companyId));
		}

		try {
			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinition(
					companyId, systemObjectDefinitionMetadata.getName());

			if ((objectDefinition == null) ||
				(objectDefinition.getVersion() !=
					systemObjectDefinitionMetadata.getVersion())) {

				objectDefinition =
					_objectDefinitionLocalService.
						addOrUpdateSystemObjectDefinition(
							companyId, systemObjectDefinitionMetadata);
			}

			_bundleContext.registerService(
				ArgumentsResolver.class,
				new ObjectDefinitionTableArgumentsResolver(
					objectDefinition.getExtensionDBTableName()),
				HashMapDictionaryBuilder.put(
					"class.name", objectDefinition.getExtensionDBTableName()
				).put(
					"table.name", objectDefinition.getExtensionDBTableName()
				).build());
			_bundleContext.registerService(
				ItemSelectorView.class,
				new SystemObjectEntryItemSelectorView(
					_itemSelector, _itemSelectorViewDescriptorRenderer,
					objectDefinition, _objectFieldLocalService,
					_objectRelatedModelsProviderRegistry, _portal),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.selector.view.order", 500
				).build());
			_bundleContext.registerService(
				NotificationTermEvaluator.class,
				new ObjectDefinitionNotificationTermEvaluator(
					_listTypeLocalService, objectDefinition,
					_objectFieldLocalService, _userLocalService),
				HashMapDictionaryBuilder.<String, Object>put(
					"class.name", objectDefinition.getClassName()
				).build());
			_bundleContext.registerService(
				NotificationHandler.class,
				new ObjectDefinitionNotificationHandler(objectDefinition),
				HashMapDictionaryBuilder.<String, Object>put(
					"class.name", objectDefinition.getClassName()
				).build());
			_bundleContext.registerService(
				ObjectRelatedModelsProvider.class,
				new SystemObject1toMObjectRelatedModelsProviderImpl(
					objectDefinition, _objectDefinitionLocalService,
					_objectEntryLocalService, _objectFieldLocalService,
					_objectRelationshipLocalService,
					_persistedModelLocalServiceRegistry,
					systemObjectDefinitionMetadata,
					_systemObjectDefinitionMetadataRegistry),
				null);
			_bundleContext.registerService(
				ObjectRelatedModelsProvider.class,
				new SystemObjectMtoMObjectRelatedModelsProviderImpl(
					objectDefinition, _objectDefinitionLocalService,
					_objectFieldLocalService, _objectRelationshipLocalService,
					_persistedModelLocalServiceRegistry,
					systemObjectDefinitionMetadata,
					_systemObjectDefinitionMetadataRegistry),
				null);

			JaxRsApplicationDescriptor jaxRsApplicationDescriptor =
				systemObjectDefinitionMetadata.getJaxRsApplicationDescriptor();

			_bundleContext.registerService(
				RESTContextPathResolver.class,
				new RESTContextPathResolverImpl(
					"/o/" + jaxRsApplicationDescriptor.getRESTContextPath(),
					_objectScopeProviderRegistry.getObjectScopeProvider(
						objectDefinition.getScope()),
					true),
				HashMapDictionaryBuilder.<String, Object>put(
					"model.class.name", objectDefinition.getClassName()
				).build());
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SystemObjectDefinitionMetadataPortalInstanceLifecycleListener.class);

	private static final ThreadLocal<Boolean> _openingThreadLocal =
		new CentralizedThreadLocal<>(
			SystemObjectDefinitionMetadataPortalInstanceLifecycleListener.class.
				getName() + "._openingThreadLocal",
			() -> Boolean.FALSE);

	private BundleContext _bundleContext;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private ItemSelectorViewDescriptorRenderer<InfoItemItemSelectorCriterion>
		_itemSelectorViewDescriptorRenderer;

	@Reference
	private ListTypeLocalService _listTypeLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	@Reference
	private ObjectRelatedModelsProviderRegistry
		_objectRelatedModelsProviderRegistry;

	@Reference
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@Reference
	private ObjectScopeProviderRegistry _objectScopeProviderRegistry;

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.object.service)(release.schema.version>=1.0.0))"
	)
	private Release _release;

	private ServiceTrackerList<SystemObjectDefinitionMetadata>
		_serviceTrackerList;

	@Reference
	private SystemObjectDefinitionMetadataRegistry
		_systemObjectDefinitionMetadataRegistry;

	@Reference
	private UserLocalService _userLocalService;

}