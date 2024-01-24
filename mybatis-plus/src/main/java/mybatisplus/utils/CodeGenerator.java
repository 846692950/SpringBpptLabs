package mybatisplus.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName CodeGenerator
 * @Description mybatisplus 的代码生成器
 * @Author yuepeiwen
 * @Date 2023/11/27 16:17
 * @Version 1.0
 */
public class CodeGenerator {
    // 代码作者信息
    public static final String AUTHOR = "ypw";
    // 要生产的表
    public static final List<String> TABLES = Arrays.asList("user");
    // 路径设置
    public static final String PACKAGE = "mybatis-plus"; // 设置子工程目录名
    public static final String START_PACKAGE = "mybatisplus"; // 设置父包模块名（即启动类所在包的包名）
    public static final String PROJECT_DIR = System.getProperty("user.dir"); // 项目路径
    // 相对路径（无需改动）
    public static final String JAVA_FILE_DIR = PROJECT_DIR + "/" + PACKAGE + "/src/main/java";
    public static final String MAPPER_XML_DIR = PROJECT_DIR + "/" + PACKAGE + "/src/main/resources/mapper";
    // 数据库设置
    public static final String MYSQL_URL = "jdbc:mysql://43.142.106.101:3306/sss?serverTimezone=Asia/Shanghai";
    public static final String MYSQL_UAERNAME = "root";
    public static final String MYSQL_PASSWORD = "123456";

    public static void main(String[] args) {
        // 数据源配置
        FastAutoGenerator.create(MYSQL_URL, MYSQL_UAERNAME, MYSQL_PASSWORD)
                // 全局配置
                .globalConfig(builder -> {
                    builder
                            .author(AUTHOR) // 设置作者
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .dateType(DateType.ONLY_DATE) //定义生成的实体类中日期类型 ONLY_DATE 默认值: TIME_PACK
                            .outputDir(JAVA_FILE_DIR) // 指定输出目录
                            .fileOverride() // 开启覆盖原来生成的文件
                            .disableOpenDir() // 禁止打开输出的目录 默认值:true
                    ;
                })

                // 包配置
                .packageConfig(builder -> {
                    builder
                            .parent(START_PACKAGE)
                            .controller("controller")
                            .entity("entity")
                            .service("service")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, MAPPER_XML_DIR))
                    ;
                })

                // 策略配置
                .strategyConfig(builder -> {
                    builder
                            .addInclude(TABLES) // 设置需要生成的表名，可变长参数“user”, “user1”
                            .addTablePrefix("tb_", "gms_") // 设置过滤表前缀
                            // <service 策略配置>
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // %s适配，根据表名替换
                            .formatServiceImplFileName("%sServiceImpl") // %s适配，根据表名替换
                            // <entity 策略配置>
                            .entityBuilder()
                            .idType(IdType.ASSIGN_ID) // 主键策略，雪花算法自动生成的 id
                            .enableLombok() // 开启 lombok
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Column("deleted", FieldFill.INSERT))
                            .logicDeleteColumnName("deleted") // 说明逻辑删除是哪个字段
                            .enableTableFieldAnnotation() // 属性加上注解说明
                            // <controller 策略配置>
                            .controllerBuilder()
                            .formatFileName("%sController") // %s适配，根据表名替换
                            .enableRestStyle() // 开启 RestController 注解
                            // <mapper 策略配置>
                            .mapperBuilder()
                            .enableMapperAnnotation() // @mapper 注解开启
                            .formatXmlFileName("%sMapper") // %s适配，根据表名替换
                            .enableBaseColumnList()
                            .enableBaseResultMap()
                    ;
                })

                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
