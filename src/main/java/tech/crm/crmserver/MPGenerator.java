package tech.crm.crmserver;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


public class MPGenerator {

    public static String PARENT_PACKAGE_NAME = "tech";
    //包名
    public static String PACKAGE_NAME = "crm.crmserver";

    //生成路径 路径到java
    public static String DIR = "D:\\ITproject\\comp30022-it-project-backend\\src\\main\\java";

    public static String AUTHOR = "Lingxiao";

    //修改成自己数据库的username,password和URL
    public static String USERNAME = "root";
    public static String PASSWORD = "415623";
    public static String URL = "jdbc:mysql://localhost:3306/mydb";



    public void testGenerator(){

        //1.全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false) //是否支持AR模式
                .setAuthor(AUTHOR) //作者
                .setOutputDir(DIR) //生成路径
                .setFileOverride(true) //文件覆盖
                .setIdType(IdType.AUTO) //主键策略
                .setServiceName("%sService")  //设置生成得service接口的名字的首字母是否为I
                .setBaseResultMap(false) //
                .setBaseColumnList(true);


        //2.数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL) //设置数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl(URL)
                .setUsername(USERNAME)
                .setPassword(PASSWORD);

        //3.策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true) //全局大写命名
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setNaming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略
                .setTablePrefix("")
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setInclude("department","organization","user","order","contact","event","to_do_list",
                        "permission");

        //4.包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PARENT_PACKAGE_NAME + "." + PACKAGE_NAME)
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller")
                .setEntity("dao")
                .setXml("mapper");


        //配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null)
                .setController("templates/controller.java.vm")
                .setEntity("templates/entity.java.vm")
                .setMapper("templates/mapper.java.vm")
                .setService("templates/service.java.vm")
                .setServiceImpl("templates/serviceImpl.java.vm");

        //5.整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplate(templateConfig);

        //执行
        autoGenerator.execute();
    }

}
