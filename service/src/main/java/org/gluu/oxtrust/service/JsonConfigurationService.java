/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.service;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gluu.config.oxtrust.AppConfiguration;
import org.gluu.config.oxtrust.AttributeResolverConfiguration;
import org.gluu.config.oxtrust.CacheRefreshConfiguration;
import org.gluu.config.oxtrust.DbApplicationConfiguration;
import org.gluu.config.oxtrust.ImportPersonConfig;
import org.gluu.config.oxtrust.LdapOxAuthConfiguration;
import org.gluu.config.oxtrust.LdapOxTrustConfiguration;
import org.gluu.service.config.ConfigurationFactory;
import org.gluu.oxtrust.model.GluuConfiguration;
import org.gluu.persist.PersistenceEntryManager;
import org.gluu.persist.exception.BasePersistenceException;
import org.gluu.service.JsonService;
import org.gluu.service.cache.CacheConfiguration;
import org.gluu.service.document.store.conf.DocumentStoreConfiguration;
import org.slf4j.Logger;

/**
 * Provides operations with JSON oxAuth/oxTrust configuration
 * 
 * @author Yuriy Movchan Date: 12.15.2010
 */
@ApplicationScoped
@Named("jsonConfigurationService")
public class JsonConfigurationService implements Serializable {

	private static final long serialVersionUID = -3840968275007784641L;

	@Inject
	private Logger log;

	@Inject
	private PersistenceEntryManager persistenceEntryManager;
	@Inject
	private JsonService jsonService;

	@Inject
	private ConfigurationFactory<?> configurationFactory;

	@Inject
	private ConfigurationService configurationService;

	public AppConfiguration getOxTrustappConfiguration() {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		return ldapOxTrustConfiguration.getApplication();
	}

	public CacheConfiguration getOxMemCacheConfiguration() {
		CacheConfiguration cachedConfiguration = configurationService.getConfiguration().getCacheConfiguration();
		return cachedConfiguration;
	}

	public DocumentStoreConfiguration getDocumentStoreConfiguration() {
		DocumentStoreConfiguration documentStoreConfiguration = configurationService.getConfiguration().getDocumentStoreConfiguration();
		return documentStoreConfiguration;
	}

	public ImportPersonConfig getOxTrustImportPersonConfiguration() {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		return ldapOxTrustConfiguration.getImportPersonConfig();
	}

	public CacheRefreshConfiguration getOxTrustCacheRefreshConfiguration() {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		return ldapOxTrustConfiguration.getCacheRefresh();
	}

	private LdapOxTrustConfiguration getOxTrustConfiguration() {
		String configurationDn = configurationFactory.getConfigurationDn();

		LdapOxTrustConfiguration ldapOxTrustConfiguration = loadOxTrustConfig(configurationDn);
		return ldapOxTrustConfiguration;
	}

	public String getOxAuthDynamicConfigJson() throws IOException {
		String configurationDn = configurationFactory.getConfigurationDn();

		LdapOxAuthConfiguration ldapOxAuthConfiguration = loadOxAuthConfig(configurationDn);
		return ldapOxAuthConfiguration.getOxAuthConfigDynamic();
	}

	public org.gluu.oxauth.model.configuration.AppConfiguration getOxauthAppConfiguration() throws IOException {
		return jsonService.jsonToObject(getOxAuthDynamicConfigJson(),
				org.gluu.oxauth.model.configuration.AppConfiguration.class);
	}

	public boolean saveOxTrustappConfiguration(AppConfiguration oxTrustappConfiguration) {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		ldapOxTrustConfiguration.setApplication(oxTrustappConfiguration);
		ldapOxTrustConfiguration.setRevision(ldapOxTrustConfiguration.getRevision() + 1);
		persistenceEntryManager.merge(ldapOxTrustConfiguration);
		return true;
	}

	public boolean saveOxTrustImportPersonConfiguration(ImportPersonConfig oxTrustImportPersonConfiguration) {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		ldapOxTrustConfiguration.setImportPersonConfig(oxTrustImportPersonConfiguration);
		ldapOxTrustConfiguration.setRevision(ldapOxTrustConfiguration.getRevision() + 1);
		persistenceEntryManager.merge(ldapOxTrustConfiguration);
		return true;
	}

	public boolean saveOxTrustCacheRefreshConfiguration(CacheRefreshConfiguration oxTrustCacheRefreshConfiguration) {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		ldapOxTrustConfiguration.setCacheRefresh(oxTrustCacheRefreshConfiguration);
		ldapOxTrustConfiguration.setRevision(ldapOxTrustConfiguration.getRevision() + 1);
		persistenceEntryManager.merge(ldapOxTrustConfiguration);
		return true;
	}

	public boolean saveOxTrustAttributeResolverConfigurationConfiguration(AttributeResolverConfiguration attributeResolverConfiguration) {
		LdapOxTrustConfiguration ldapOxTrustConfiguration = getOxTrustConfiguration();
		ldapOxTrustConfiguration.setAttributeResolverConfig(attributeResolverConfiguration);
		ldapOxTrustConfiguration.setRevision(ldapOxTrustConfiguration.getRevision() + 1);
		persistenceEntryManager.merge(ldapOxTrustConfiguration);
		return true;
	}

	public boolean saveOxAuthAppConfiguration(org.gluu.oxauth.model.configuration.AppConfiguration appConfiguration) {
		try {
			String appConfigurationJson = jsonService.objectToJson(appConfiguration);
			return saveOxAuthDynamicConfigJson(appConfigurationJson);
		} catch (IOException e) {
			log.error("Failed to serialize AppConfiguration", e);
		}
		return false;
	}

	public boolean saveOxAuthDynamicConfigJson(String oxAuthDynamicConfigJson)
			throws IOException {
		String configurationDn = configurationFactory.getConfigurationDn();

		LdapOxAuthConfiguration ldapOxAuthConfiguration = loadOxAuthConfig(configurationDn);
		ldapOxAuthConfiguration.setOxAuthConfigDynamic(oxAuthDynamicConfigJson);
		ldapOxAuthConfiguration.setRevision(ldapOxAuthConfiguration.getRevision() + 1);
		persistenceEntryManager.merge(ldapOxAuthConfiguration);
		return true;
	}

	public boolean saveOxMemCacheConfiguration(CacheConfiguration cachedConfiguration) {
		GluuConfiguration gluuConfiguration = configurationService.getConfiguration();
		gluuConfiguration.setCacheConfiguration(cachedConfiguration);
		configurationService.updateConfiguration(gluuConfiguration);
		return true;
	}

	public boolean saveDocumentStoreConfiguration(DocumentStoreConfiguration documentStoreConfiguration) {
		GluuConfiguration gluuConfiguration = configurationService.getConfiguration();
		gluuConfiguration.setDocumentStoreConfiguration(documentStoreConfiguration);
		configurationService.updateConfiguration(gluuConfiguration);
		return true;
	}

	private LdapOxTrustConfiguration loadOxTrustConfig(String configurationDn) {
		try {
			LdapOxTrustConfiguration conf = persistenceEntryManager.find(LdapOxTrustConfiguration.class, configurationDn);

			return conf;
		} catch (BasePersistenceException ex) {
			log.error("Failed to load configuration from LDAP");
		}

		return null;
	}

	private LdapOxAuthConfiguration loadOxAuthConfig(String configurationDn) {
		try {
			configurationDn = configurationDn.replace("ou=oxtrust", "ou=oxauth");
			LdapOxAuthConfiguration conf = persistenceEntryManager.find(LdapOxAuthConfiguration.class, configurationDn);
			return conf;
		} catch (BasePersistenceException ex) {
			log.error("Failed to load configuration from LDAP");
		}

		return null;
	}

	public DbApplicationConfiguration loadFido2Configuration() {
		try {
			String configurationDn = configurationFactory.getBaseConfiguration().getString("fido2_ConfigurationEntryDN");
			DbApplicationConfiguration conf = persistenceEntryManager.find(DbApplicationConfiguration.class, configurationDn);
			return conf;
		} catch (BasePersistenceException ex) {
			log.error("Failed to load Fido2 configuration from LDAP");
		}

		return null;
	}

	public void saveFido2Configuration(String fido2ConfigJson) {
		DbApplicationConfiguration fido2Configuration = loadFido2Configuration();
		
		fido2Configuration.setDynamicConf(fido2ConfigJson);
		fido2Configuration.setRevision(fido2Configuration.getRevision() + 1);
		persistenceEntryManager.merge(fido2Configuration);
	}
	
}
