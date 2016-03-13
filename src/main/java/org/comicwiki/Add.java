package org.comicwiki;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

public class Add {

	public static <T> T[] one(T[] array, T object) {
		if (array == null && object != null) {			
			T[] o = (T[]) Array.newInstance(object.getClass(), 1);
			o[0] = object;
			return o;
		} else if(array!= null && object == null) {
			return array;
		}
		return ObjectArrays.concat(array, object);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] both(T[] array, T[] object, Class<T> type) {
		if (array == null && object != null) {
			return object;
		} else if (array != null && object == null) {
			return array;
		} else if (array == null && object == null) {
			return null;
		}
		
		return ObjectArrays.concat(array, object, type);
	}

	public static <T> T[] toArray(Collection<T> object, Class<T> type) {
		if(object == null) {
			return null;
		}
		T[] o = (T[]) Array.newInstance(type, object.size());
		return object.toArray(o);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] both(T[] array, Collection<T> object, Class<T> type) {

		if (array == null && object != null) {
			T[] o = (T[]) Array.newInstance(type, object.size());
			return object.toArray(o);
		} else if (array != null && object == null) {
			return array;
		} else if (array == null && object == null) {
			return null;
		}
		return ObjectArrays.concat(array, object.toArray(array),
				(Class<T>) object.getClass());
	}
}
