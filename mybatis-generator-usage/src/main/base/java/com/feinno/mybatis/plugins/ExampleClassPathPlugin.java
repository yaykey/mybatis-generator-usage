/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.feinno.mybatis.plugins;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
/**
 * 
 * ClassName:ExampleClassPathPlugin
 * Reason:	 TODO ADD REASON
 *
 * @author   yzy
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2013	2013-1-9		下午04:15:19
 * @see
 */
public class ExampleClassPathPlugin extends BasePlugin {
    
    
    private boolean enableExamplePath;
    
    private String extendExample;

    /**
     * 
     */
    public ExampleClassPathPlugin() {
    	
    }

    public boolean validate(List<String> warnings) {
    	
        return true;
    }

   
	@Override
    public void initialized(IntrospectedTable introspectedTable) {
        
		extendExample = (properties.getProperty("extendExample"));
    	
    	enableExamplePath = StringUtility.isTrue(properties.getProperty("enableExamplePath"));
		
    	if (enableExamplePath) {
    		//增加扩展包
            FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
            String pakkage = calculateJavaModelPackage(fullyQualifiedTable);
            
            StringBuilder sb = new StringBuilder();
            sb.append(pakkage);
            sb.append('.');
            
            //增加包
            sb.append("example.");
            
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Example");
            
            introspectedTable.setExampleType(sb.toString());
            
            
            
            
            
    	}
    }
	
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		
		if (StringUtility.stringHasValue(extendExample)) {
			FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(extendExample);
			
			topLevelClass.setSuperClass(fullyQualifiedJavaType);
			
			topLevelClass.addImportedType(fullyQualifiedJavaType);
		}
		
		return true;
	}
	
    
}
