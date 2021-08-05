package me.szumielxd.PlaceholderSK.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class ReflectionUtils {
	
	
	private final String version;
	
	
	public ReflectionUtils() {
		this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	
	public Class<?> getNMSClass(String name) {
		try {
			return Class.forName(String.format("net.minecraft.server.%s.%s", this.version, name));
		} catch (ClassNotFoundException e) {}
		return null;
	}
	
	
	public Class<?> getCraftClass(String name) {
		try {
			return Class.forName(String.format("org.bukkit.craftbukkit.%s.%s", this.version, name));
		} catch (ClassNotFoundException e) {}
		return null;
	}
	
	
	public Object getField(Class<?> clazz, Object obj, String name) {
		try {
			Field f;
			try {
				f = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException | SecurityException e) {
				f = clazz.getField(name);
			}
			f.setAccessible(true);
			return f.get(obj);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {}
		return null;
	}
	public Object getField(Object obj, String name) {
		return this.getField(obj.getClass(), obj, name);
	}
	
	public Object getStaticField(Class<?> clazz, String name) {
		return this.getField(clazz, null, name);
	}
	
	
	public Object invokeMethod(Class<?> clazz, Object obj, String name, Class<?>[] types, Object... args) {
		if(types == null) types = new Class<?>[0];
		try {
			Method meth;
			try {
				meth = clazz.getDeclaredMethod(name, types);
			} catch (NoSuchMethodException | SecurityException e) {
				meth = clazz.getMethod(name, types);
			}
			return meth.invoke(obj, args);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		return null;
	}
	public Object invokeMethod(Class<?> clazz, Object obj, String name, Object... args) {
		Class<?>[] types = new Class<?>[args.length];
		for(int i=0; i<args.length; i++) {
			types[i] = args[i].getClass();
		}
		return this.invokeMethod(clazz, obj, name, types, args);
	}
	public Object invokeMethod(Object obj, String name, Object... args) {
		Class<?>[] types = new Class<?>[args.length];
		for(int i=0; i<args.length; i++) {
			types[i] = args[i].getClass();
		}
		return this.invokeMethod(obj.getClass(), obj, name, types, args);
	}
	public Object invokeMethod(Class<?> clazz, Object obj, String name) {
		return this.invokeMethod(clazz, obj, name, new Class<?>[0], new Object[0]);
	}
	public Object invokeMethod(Object obj, String name) {
		return this.invokeMethod(obj.getClass(), obj, name, new Class<?>[0], new Object[0]);
	}
	
	public Object invokeStaticMethod(Class<?> clazz, String name, Object... args) {
		Class<?>[] types = new Class<?>[args.length];
		for(int i=0; i<args.length; i++) {
			types[i] = args[i].getClass();
		}
		return this.invokeMethod(clazz, null, name, types, args);
	}
	public Object invokeStaticMethod(Class<?> clazz, String name) {
		return this.invokeMethod(clazz, null, name, new Class<?>[0], new Object[0]);
	}
	
	

}