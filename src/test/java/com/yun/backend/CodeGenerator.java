package com.yun.backend;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static final String URL = "jdbc:mysql:///ocr_data_system";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "947499";
    public static final String AUTHOR = "LiYueyun";
    public static final String DIR_LOCATION = "C:\\Users\\96215\\Desktop\\backend\\src\\main\\java";
    public static final String MODULE_NAME = "backend";
    public static final String MAPPER_LOCATION = "C:\\Users\\96215\\Desktop\\backend\\src\\main\\resources\\mapper";
    public static final String TABLES = "statistics";
    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(DIR_LOCATION); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.yun") // 设置父包名
                            .moduleName(MODULE_NAME) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, MAPPER_LOCATION)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLES); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
