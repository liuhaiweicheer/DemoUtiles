package com.springdata.test;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lhw
 * @date 2021/4/21
 * CREATE TABLE `article` (
 * 	`aid` INT(11) NOT NULL auto_increment COMMENT '主键',
 * 	`author` CHAR(255) DEFAULT NULL COMMENT '作者',
 * 	`createTime` DATETIME DEFAULT null COMMENT '创建时间',
 * 	`title` VARCHAR(255) DEFAULT NULL COMMENT '标题',
 * 	PRIMARY KEY (`aid`)
 * );
 *
 */
//  使用注解建立实体类和数据表之间的关系
@Data
@Table(name = "article")   //  建立了实体类和数据表之间的关系
@Entity   // 告诉数据库这是一个实体类  会把他和数据库中的表做映射
public class Article {

    @Id   // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //  标识主键的生成策略  IDENTITY 自增策略
    @Column(name = "aid")
    private Integer aid;

    @Column(name = "author")
    private String author;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "title")
    private String title;

}
