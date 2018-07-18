/*
 * File Name：MyBatisGeneratorTool.java
 * Package Name：test
 *
 * Function： TODO 
 *
 *   ver     date      author               department
 * ──────────────────────────────────————————————————————————————
 *   V1.0   2012-12-24    yangzhenyu		  DATA BUSINESS DEPARTMENT
 *
 * Copyright (c) 2012, Feinno Communication Tech All Rights Reserved.
 */
package test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * ClassName:MyBatisGeneratorTool Reason: 
 * 
 * @author yzy
 * @version V1.0.0
 * @since 1.0
 * @Date 2012-12-24 下午05:07:04
 * 
 */
public class MyBatisGeneratorTool {
	public static void main(String[] args) {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		
//		String genCfg = "/generatorConfig1.xml";
//		String genCfg = "/generatorConfig2.xml";
//		String genCfg = "/generatorConfig_cmpp.xml";
//		String genCfg = "/generatorConfig_message.xml";
//		String genCfg = "/generatorConfig_deliver.xml";
//		String genCfg = "/generatorConfig_at.xml";
//		String genCfg = "/generatorConfig_kpi.xml";
//		String genCfg = "/generatorConfig_op.xml";
//		String genCfg = "/generatorConfig_op_dim.xml";
//		String genCfg = "/generatorConfig_op_p.xml";
//		String genCfg = "/generatorConfig_op_rpt.xml";
//		String genCfg = "/generatorConfig_op_ui.xml";
		
		String genCfg = "/generatorConfig_st.xml";
		
		
		String filePath = MyBatisGeneratorTool.class.getResource(
				genCfg).getFile();
		File configFile = new File(filePath);
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = null;
		try {
			config = cp.parseConfiguration(configFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (XMLParserException e) {
			e.printStackTrace();
		}
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = null;
		try {
			myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		}
		catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		try {
			myBatisGenerator.generate(null);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

