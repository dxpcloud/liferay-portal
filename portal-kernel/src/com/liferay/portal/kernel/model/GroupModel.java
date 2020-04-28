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

package com.liferay.portal.kernel.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.model.change.tracking.CTModel;

import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the Group service. Represents a row in the &quot;Group_&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.model.impl.GroupModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.model.impl.GroupImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Group
 * @generated
 */
@ProviderType
public interface GroupModel
	extends AttachedModel, BaseModel<Group>, CTModel<Group>, LocalizedModel,
			MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a group model instance should use the {@link Group} interface instead.
	 */

	/**
	 * Returns the primary key of this group.
	 *
	 * @return the primary key of this group
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this group.
	 *
	 * @param primaryKey the primary key of this group
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this group.
	 *
	 * @return the mvcc version of this group
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this group.
	 *
	 * @param mvccVersion the mvcc version of this group
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the ct collection ID of this group.
	 *
	 * @return the ct collection ID of this group
	 */
	@Override
	public long getCtCollectionId();

	/**
	 * Sets the ct collection ID of this group.
	 *
	 * @param ctCollectionId the ct collection ID of this group
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId);

	/**
	 * Returns the uuid of this group.
	 *
	 * @return the uuid of this group
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this group.
	 *
	 * @param uuid the uuid of this group
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the group ID of this group.
	 *
	 * @return the group ID of this group
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this group.
	 *
	 * @param groupId the group ID of this group
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this group.
	 *
	 * @return the company ID of this group
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this group.
	 *
	 * @param companyId the company ID of this group
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the creator user ID of this group.
	 *
	 * @return the creator user ID of this group
	 */
	public long getCreatorUserId();

	/**
	 * Sets the creator user ID of this group.
	 *
	 * @param creatorUserId the creator user ID of this group
	 */
	public void setCreatorUserId(long creatorUserId);

	/**
	 * Returns the creator user uuid of this group.
	 *
	 * @return the creator user uuid of this group
	 */
	public String getCreatorUserUuid();

	/**
	 * Sets the creator user uuid of this group.
	 *
	 * @param creatorUserUuid the creator user uuid of this group
	 */
	public void setCreatorUserUuid(String creatorUserUuid);

	/**
	 * Returns the fully qualified class name of this group.
	 *
	 * @return the fully qualified class name of this group
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this group.
	 *
	 * @return the class name ID of this group
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this group.
	 *
	 * @param classNameId the class name ID of this group
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class pk of this group.
	 *
	 * @return the class pk of this group
	 */
	@Override
	public long getClassPK();

	/**
	 * Sets the class pk of this group.
	 *
	 * @param classPK the class pk of this group
	 */
	@Override
	public void setClassPK(long classPK);

	/**
	 * Returns the parent group ID of this group.
	 *
	 * @return the parent group ID of this group
	 */
	public long getParentGroupId();

	/**
	 * Sets the parent group ID of this group.
	 *
	 * @param parentGroupId the parent group ID of this group
	 */
	public void setParentGroupId(long parentGroupId);

	/**
	 * Returns the live group ID of this group.
	 *
	 * @return the live group ID of this group
	 */
	public long getLiveGroupId();

	/**
	 * Sets the live group ID of this group.
	 *
	 * @param liveGroupId the live group ID of this group
	 */
	public void setLiveGroupId(long liveGroupId);

	/**
	 * Returns the tree path of this group.
	 *
	 * @return the tree path of this group
	 */
	@AutoEscape
	public String getTreePath();

	/**
	 * Sets the tree path of this group.
	 *
	 * @param treePath the tree path of this group
	 */
	public void setTreePath(String treePath);

	/**
	 * Returns the group key of this group.
	 *
	 * @return the group key of this group
	 */
	@AutoEscape
	public String getGroupKey();

	/**
	 * Sets the group key of this group.
	 *
	 * @param groupKey the group key of this group
	 */
	public void setGroupKey(String groupKey);

	/**
	 * Returns the name of this group.
	 *
	 * @return the name of this group
	 */
	public String getName();

	/**
	 * Returns the localized name of this group in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized name of this group
	 */
	@AutoEscape
	public String getName(Locale locale);

	/**
	 * Returns the localized name of this group in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this group. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getName(Locale locale, boolean useDefault);

	/**
	 * Returns the localized name of this group in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized name of this group
	 */
	@AutoEscape
	public String getName(String languageId);

	/**
	 * Returns the localized name of this group in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this group
	 */
	@AutoEscape
	public String getName(String languageId, boolean useDefault);

	@AutoEscape
	public String getNameCurrentLanguageId();

	@AutoEscape
	public String getNameCurrentValue();

	/**
	 * Returns a map of the locales and localized names of this group.
	 *
	 * @return the locales and localized names of this group
	 */
	public Map<Locale, String> getNameMap();

	/**
	 * Sets the name of this group.
	 *
	 * @param name the name of this group
	 */
	public void setName(String name);

	/**
	 * Sets the localized name of this group in the language.
	 *
	 * @param name the localized name of this group
	 * @param locale the locale of the language
	 */
	public void setName(String name, Locale locale);

	/**
	 * Sets the localized name of this group in the language, and sets the default locale.
	 *
	 * @param name the localized name of this group
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setName(String name, Locale locale, Locale defaultLocale);

	public void setNameCurrentLanguageId(String languageId);

	/**
	 * Sets the localized names of this group from the map of locales and localized names.
	 *
	 * @param nameMap the locales and localized names of this group
	 */
	public void setNameMap(Map<Locale, String> nameMap);

	/**
	 * Sets the localized names of this group from the map of locales and localized names, and sets the default locale.
	 *
	 * @param nameMap the locales and localized names of this group
	 * @param defaultLocale the default locale
	 */
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale);

	/**
	 * Returns the description of this group.
	 *
	 * @return the description of this group
	 */
	public String getDescription();

	/**
	 * Returns the localized description of this group in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized description of this group
	 */
	@AutoEscape
	public String getDescription(Locale locale);

	/**
	 * Returns the localized description of this group in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this group. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getDescription(Locale locale, boolean useDefault);

	/**
	 * Returns the localized description of this group in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized description of this group
	 */
	@AutoEscape
	public String getDescription(String languageId);

	/**
	 * Returns the localized description of this group in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this group
	 */
	@AutoEscape
	public String getDescription(String languageId, boolean useDefault);

	@AutoEscape
	public String getDescriptionCurrentLanguageId();

	@AutoEscape
	public String getDescriptionCurrentValue();

	/**
	 * Returns a map of the locales and localized descriptions of this group.
	 *
	 * @return the locales and localized descriptions of this group
	 */
	public Map<Locale, String> getDescriptionMap();

	/**
	 * Sets the description of this group.
	 *
	 * @param description the description of this group
	 */
	public void setDescription(String description);

	/**
	 * Sets the localized description of this group in the language.
	 *
	 * @param description the localized description of this group
	 * @param locale the locale of the language
	 */
	public void setDescription(String description, Locale locale);

	/**
	 * Sets the localized description of this group in the language, and sets the default locale.
	 *
	 * @param description the localized description of this group
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setDescription(
		String description, Locale locale, Locale defaultLocale);

	public void setDescriptionCurrentLanguageId(String languageId);

	/**
	 * Sets the localized descriptions of this group from the map of locales and localized descriptions.
	 *
	 * @param descriptionMap the locales and localized descriptions of this group
	 */
	public void setDescriptionMap(Map<Locale, String> descriptionMap);

	/**
	 * Sets the localized descriptions of this group from the map of locales and localized descriptions, and sets the default locale.
	 *
	 * @param descriptionMap the locales and localized descriptions of this group
	 * @param defaultLocale the default locale
	 */
	public void setDescriptionMap(
		Map<Locale, String> descriptionMap, Locale defaultLocale);

	/**
	 * Returns the type of this group.
	 *
	 * @return the type of this group
	 */
	public int getType();

	/**
	 * Sets the type of this group.
	 *
	 * @param type the type of this group
	 */
	public void setType(int type);

	/**
	 * Returns the type settings of this group.
	 *
	 * @return the type settings of this group
	 */
	@AutoEscape
	public String getTypeSettings();

	/**
	 * Sets the type settings of this group.
	 *
	 * @param typeSettings the type settings of this group
	 */
	public void setTypeSettings(String typeSettings);

	/**
	 * Returns the manual membership of this group.
	 *
	 * @return the manual membership of this group
	 */
	public boolean getManualMembership();

	/**
	 * Returns <code>true</code> if this group is manual membership.
	 *
	 * @return <code>true</code> if this group is manual membership; <code>false</code> otherwise
	 */
	public boolean isManualMembership();

	/**
	 * Sets whether this group is manual membership.
	 *
	 * @param manualMembership the manual membership of this group
	 */
	public void setManualMembership(boolean manualMembership);

	/**
	 * Returns the membership restriction of this group.
	 *
	 * @return the membership restriction of this group
	 */
	public int getMembershipRestriction();

	/**
	 * Sets the membership restriction of this group.
	 *
	 * @param membershipRestriction the membership restriction of this group
	 */
	public void setMembershipRestriction(int membershipRestriction);

	/**
	 * Returns the friendly url of this group.
	 *
	 * @return the friendly url of this group
	 */
	@AutoEscape
	public String getFriendlyURL();

	/**
	 * Sets the friendly url of this group.
	 *
	 * @param friendlyURL the friendly url of this group
	 */
	public void setFriendlyURL(String friendlyURL);

	/**
	 * Returns the site of this group.
	 *
	 * @return the site of this group
	 */
	public boolean getSite();

	/**
	 * Returns <code>true</code> if this group is site.
	 *
	 * @return <code>true</code> if this group is site; <code>false</code> otherwise
	 */
	public boolean isSite();

	/**
	 * Sets whether this group is site.
	 *
	 * @param site the site of this group
	 */
	public void setSite(boolean site);

	/**
	 * Returns the remote staging group count of this group.
	 *
	 * @return the remote staging group count of this group
	 */
	public int getRemoteStagingGroupCount();

	/**
	 * Sets the remote staging group count of this group.
	 *
	 * @param remoteStagingGroupCount the remote staging group count of this group
	 */
	public void setRemoteStagingGroupCount(int remoteStagingGroupCount);

	/**
	 * Returns the inherit content of this group.
	 *
	 * @return the inherit content of this group
	 */
	public boolean getInheritContent();

	/**
	 * Returns <code>true</code> if this group is inherit content.
	 *
	 * @return <code>true</code> if this group is inherit content; <code>false</code> otherwise
	 */
	public boolean isInheritContent();

	/**
	 * Sets whether this group is inherit content.
	 *
	 * @param inheritContent the inherit content of this group
	 */
	public void setInheritContent(boolean inheritContent);

	/**
	 * Returns the active of this group.
	 *
	 * @return the active of this group
	 */
	public boolean getActive();

	/**
	 * Returns <code>true</code> if this group is active.
	 *
	 * @return <code>true</code> if this group is active; <code>false</code> otherwise
	 */
	public boolean isActive();

	/**
	 * Sets whether this group is active.
	 *
	 * @param active the active of this group
	 */
	public void setActive(boolean active);

	@Override
	public String[] getAvailableLanguageIds();

	@Override
	public String getDefaultLanguageId();

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException;

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException;

}