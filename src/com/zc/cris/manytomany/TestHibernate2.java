package com.zc.cris.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TestHibernate2 {
	
	private SessionFactory sessionFactory = null;
	private Session session = null;
	private Transaction transaction = null;
	
	@Test
	void testGet() {
		//同样存在懒加载问题
		Student student = this.session.get(Student.class, 1);
		System.out.println(student.getName());
		System.out.println(student.getCourses().getClass());
//		select
//	        courses0_.STUDENT_ID as STUDENT_2_1_0_,
//	        courses0_.COURSE_ID as COURSE_I1_1_0_,
//	        course1_.ID as ID1_0_1_,
//	        course1_.NAME as NAME2_0_1_ 
//			    from
//			        STUDENT_COURSE courses0_ 
//			    inner join
//			        COURSE course1_ 
//			            on courses0_.COURSE_ID=course1_.ID 
//			    where
//			        courses0_.STUDENT_ID=?
		System.out.println(student.getCourses().size());
		
	}
	
	@Test
	void testSave() {
		Student student1 = new Student();
		Student student2 = new Student();
		student1.setName("a");
		student2.setName("b");
		
		Course course1 = new Course();
		Course course2 = new Course();
		course1.setName("aa");
		course2.setName("bb");
		
		course1.getStudents().add(student1);
		course1.getStudents().add(student2);
		course2.getStudents().add(student1);
		course2.getStudents().add(student2);
		
		student1.getCourses().add(course1);
		student1.getCourses().add(course2);
		student2.getCourses().add(course1);
		student2.getCourses().add(course1);
		
		this.session.save(student1);
		this.session.save(student2);
		this.session.save(course1);
		this.session.save(course2);
		
	
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
