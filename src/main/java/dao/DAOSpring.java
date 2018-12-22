package dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DAOSpring {
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("dao/dao-spring.xml");
	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

}
