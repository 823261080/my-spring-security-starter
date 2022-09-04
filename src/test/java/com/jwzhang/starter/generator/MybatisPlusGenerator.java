package com.jwzhang.starter.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;

/**
 * MybatisPlusGenerator
 *
 * @author zjw
 * @since 2022/8/16
 */
public class MybatisPlusGenerator {

    private static String url = "jdbc:mysql://192.168.31.104:3306/security-starter?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static String username = "security-starter";
    private static String password = "security-starter";
    private static String outputPath = "C:\\Users\\Administrator\\Desktop\\generator";
    private static String[] tableNames = {
            "sys_role_menu",
            "sys_role_user",
            "sys_role",
            "sys_menu",
            "sys_user1",
            "sys_use2",
    };

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("zjw") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.jwzhang") // 设置父包名
                            .moduleName("starter") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputPath + "/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames) // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            //实体类配置
                            .entityBuilder()
                            .enableLombok()
//                            .enableChainModel()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord()
                            //控制层配置
                            .controllerBuilder()
                            .enableRestStyle()
                            //开启@Mapper 注解
                            .mapperBuilder()
                            .mapperAnnotation(Mapper.class)
                    ;
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }
}
