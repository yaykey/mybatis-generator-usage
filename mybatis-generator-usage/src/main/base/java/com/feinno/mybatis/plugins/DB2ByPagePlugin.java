package com.feinno.mybatis.plugins;
/**  
 * @author 陈云  
 * @version 创建时间：2011-12-29 下午02:54:23  
 * 类说明  
 */


import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class DB2ByPagePlugin extends BasePlugin {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	private void addField(String fieldName, FullyQualifiedJavaType fieldType,
			TopLevelClass topLevelClass) {
		Field tmpField = new Field(fieldName, fieldType);
		tmpField.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(tmpField);

		Method setMethod = new Method();
		setMethod.setName("set" + fieldName.toUpperCase().substring(0, 1)
				+ fieldName.substring(1));
		Parameter param = new Parameter(fieldType, fieldName);
		setMethod.addParameter(param);
		setMethod.setVisibility(JavaVisibility.PUBLIC);
		setMethod.addBodyLine("this." + fieldName + "=" + fieldName + ";");

		topLevelClass.addMethod(setMethod);

		Method getMethod = new Method();
		getMethod.setName("get" + fieldName.toUpperCase().substring(0, 1)
				+ fieldName.substring(1));

		getMethod.setReturnType(fieldType);
		getMethod.setVisibility(JavaVisibility.PUBLIC);
		getMethod.addBodyLine("return this." + fieldName + ";");

		topLevelClass.addMethod(getMethod);

	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
//		FullyQualifiedJavaType intType = FullyQualifiedJavaType
//				.getIntInstance();
//		addField("pageIndex", intType, topLevelClass);
//		addField("pageSize", intType, topLevelClass);
//
//		Method getSkipCountMethod = new Method();
//		getSkipCountMethod.setName("getSkipRecordCount");
//
//		getSkipCountMethod.setReturnType(intType);
//		getSkipCountMethod.setVisibility(JavaVisibility.PUBLIC);
//		getSkipCountMethod
//				.addBodyLine("return (this.pageIndex-1)*this.pageSize;");
//
//		topLevelClass.addMethod(getSkipCountMethod);
//		
//		Method getEndRecordIndexMethod = new Method();
//		getEndRecordIndexMethod.setName("getEndRecordCount");
//
//		getEndRecordIndexMethod.setReturnType(intType);
//		getEndRecordIndexMethod.setVisibility(JavaVisibility.PUBLIC);
//		getEndRecordIndexMethod
//				.addBodyLine("return this.pageIndex*this.pageSize;");
//
//		topLevelClass.addMethod(getEndRecordIndexMethod);
//		
//
//		Method newConstructorMethod = new Method();
//		newConstructorMethod.setConstructor(true);
//		newConstructorMethod.addParameter(new Parameter(intType, "pageSize"));
//		newConstructorMethod.addParameter(new Parameter(intType, "pageIndex"));
//		newConstructorMethod.addBodyLine("this();");
//		newConstructorMethod.addBodyLine("this.pageSize=pageSize;");
//		newConstructorMethod.addBodyLine("this.pageIndex=pageIndex;");
//		newConstructorMethod.setVisibility(JavaVisibility.PUBLIC);
//		newConstructorMethod.setName(topLevelClass.getType().getShortName());
//
//		topLevelClass.addMethod(newConstructorMethod);
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				introspectedTable.getExampleType());
		importedTypes.add(type);
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);

		FullyQualifiedJavaType returnType = FullyQualifiedJavaType
				.getNewListInstance();
		FullyQualifiedJavaType listType;
		
		if (introspectedTable.getRules().generateBaseRecordClass()) {
			listType = new FullyQualifiedJavaType(introspectedTable
					.getBaseRecordType());
		} else if (introspectedTable.getRules().generatePrimaryKeyClass()) {
			listType = new FullyQualifiedJavaType(introspectedTable
					.getPrimaryKeyType());
		} else {
			throw new RuntimeException(getString("RuntimeError.12")); //$NON-NLS-1$
		}

		importedTypes.add(listType);
		returnType.addTypeArgument(listType);
		method.setReturnType(returnType);

		method.setName("selectByExampleAndPage");
		method.addParameter(new Parameter(type, "example")); //$NON-NLS-1$

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(method);

		return true;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see org.mybatis.generator.api.PluginAdapter#sqlMapDocumentGenerated(org.mybatis.generator.api.dom.xml.Document, org.mybatis.generator.api.IntrospectedTable)
	 */
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {

		XmlElement parentElement = document.getRootElement();
				
		StringBuilder sb = new StringBuilder();
		
		String fqjt = introspectedTable.getExampleType();
		XmlElement answer = new XmlElement("select");

		answer.addAttribute(new Attribute("id",
				"selectByExampleAndPage"));
		answer.addAttribute(new Attribute(
				"resultMap","BaseResultMap"));
		answer.addAttribute(new Attribute("parameterType", fqjt));
		
		
		sb.append("select");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("distinct");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("<include refid=\"Base_Column_List\" />");
		sb.append("\r\n");
		
		sb.append("\t");
		sb.append("from (");
		sb.append("\r\n");
				
		sb.append("\t\t");
		sb.append("select ROW_NUMBER() OVER(");
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("order by");
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("<if test=\"orderByClause != null\">");
		sb.append("\r\n");
		
		sb.append("\t\t\t\t");
		sb.append("${orderByClause}");
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("</if>");
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("<if test=\"orderByClause == null\">");
		sb.append("\r\n");
		
		//主键
		sb.append("\t\t\t\t");
		List<IntrospectedColumn> pks=introspectedTable.getPrimaryKeyColumns();
		if (pks.size()==1){
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(pks.get(0)));
		}else{
			sb.append(" #your_primary_key_name ");
		}
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("</if>");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append(") AS ROWNUM,");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("<include refid=\"Base_Column_List\" />");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("<if test=\"_parameter != null\">");
		sb.append("\r\n");
		
		sb.append("\t\t\t");
		sb.append("<include refid=\"Example_Where_Clause\" />");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("</if>");
		sb.append("\r\n");
		
		sb.append("\t");
		sb.append(") row");
		sb.append("\r\n");
		
		sb.append("\t");
		sb.append("where");
		sb.append("\r\n");
		
		sb.append("\t\t");
		sb.append("ROWNUM BETWEEN ${start} AND ${end}");
		
		answer.addElement(new TextElement(sb.toString()));

		parentElement.addElement(answer);

		return true;
	}

}
