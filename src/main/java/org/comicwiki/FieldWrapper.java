package org.comicwiki;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldWrapper {

	private Field field;

	public FieldWrapper(Field field) {
		this.field = field;
	}
	
	public Object get(Object object) throws IllegalArgumentException,
			IllegalAccessException {
		return field.get(object);
	}

	public <T extends Annotation> Annotation getAnnotation(Class<T> clazz) {
		return field.getAnnotation(clazz);
	}

	public Annotation[] getAnnotations() {
		return field.getAnnotations();
	}

	public String getName() {
		return field.getName();
	}
}
