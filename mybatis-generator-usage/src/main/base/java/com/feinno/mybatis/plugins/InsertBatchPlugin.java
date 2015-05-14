package com.feinno.mybatis.plugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class InsertBatchPlugin extends BasePlugin {

	private boolean enable;

	private String targetProject;

	private String targetPackage;

	private String extendMapper;

	private String extendBLOBsMapper;

	private String extendManager;

	private String extendBLOBsManager;
	
	private String extendModule;

	@Override
	public void initialized(IntrospectedTable introspectedTable) {

		targetProject = (properties.getProperty("targetProject"));

		targetPackage = (properties.getProperty("targetPackage"));

		extendModule = (properties.getProperty("extendModule"));
		
		extendMapper = (properties.getProperty("extendMapper"));

		extendBLOBsMapper = (properties.getProperty("extendBLOBsMapper"));

		extendManager = (properties.getProperty("extendManager"));

		extendBLOBsManager = (properties.getProperty("extendBLOBsManager"));

		enable = StringUtility.isTrue(properties.getProperty("enable"));

	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
			IntrospectedTable introspectedTable) {

		List<GeneratedJavaFile> list = new ArrayList<GeneratedJavaFile>();

		list.add(createGeneratedJavaManagerFile(introspectedTable));

		return list;
	}

	/**
	 * 创建JavaManagerImpl
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private GeneratedJavaFile createGeneratedJavaManagerFile(
			IntrospectedTable introspectedTable) {

		// // base java module type
		// FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(
		// introspectedTable.getBaseRecordType());

		// manager type
		FullyQualifiedJavaType managerType = getManagerType(introspectedTable);

		// mapper type
		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());

		// public
		TopLevelClass topLevelClass = new TopLevelClass(managerType);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);

		// Annotation
		topLevelClass.addAnnotation("@Component(\""
				+ managerType.getShortName().substring(0, 1).toLowerCase()
				+ managerType.getShortName().substring(1) + "\")");

		FullyQualifiedJavaType extendManagerType = getExtendManagerType(
				introspectedTable);

		// add mapper field
		Field fieldMapper = new Field();
		fieldMapper.addAnnotation("@Autowired");
		fieldMapper.setVisibility(JavaVisibility.PRIVATE);
		fieldMapper.setType(mapperType);
		fieldMapper.setName(mapperType.getShortName().substring(0, 1)
				.toLowerCase()
				+ mapperType.getShortName().substring(1));
		topLevelClass.addField(fieldMapper);

		// add base mapper Method
		Method baseMapperMethod = new Method();
		baseMapperMethod.addAnnotation("@Override");
		baseMapperMethod.setVisibility(JavaVisibility.PUBLIC);
		baseMapperMethod.setName("getMapper");
		baseMapperMethod.setReturnType(getBaseMapperType(introspectedTable));
		StringBuffer line = new StringBuffer();
		line.append("return ");
		line.append(mapperType.getShortName().substring(0, 1).toLowerCase()
				+ mapperType.getShortName().substring(1));
		line.append(";");
		baseMapperMethod.addBodyLine(line.toString());
		topLevelClass.addMethod(baseMapperMethod);

		// add Imported
		topLevelClass
				.addImportedType("org.springframework.beans.factory.annotation.Autowired");
		topLevelClass
				.addImportedType("org.springframework.stereotype.Component");
		topLevelClass.addImportedType(extendManagerType);
		topLevelClass.addImportedType(mapperType);
		topLevelClass.addImportedType(getBaseMapperType(introspectedTable));

		// //import <K,T,E>
		// topLevelClass.addImportedType(getKType(introspectedTable));
		// topLevelClass.addImportedType(getTType(introspectedTable));
		// topLevelClass.addImportedType(introspectedTable.getExampleType());

		topLevelClass.setSuperClass(extendManagerType);

		// java class
		GeneratedJavaFile gjf = new GeneratedJavaFile(
				topLevelClass,
				targetProject,
				context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
				context.getJavaFormatter());

		return gjf;
	}
	
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
		if (StringUtility.stringHasValue(extendModule) && hasModelPrimaryKeyClass(introspectedTable) == true) {
			FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(extendModule);
			
			topLevelClass.setSuperClass(fullyQualifiedJavaType);
			
			topLevelClass.addImportedType(fullyQualifiedJavaType);
		}
		
        return true;
    }
	
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
		
		if (StringUtility.stringHasValue(extendModule) && hasModelPrimaryKeyClass(introspectedTable) == false) {
			FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(extendModule);
			
			topLevelClass.setSuperClass(fullyQualifiedJavaType);
			
			topLevelClass.addImportedType(fullyQualifiedJavaType);
		}
		
        return true;
    }
	
//	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
//			IntrospectedTable introspectedTable) {
//		
//		if (StringUtility.stringHasValue(extendModule)) {
//			FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(extendExample);
//			
//			topLevelClass.setSuperClass(fullyQualifiedJavaType);
//			
//			topLevelClass.addImportedType(fullyQualifiedJavaType);
//		}
//		
//		return true;
//	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for limit clause
		// addPage(topLevelClass, introspectedTable, "page");
		// addDialect(topLevelClass, introspectedTable, "dialect");
		return super.modelExampleClassGenerated(topLevelClass,
				introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		appendBaseMapper(interfaze, topLevelClass, introspectedTable);

		return true;
	}

	/**
	 * extends base mapper
	 * 
	 * @param interfaze
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	private void appendBaseMapper(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// mapper type
//		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
//				introspectedTable.getMyBatis3JavaMapperType());

//		// Annotation
//		interfaze.addAnnotation("@Component(\""
//				+ mapperType.getShortName().substring(0, 1).toLowerCase()
//				+ mapperType.getShortName().substring(1) + "\")");
//		interfaze.addImportedType(new FullyQualifiedJavaType(
//				"org.springframework.stereotype.Component"));
		
		interfaze.addImportedType(getBaseMapperType(introspectedTable));
		interfaze.addSuperInterface(getBaseMapperType(introspectedTable));

		
		
		interfaze.getMethods().clear();
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		// XmlElement parentElement = document.getRootElement();
		//
		// // 产生分页语句前半部分
		// XmlElement paginationPrefixElement = new XmlElement("sql");
		// paginationPrefixElement.addAttribute(new Attribute("id",
		// "OracleDialectPrefix"));
		// XmlElement pageStart = new XmlElement("if");
		// pageStart.addAttribute(new Attribute("test", "page != null"));
		// XmlElement pageDialect1 = new XmlElement("if");
		// pageDialect1.addAttribute(new Attribute("test",
		// "dialect == 'oralce'"));
		// pageStart.addElement(pageDialect1);
		// pageDialect1.addElement(new TextElement(
		// "select * from ( select row_.*, rownum rownum_ from ( "));
		// paginationPrefixElement.addElement(pageStart);
		// parentElement.addElement(paginationPrefixElement);
		//
		// // 产生分页语句后半部分
		// // XmlElement paginationSuffixElement = new XmlElement("sql");
		// // paginationSuffixElement.addAttribute(new Attribute("id",
		// // "OracleDialectSuffix"));
		// // XmlElement pageEnd = new XmlElement("if");
		// // pageEnd.addAttribute(new Attribute("test", "page != null"));
		// // XmlElement pageDialect2 = new XmlElement("if");
		// // pageDialect2.addAttribute(new Attribute("test",
		// "dialect == 'oralce'"));
		// // pageEnd.addElement(pageDialect2);
		// // pageDialect2
		// // .addElement(new TextElement(
		// //
		// "<![CDATA[ ) row_ ) where rownum_ > #{page.begin} and rownum_ <= #{page.end} ]]>"));
		//
		// // ----- mysql语句。
		// // XmlElement mysqlDialect = new XmlElement("if");
		// // mysqlDialect.addAttribute(new Attribute("test",
		// "dialect == 'mysql'"));
		// // pageEnd.addElement(mysqlDialect);
		// // mysqlDialect.addElement(new TextElement(
		// // "limit #{page.start} , #{page.limit}"));
		// // paginationSuffixElement.addElement(pageEnd);
		//
		// // parentElement.addElement(paginationSuffixElement);

		XmlElement parentElement = document.getRootElement();

		XmlElement insertBatchElement = new XmlElement("insert");
		insertBatchElement.addAttribute(new Attribute("id", "insertBatch"));
		insertBatchElement.addAttribute(new Attribute("parameterType", "map"));

		insertBatchElement.addElement(new TextElement("insert into "
				+ introspectedTable.getFullyQualifiedTable()));
		insertBatchElement.addElement(new TextElement(
				"(<include refid=\"Base_Column_List\" />)"));
		insertBatchElement.addElement(new TextElement("values"));

		XmlElement foreach = new XmlElement("foreach");
		foreach.addAttribute(new Attribute("collection", "list"));
		foreach.addAttribute(new Attribute("item", "item"));
		foreach.addAttribute(new Attribute("index", "index"));
		foreach.addAttribute(new Attribute("separator", ","));

		foreach.addElement(new TextElement("("));

		StringBuffer buffer = new StringBuffer();

		List<IntrospectedColumn> list = introspectedTable.getAllColumns();

		for (int i = 0; i < list.size(); i++) {
			IntrospectedColumn col = list.get(i);
			if (i != 0) {
				buffer.append(",");
			}

			buffer.append("#{item." + col.getJavaProperty() + "}");
		}

		foreach.addElement(new TextElement(buffer.toString()));

		foreach.addElement(new TextElement(")"));

		insertBatchElement.addElement(foreach);

		// XmlElement pageStart = new XmlElement("if");
		// pageStart.addAttribute(new Attribute("test", "page != null"));
		// XmlElement pageDialect1 = new XmlElement("if");
		// pageDialect1.addAttribute(new Attribute("test",
		// "dialect == 'oralce'"));
		// pageStart.addElement(pageDialect1);
		// pageDialect1.addElement(new TextElement(
		// "select * from ( select row_.*, rownum rownum_ from ( "));
		// paginationPrefixElement.addElement(pageStart);
		parentElement.addElement(insertBatchElement);

		// // 产生分页语句后半部分
		// XmlElement paginationSuffixElement = new XmlElement("sql");
		// paginationSuffixElement.addAttribute(new Attribute("id",
		// "OracleDialectSuffix"));
		// XmlElement pageEnd = new XmlElement("if");
		// pageEnd.addAttribute(new Attribute("test", "page != null"));
		// XmlElement pageDialect2 = new XmlElement("if");
		// pageDialect2.addAttribute(new Attribute("test",
		// "dialect == 'oralce'"));
		// pageEnd.addElement(pageDialect2);
		// pageDialect2
		// .addElement(new TextElement(
		// "<![CDATA[ ) row_ ) where rownum_ > #{page.begin} and rownum_ <= #{page.end} ]]>"));
		//
		// // ----- mysql语句。
		// XmlElement mysqlDialect = new XmlElement("if");
		// mysqlDialect.addAttribute(new Attribute("test",
		// "dialect == 'mysql'"));
		// pageEnd.addElement(mysqlDialect);
		// mysqlDialect.addElement(new TextElement(
		// "limit #{page.start} , #{page.limit}"));
		// paginationSuffixElement.addElement(pageEnd);
		//
		// parentElement.addElement(paginationSuffixElement);

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {

		//		XmlElement pageStart = new XmlElement("include"); //$NON-NLS-1$     
		// pageStart.addAttribute(new Attribute("refid",
		// "OracleDialectPrefix"));
		// element.getElements().add(0, pageStart);
		//
		//		XmlElement isNotNullElement = new XmlElement("include"); //$NON-NLS-1$     
		// isNotNullElement.addAttribute(new Attribute("refid",
		// "OracleDialectSuffix"));
		// element.getElements().add(isNotNullElement);

		//		XmlElement pageStart = new XmlElement("if"); //$NON-NLS-1$     
		// pageStart.addAttribute(new Attribute("test",
		// "start != null and pageSize != null"));
		// element.getElements().add(0, pageStart);

		
		
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$     
		isNotNullElement.addAttribute(new Attribute("test",
				"start != null and pageSize != null"));
		isNotNullElement.addElement(new TextElement(
				"limit #{start}, #{pageSize}"));
		element.getElements().add(isNotNullElement);

		return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}

	// /**
	// * @param topLevelClass
	// * @param introspectedTable
	// * @param name
	// */
	// private void addDialect(TopLevelClass topLevelClass,
	// IntrospectedTable introspectedTable, String name) {
	// CommentGenerator commentGenerator = context.getCommentGenerator();
	// Field field = new Field();
	// field.setVisibility(JavaVisibility.PRIVATE);
	// field.setType(new FullyQualifiedJavaType("String"));
	// field.setName(name + " = \"mysql\"");
	// commentGenerator.addFieldComment(field, introspectedTable);
	// topLevelClass.addField(field);
	// char c = name.charAt(0);
	// String camel = Character.toUpperCase(c) + name.substring(1);
	// Method method = new Method();
	// method.setVisibility(JavaVisibility.PUBLIC);
	// method.setName("set" + camel);
	// method.addParameter(new Parameter(new FullyQualifiedJavaType("String"),
	// name));
	// method.addBodyLine("this." + name + "=" + name + ";");
	// commentGenerator.addGeneralMethodComment(method, introspectedTable);
	// topLevelClass.addMethod(method);
	// method = new Method();
	// method.setVisibility(JavaVisibility.PUBLIC);
	// method.setReturnType(new FullyQualifiedJavaType("String"));
	// method.setName("get" + camel);
	// method.addBodyLine("return " + name + ";");
	// commentGenerator.addGeneralMethodComment(method, introspectedTable);
	// topLevelClass.addMethod(method);
	// }

	// /**
	// * @param topLevelClass
	// * @param introspectedTable
	// * @param name
	// */
	// private void addPage(TopLevelClass topLevelClass,
	// IntrospectedTable introspectedTable, String name) {
	// topLevelClass.addImportedType(new FullyQualifiedJavaType(
	// "com.hnisi.e3itm.base.util.Page"));
	// CommentGenerator commentGenerator = context.getCommentGenerator();
	// Field field = new Field();
	// field.setVisibility(JavaVisibility.PROTECTED);
	// field.setType(new FullyQualifiedJavaType(
	// "com.hnisi.e3itm.base.util.Page"));
	// field.setName(name);
	// commentGenerator.addFieldComment(field, introspectedTable);
	// topLevelClass.addField(field);
	// char c = name.charAt(0);
	// String camel = Character.toUpperCase(c) + name.substring(1);
	// Method method = new Method();
	// method.setVisibility(JavaVisibility.PUBLIC);
	// method.setName("set" + camel);
	// method.addParameter(new Parameter(new FullyQualifiedJavaType(
	// "com.hnisi.e3itm.base.util.Page"), name));
	// method.addBodyLine("this." + name + "=" + name + ";");
	// commentGenerator.addGeneralMethodComment(method, introspectedTable);
	// topLevelClass.addMethod(method);
	// method = new Method();
	// method.setVisibility(JavaVisibility.PUBLIC);
	// method.setReturnType(new FullyQualifiedJavaType(
	// "com.hnisi.e3itm.base.util.Page"));
	// method.setName("get" + camel);
	// method.addBodyLine("return " + name + ";");
	// commentGenerator.addGeneralMethodComment(method, introspectedTable);
	// topLevelClass.addMethod(method);
	// }

}
