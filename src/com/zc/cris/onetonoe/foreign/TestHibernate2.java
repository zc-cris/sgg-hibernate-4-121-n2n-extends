package com.zc.cris.onetonoe.foreign;

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
		
		//默认情况下查询有外键一端同样会出现懒加载
//		Dept dept = this.session.get(Dept.class, 1);
//		System.out.println(dept.getName());
//		this.session.close();
//		
		//默认情况下查询没有外键一端会把对应的另外一端查询出来，因为对于hibernate来说，它并不知道
		//这个没有外键的表的对应关联关系，所以会把关联的数据一并查询出来
//		    select
//		        manager0_.ID as ID1_1_0_,
//		        manager0_.NAME as NAME2_1_0_,
//		        dept1_.ID as ID1_0_1_,
//		        dept1_.NAME as NAME2_0_1_,
//		        dept1_.MGR_ID as MGR_ID3_0_1_ 
//				    from
//				        MANAGERS manager0_ 
//				    left outer join
//				        DEPTS dept1_ 
//				            on manager0_.ID=dept1_.ID 
//				    where
//				        manager0_.ID=?
		
		//仔细观察，以上sql语句是有问题的，左外连接的条件应该是：manager.id = dept.manager_id
		//而不应该是manager.id = dept.id
		//所以需要在Manager对应的映射文件中的one-to-one节点设置属性property-ref="manager"
		Manager manager = this.session.get(Manager.class, 1);
		System.out.println(manager.getName());
//		System.out.println(manager.getDept().getName());
		
	}
	
	@Test
	void testSave() {
	
		Manager manager = new Manager();
		manager.setName("aa");
		
		Dept dept = new Dept();
		dept.setName("bb");
		
		manager.setDept(dept);
		dept.setManager(manager);
		
		//建议先保存没有外键的一方，防止发送多余的update语句
		this.session.save(manager);
		this.session.save(dept);
		
		
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
