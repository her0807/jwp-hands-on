package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

	private final Set<Object> beans;

	public DIContainer(final Set<Class<?>> classes)  {
		beans = new HashSet<>();

		for (Class clazz : classes) {
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor();  // private 생성자 가져오기
				constructor.setAccessible(true); // 객체 생성 허용하기
				final Object instance = constructor.newInstance();
				beans.add(instance);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}

		for (Object obj : beans) {
			try {
				initFieldObject(obj);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
	}

	private void initFieldObject(Object obj) throws IllegalAccessException {
		final Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			for (Object targets : beans) {
				if (field.getType().isInstance(targets)) {
					field.set(obj, targets);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(final Class<T> aClass) {
		for (Object obj : beans) {
			if (aClass.isInstance(obj)) {
				return (T)obj;
			}
		}
		return null;
	}
}
