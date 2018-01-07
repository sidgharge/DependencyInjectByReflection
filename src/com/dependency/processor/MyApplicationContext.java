package com.dependency.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.dependency.annotation.MyBean;
import com.dependency.annotation.MyConfiguration;


public class MyApplicationContext {
	
	private Map<String, Object> beans;
	
	public <T> MyApplicationContext(Class<T> classType) {
		
		Annotation[] annotations = classType.getAnnotations();
		
		boolean isConfigured = false;
		
		for (Annotation annotation : annotations) {
			if (annotation instanceof MyConfiguration) {
				isConfigured = true;
			}
		}
		
		if (!isConfigured) {
			throw new RuntimeException("Class is not configured with @MyConfiguration annotation");
		}
		
		T object = null;
		try {
			object = classType.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		beans = new HashMap<String, Object>();
		
		Method[] methods = classType.getMethods();
		
		for (Method method : methods) {
			Annotation annotation = method.getAnnotation(MyBean.class);
			if (annotation != null) {
				try {
					beans.put(method.getName(), method.invoke(object));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public <T> Object getBean(String name) {
		return beans.get(name);
	}
}
