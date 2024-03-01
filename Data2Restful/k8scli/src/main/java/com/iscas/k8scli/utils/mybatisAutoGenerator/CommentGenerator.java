package com.iscas.k8scli.utils.mybatisAutoGenerator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * @ClassName: CommentGenerator
 * @Description: 用于根据数据库自动生成pojo、mapper
 * @Author: wzc
 * @Date: 2023/4/22 10:02
 */
public class CommentGenerator extends DefaultCommentGenerator {
    private boolean addRemarkComments = false;
    private boolean markAsDoNotDelete = false;

    /**
     * 设置用户配置的参数
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        System.out.println("--------------------------");
        System.out.println(StringUtility.isTrue(properties.getProperty("addRemarkComments")));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        this.markAsDoNotDelete = StringUtility.isTrue(properties.getProperty("markAsDoNotDelete"));
    }

    /**
     * 给字段添加注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        //根据参数和备注信息判断是否添加备注信息
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            addFieldJavaDoc(field, remarks);
        }
    }

    /**
     * @param innerClass
     *            the inner class
     * @param introspectedTable
     *            the introspected table
     * @param newMarkAsDoNotDelete
     *            the mark as do not delete
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable,
                                boolean newMarkAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append(" ");
        sb.append(getDateString());
        innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
        innerClass.addJavaDocLine(" */");
        super.addClassComment(innerClass, introspectedTable);
    }

    /**
     * @param innerClass
     *            the inner class
     * @param introspectedTable
     *            the introspected table
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        this.addClassComment(innerClass, introspectedTable, false);
    }

    /**
     * 给model的字段添加注释
     */
    private void addFieldJavaDoc(Field field, String remarks) {
        //文档注释开始
//        field.addJavaDocLine("/**");
        //获取数据库字段的备注信息
        String[] remarkLines = remarks.split(System.getProperty("line.separator"));
        for (String remarkLine : remarkLines) {
//            field.addJavaDocLine(" * " + remarkLine);
            field.addJavaDocLine("/** " + remarkLine + " */");
        }
//        addJavadocTag(field, false);
//        field.addJavaDocLine(" */");
    }
}
