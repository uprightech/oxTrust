/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.gluu.oxtrust.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.gluu.oxauth.model.common.ScopeType;
import org.gluu.persist.annotation.AttributeName;
import org.gluu.persist.annotation.DataEntry;
import org.gluu.persist.annotation.ObjectClass;
import org.gluu.persist.model.base.Entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * oxAuth scope
 * 
 * @author Reda Zerrad Date: 06/18/2012
 * @author Yuriy Movchan Date: 03/21/2014
 */
@DataEntry(sortBy = { "displayName" })
@ObjectClass(values = { "top", "oxAuthCustomScope" })
@JsonInclude(Include.NON_NULL)
public class OxAuthScope extends Entry implements Serializable {

	private static final long serialVersionUID = 4308826784917052508L;

	private transient boolean selected;

	@AttributeName(ignoreDuringUpdate = true)
	private String inum;

	@NotNull
	@Size(min = 0, max = 60, message = "Length of the Display Name should not exceed 60")
	@AttributeName
	private String displayName;

	@Size(min = 0, max = 4000, message = "Length of the Description should not exceed 4000")
	@AttributeName
	private String description;

    @AttributeName(name = "oxScopeType")
    private ScopeType scopeType;

	@AttributeName(name = "oxAuthClaim")
	private List<String> oxAuthClaims;

	@AttributeName(name = "defaultScope")
	private Boolean defaultScope;

	@AttributeName(name = "oxScriptDn")
    private List<String> dynamicScopeScripts;

	public String getInum() {
		return this.inum;
	}

	public void setInum(String inum) {
		this.inum = inum;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ScopeType getScopeType() {
		return scopeType;
	}

	public void setScopeType(ScopeType scopeType) {
		this.scopeType = scopeType;
	}

	public List<String> getOxAuthClaims() {
		return this.oxAuthClaims;
	}

	public void setOxAuthClaims(List<String> oxAuthClaims) {
		this.oxAuthClaims = oxAuthClaims;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Boolean getDefaultScope() {
		return this.defaultScope;
	}

	public void setDefaultScope(Boolean defaultScope) {
		this.defaultScope = defaultScope;
	}

    public List<String> getDynamicScopeScripts() {
		return dynamicScopeScripts;
	}

	public void setDynamicScopeScripts(List<String> dynamicScopeScripts) {
        this.dynamicScopeScripts = dynamicScopeScripts;
    }
	
	@Override
	public String toString() {
		return "OxAuthScope [inum=" + inum + ", displayName=" + displayName + ", description=" + description + ", oxAuthClaims="
				+ oxAuthClaims + ", defaultScope=" + defaultScope + "]";
	}
}
