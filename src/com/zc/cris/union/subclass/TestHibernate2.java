package com.zc.cris.union.subclass;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TestHibernate2 {
	
	private SessionFactory sessionFactory = null;
	private Session session = null;
	private Transaction transaction = null;
	
	
	/*
	 * union-subclass 方式进行继承关系的映射的优点：
	 * 1. 没有辨别列
	 * 2. 子类独有的字段能使用非空约束
	 * 
	 * 缺点：
	 * 1. 多了很多冗余字段
	 * 3. 更新父表数据非常麻烦
	 * 2. 了解即可
	 */
	
	/*
	 * 查询：
	 * 1. 查询父类记录，需要做联合子查询
	 * 2. 查询子类记录，只需要插叙一张表即可
	 */
	@Test
	void testQuery() {
//		List lists = this.session.createQuery(" from Person").list();
//		System.out.println(lists.size());
		
		List list = this.session.createQuery("from Student").list();
		System.out.println(list.size());
	}
	
	
	
	/*
	 * 插入操作：
	 * 1. 为子类和父类各建立一张表，每张表都有自己的所有属性（性能还不错），主键策略暂时只能使用 "increment"
	 */
	@Test
	void testSave() {
		Person person = new Person();
		person.setName("cris");
		person.setAge(12);
		
		Student student = new Student();
		student.setName("张大帅");
		student.setAge(111);
		student.setSchool("重庆南开中学");
		
		this.session.save(person);
		this.session.save(student);
		
	}
	
	
	
	
	/**
	 * 
	 * @MethodName: init
	 * @Description: TODO (执行每次@Test 方法前需要执行的方法)
	 * @Return Type: void
	 * @Author: zc-cris
	 */
	@BeforeEach		
	public void init() {
		
		 //在5.1.0版本汇总，hibernate则采用如下新方式获取：
	    //1. 配置类型安全的准服务注册类，这是当前应用的单例对象，不作修改，所以声明为final
	    //在configure("cfg/hibernate.cfg.xml")方法中，如果不指定资源路径，默认在类路径下寻找名为hibernate.cfg.xml的文件
	    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();
	    //2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成该应用唯一（一般情况下）的一个session工厂
	    this.sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	    this.session = this.sessionFactory.openSession();
	    this.transaction = this.session.beginTransaction();
		
	}
	
	/**
	 * 
	 * @MethodName: destroy
	 * @Description: TODO (执行每次@Test 方法后需要执行的方法注解)
	 * @Return Type: void
	 * @Author: zc-cris
	 */
	@AfterEach		
	public void destroy() {
		
		this.transaction.commit();
		this.session.close();
		this.sessionFactory.close();
	}
	


}
