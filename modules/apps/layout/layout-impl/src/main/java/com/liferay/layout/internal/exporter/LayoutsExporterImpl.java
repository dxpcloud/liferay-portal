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

package com.liferay.layout.internal.exporter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.delivery.dto.v1_0.PageDefinition;
import com.liferay.layout.exporter.LayoutsExporter;
import com.liferay.layout.internal.headless.delivery.dto.v1_0.converter.DisplayPageTemplateDTOConverter;
import com.liferay.layout.internal.headless.delivery.dto.v1_0.converter.MasterPageDTOConverter;
import com.liferay.layout.internal.headless.delivery.dto.v1_0.converter.PageTemplateCollectionDTOConverter;
import com.liferay.layout.internal.headless.delivery.dto.v1_0.converter.PageTemplateDTOConverter;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.constants.LayoutPageTemplateExportImportConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.kernel.zip.ZipWriterFactory;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;

import java.io.File;

import java.util.List;

import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rubén Pulido
 */
@Component(immediate = true, service = LayoutsExporter.class)
public class LayoutsExporterImpl implements LayoutsExporter {

	@Override
	public File exportDisplayPages(long[] layoutPageTemplateEntryIds)
		throws Exception {

		return _exportLayoutPageTemplateEntries(
			layoutPageTemplateEntryIds,
			LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
			this::_populateDisplayPagesZipWriter);
	}

	@Override
	public File exportGroupLayoutPageTemplates(long groupId) throws Exception {
		DTOConverter<LayoutStructure, PageDefinition>
			pageDefinitionDTOConverter = _getPageDefinitionDTOConverter();
		ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

		List<LayoutPageTemplateEntry> layoutPageTemplateEntries =
			_layoutPageTemplateEntryLocalService.getLayoutPageTemplateEntries(
				groupId);

		try {
			for (LayoutPageTemplateEntry layoutPageTemplateEntry :
					layoutPageTemplateEntries) {

				if (layoutPageTemplateEntry.isDraft()) {
					continue;
				}

				if (layoutPageTemplateEntry.getType() ==
						LayoutPageTemplateEntryTypeConstants.TYPE_BASIC) {

					_populatePageTemplatesZipWriter(
						layoutPageTemplateEntry, pageDefinitionDTOConverter,
						zipWriter);
				}
				else if (layoutPageTemplateEntry.getType() ==
							LayoutPageTemplateEntryTypeConstants.
								TYPE_DISPLAY_PAGE) {

					_populateDisplayPagesZipWriter(
						layoutPageTemplateEntry, pageDefinitionDTOConverter,
						zipWriter);
				}
				else if (layoutPageTemplateEntry.getType() ==
							LayoutPageTemplateEntryTypeConstants.
								TYPE_MASTER_LAYOUT) {

					_populateMasterLayoutsZipWriter(
						layoutPageTemplateEntry, pageDefinitionDTOConverter,
						zipWriter);
				}
			}

			return zipWriter.getFile();
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	@Override
	public File exportMasterLayouts(long[] layoutPageTemplateEntryIds)
		throws Exception {

		return _exportLayoutPageTemplateEntries(
			layoutPageTemplateEntryIds,
			LayoutPageTemplateEntryTypeConstants.TYPE_MASTER_LAYOUT,
			this::_populateMasterLayoutsZipWriter);
	}

	@Override
	public File exportPageTemplates(long[] layoutPageTemplateEntryIds)
		throws Exception {

		DTOConverter<LayoutStructure, PageDefinition>
			pageDefinitionDTOConverter = _getPageDefinitionDTOConverter();
		ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

		try {
			for (long layoutPageTemplateEntryId : layoutPageTemplateEntryIds) {
				LayoutPageTemplateEntry layoutPageTemplateEntry =
					_layoutPageTemplateEntryLocalService.
						fetchLayoutPageTemplateEntry(layoutPageTemplateEntryId);

				if (layoutPageTemplateEntry.isDraft() ||
					(layoutPageTemplateEntry.getType() !=
						LayoutPageTemplateEntryTypeConstants.TYPE_BASIC)) {

					continue;
				}

				_populatePageTemplatesZipWriter(
					layoutPageTemplateEntry, pageDefinitionDTOConverter,
					zipWriter);
			}

			return zipWriter.getFile();
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	private File _exportLayoutPageTemplateEntries(
			long[] layoutPageTemplateEntryIds, int type,
			UnsafeTriConsumer
				<LayoutPageTemplateEntry,
				 DTOConverter<LayoutStructure, PageDefinition>, ZipWriter,
				 Exception> unsafeConsumer)
		throws Exception {

		DTOConverter<LayoutStructure, PageDefinition>
			pageDefinitionDTOConverter = _getPageDefinitionDTOConverter();
		ZipWriter zipWriter = _zipWriterFactory.getZipWriter();

		try {
			for (long layoutPageTemplateEntryId : layoutPageTemplateEntryIds) {
				LayoutPageTemplateEntry layoutPageTemplateEntry =
					_layoutPageTemplateEntryLocalService.
						fetchLayoutPageTemplateEntry(layoutPageTemplateEntryId);

				if (layoutPageTemplateEntry.isDraft() ||
					(layoutPageTemplateEntry.getType() != type)) {

					continue;
				}

				unsafeConsumer.accept(
					layoutPageTemplateEntry, pageDefinitionDTOConverter,
					zipWriter);
			}

			return zipWriter.getFile();
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	private DTOConverterContext _getDTOConverterContext(
		Layout layout, LayoutStructure layoutStructure) {

		DTOConverterContext dtoConverterContext =
			new DefaultDTOConverterContext(
				_dtoConverterRegistry, layoutStructure.getMainItemId(), null,
				null, null);

		dtoConverterContext.setAttribute("layout", layout);

		return dtoConverterContext;
	}

	private LayoutStructure _getLayoutStructure(Layout layout) {
		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					layout.getGroupId(), layout.getPlid());

		return LayoutStructure.of(
			layoutPageTemplateStructure.getDefaultSegmentsExperienceData());
	}

	private DTOConverter<LayoutStructure, PageDefinition>
		_getPageDefinitionDTOConverter() {

		return (DTOConverter<LayoutStructure, PageDefinition>)
			_dtoConverterRegistry.getDTOConverter(
				LayoutStructure.class.getName());
	}

	private FileEntry _getPreviewFileEntry(long previewFileEntryId) {
		if (previewFileEntryId <= 0) {
			return null;
		}

		try {
			return PortletFileRepositoryUtil.getPortletFileEntry(
				previewFileEntryId);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get file entry preview", portalException);
			}
		}

		return null;
	}

	private void _populateDisplayPagesZipWriter(
			LayoutPageTemplateEntry layoutPageTemplateEntry,
			DTOConverter<LayoutStructure, PageDefinition>
				pageDefinitionDTOConverter,
			ZipWriter zipWriter)
		throws Exception {

		String displayPagePath =
			"display-page-templates/" +
				layoutPageTemplateEntry.getLayoutPageTemplateEntryKey();

		SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

		FilterProvider filterProvider = simpleFilterProvider.addFilter(
			"Liferay.Vulcan", SimpleBeanPropertyFilter.serializeAll());

		ObjectWriter objectWriter = _objectMapper.writer(filterProvider);

		zipWriter.addEntry(
			displayPagePath + StringPool.SLASH +
				LayoutPageTemplateExportImportConstants.
					FILE_NAME_DISPLAY_PAGE_TEMPLATE,
			objectWriter.writeValueAsString(
				DisplayPageTemplateDTOConverter.toDTO(
					layoutPageTemplateEntry)));

		Layout layout = _layoutLocalService.fetchLayout(
			layoutPageTemplateEntry.getPlid());

		if (layout != null) {
			LayoutStructure layoutStructure = _getLayoutStructure(layout);

			PageDefinition pageDefinition = pageDefinitionDTOConverter.toDTO(
				_getDTOConverterContext(layout, layoutStructure),
				layoutStructure);

			zipWriter.addEntry(
				displayPagePath + "/page-definition.json",
				objectWriter.writeValueAsString(pageDefinition));
		}

		FileEntry previewFileEntry = _getPreviewFileEntry(
			layoutPageTemplateEntry.getPreviewFileEntryId());

		if (previewFileEntry != null) {
			zipWriter.addEntry(
				displayPagePath + "/thumbnail." +
					previewFileEntry.getExtension(),
				previewFileEntry.getContentStream());
		}
	}

	private void _populateMasterLayoutsZipWriter(
			LayoutPageTemplateEntry layoutPageTemplateEntry,
			DTOConverter<LayoutStructure, PageDefinition>
				pageDefinitionDTOConverter,
			ZipWriter zipWriter)
		throws Exception {

		String masterLayoutPath =
			"master-pages/" +
				layoutPageTemplateEntry.getLayoutPageTemplateEntryKey();

		SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

		FilterProvider filterProvider = simpleFilterProvider.addFilter(
			"Liferay.Vulcan", SimpleBeanPropertyFilter.serializeAll());

		ObjectWriter objectWriter = _objectMapper.writer(filterProvider);

		zipWriter.addEntry(
			masterLayoutPath + StringPool.SLASH +
				LayoutPageTemplateExportImportConstants.FILE_NAME_MASTER_PAGE,
			objectWriter.writeValueAsString(
				MasterPageDTOConverter.toDTO(layoutPageTemplateEntry)));

		Layout layout = _layoutLocalService.fetchLayout(
			layoutPageTemplateEntry.getPlid());

		if (layout != null) {
			LayoutStructure layoutStructure = _getLayoutStructure(layout);

			PageDefinition pageDefinition = pageDefinitionDTOConverter.toDTO(
				_getDTOConverterContext(layout, layoutStructure),
				layoutStructure);

			zipWriter.addEntry(
				masterLayoutPath + "/page-definition.json",
				objectWriter.writeValueAsString(pageDefinition));
		}

		FileEntry previewFileEntry = _getPreviewFileEntry(
			layoutPageTemplateEntry.getPreviewFileEntryId());

		if (previewFileEntry != null) {
			zipWriter.addEntry(
				masterLayoutPath + "/thumbnail." +
					previewFileEntry.getExtension(),
				previewFileEntry.getContentStream());
		}
	}

	private void _populatePageTemplatesZipWriter(
			LayoutPageTemplateEntry layoutPageTemplateEntry,
			DTOConverter<LayoutStructure, PageDefinition>
				pageDefinitionDTOConverter,
			ZipWriter zipWriter)
		throws Exception {

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			_layoutPageTemplateCollectionLocalService.
				getLayoutPageTemplateCollection(
					layoutPageTemplateEntry.
						getLayoutPageTemplateCollectionId());

		String layoutPageTemplateCollectionKey =
			layoutPageTemplateCollection.getLayoutPageTemplateCollectionKey();

		String layoutPageTemplateCollectionPath =
			"page-templates/" + layoutPageTemplateCollectionKey;

		SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

		FilterProvider filterProvider = simpleFilterProvider.addFilter(
			"Liferay.Vulcan", SimpleBeanPropertyFilter.serializeAll());

		ObjectWriter objectWriter = _objectMapper.writer(filterProvider);

		zipWriter.addEntry(
			layoutPageTemplateCollectionPath + StringPool.SLASH +
				LayoutPageTemplateExportImportConstants.
					FILE_NAME_PAGE_TEMPLATE_COLLECTION,
			objectWriter.writeValueAsString(
				PageTemplateCollectionDTOConverter.toDTO(
					layoutPageTemplateCollection)));

		String layoutPageTemplateEntryPath =
			layoutPageTemplateCollectionPath + StringPool.SLASH +
				layoutPageTemplateEntry.getLayoutPageTemplateEntryKey();

		zipWriter.addEntry(
			layoutPageTemplateEntryPath + StringPool.SLASH +
				LayoutPageTemplateExportImportConstants.FILE_NAME_PAGE_TEMPLATE,
			objectWriter.writeValueAsString(
				PageTemplateDTOConverter.toDTO(layoutPageTemplateEntry)));

		Layout layout = _layoutLocalService.fetchLayout(
			layoutPageTemplateEntry.getPlid());

		if (layout != null) {
			LayoutStructure layoutStructure = _getLayoutStructure(layout);

			PageDefinition pageDefinition = pageDefinitionDTOConverter.toDTO(
				_getDTOConverterContext(layout, layoutStructure),
				layoutStructure);

			zipWriter.addEntry(
				layoutPageTemplateEntryPath + "/page-definition.json",
				objectWriter.writeValueAsString(pageDefinition));
		}

		FileEntry previewFileEntry = _getPreviewFileEntry(
			layoutPageTemplateEntry.getPreviewFileEntryId());

		if (previewFileEntry != null) {
			zipWriter.addEntry(
				layoutPageTemplateEntryPath + "/thumbnail." +
					previewFileEntry.getExtension(),
				previewFileEntry.getContentStream());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutsExporterImpl.class);

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
			enable(SerializationFeature.INDENT_OUTPUT);
			setDateFormat(new ISO8601DateFormat());
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
			setVisibility(
				PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			setVisibility(
				PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
		}
	};

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateCollectionLocalService
		_layoutPageTemplateCollectionLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private ZipWriterFactory _zipWriterFactory;

}