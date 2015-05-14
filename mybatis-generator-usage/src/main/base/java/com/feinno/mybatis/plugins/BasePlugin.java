/*
 * File Name：BasePlugin.java
 * Package Name：com.feinno.mybatis.plugins
 *
 * Function： TODO 
 *
 *   ver     date      author               department
 * ──────────────────────────────────————————————————————————————
 *   V1.0   2013-1-9    yzy		  DATA BUSINESS DEPARTMENT
 *
 * Copyright (c) 2013, Feinno Communication Tech All Rights Reserved.
 */

package com.feinno.mybatis.plugins;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * ClassName:BasePlugin Reason: TODO
 *
 * @author yzy
 * @version V1.0.0
 * @since 1.0
 * @Date 2013-1-9 下午02:02:32
 *
 */
public abstract class BasePlugin extends PluginAdapter {

	protected String calculateJavaModelPackage(
			FullyQualifiedTable fullyQualifiedTable) {
		JavaModelGeneratorConfiguration config = context
				.getJavaModelGeneratorConfiguration();

		StringBuilder sb = new StringBuilder();
		sb.append(config.getTargetPackage());
		sb.append(fullyQualifiedTable
				.getSubPackage(isSubPackagesEnabled(config)));

		return sb.toString();
	}

	protected boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
		return isTrue(propertyHolder
				.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
	}

	protected FullyQualifiedJavaType getKType(
			IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pks = introspectedTable.getPrimaryKeyColumns();
		FullyQualifiedJavaType pktype = null;
		if (pks != null && pks.size() == 1) {
			IntrospectedColumn pk = pks.get(0);
			pktype = pk.getFullyQualifiedJavaType();
		} else if (pks.size() > 1) {
			pktype = new FullyQualifiedJavaType(
					introspectedTable.getPrimaryKeyType());
		}

		return pktype;
	}

	protected FullyQualifiedJavaType getTType(
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType type = null;
		@SuppressWarnings("rawtypes")
		List cols = introspectedTable.getBaseColumns();
		if (cols != null && cols.size() > 0) {
			type = new FullyQualifiedJavaType(
					introspectedTable.getBaseRecordType());
		} else {
			type = getKType(introspectedTable);
		}

		return type;
	}

	// extendMapper = (properties.getProperty("extendMapper"));
	// extendBLOBsMapper = (properties.getProperty("extendBLOBsMapper"));
	// extendManager = (properties.getProperty("extendManager"));
	// extendBLOBsManager = (properties.getProperty("extendBLOBsManager"));

	protected FullyQualifiedJavaType getBaseMapperType(
			IntrospectedTable introspectedTable) {

		String extendMapper = "";

		if (introspectedTable.getBLOBColumns() == null
				|| introspectedTable.getBLOBColumns().size() == 0) {
			extendMapper = properties.getProperty("extendMapper");
		} else {
			extendMapper = properties.getProperty("extendBLOBsMapper");
		}

		FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(
				extendMapper);

		// K
		fullyQualifiedJavaType.addTypeArgument(getKType(introspectedTable));

		// T
		fullyQualifiedJavaType.addTypeArgument(getTType(introspectedTable));

		// E
		fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()));

		return fullyQualifiedJavaType;
	}

	protected FullyQualifiedJavaType getExtendManagerType(
			IntrospectedTable introspectedTable) {
		String extendManager = "";

		if (introspectedTable.getBLOBColumns() == null
				|| introspectedTable.getBLOBColumns().size() == 0) {
			extendManager = properties.getProperty("extendManager");
		} else {
			extendManager = properties.getProperty("extendBLOBsManager");
		}

		FullyQualifiedJavaType extendManagerType = new FullyQualifiedJavaType(
				extendManager);

		// K
		extendManagerType.addTypeArgument(getKType(introspectedTable));

		// T
		extendManagerType.addTypeArgument(getTType(introspectedTable));

		// E
		extendManagerType.addTypeArgument(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()));

		return extendManagerType;
	}

	// targetProject = (properties.getProperty("targetProject"));
	// targetPackage = (properties.getProperty("targetPackage"));

	protected FullyQualifiedJavaType getManagerType(
			IntrospectedTable introspectedTable) {
		String targetPackage = properties.getProperty("targetPackage");
		FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());

		// manager type
		FullyQualifiedJavaType managerType = new FullyQualifiedJavaType(
				targetPackage + "." + baseRecordType.getShortName() + "Manager");

		return managerType;
	}
	
	protected boolean hasModelPrimaryKeyClass (IntrospectedTable introspectedTable) {
		
		if (introspectedTable.getPrimaryKeyColumns() != null && introspectedTable.getPrimaryKeyColumns().size() > 1) {
			return true;
		}
		
		return false;
	}
}
