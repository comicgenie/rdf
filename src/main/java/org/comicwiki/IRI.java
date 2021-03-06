package org.comicwiki;

import com.google.common.base.Strings;

public class IRI {

	public String value;

	public static IRI create(String value, IRICache cache) {
		if (Strings.isNullOrEmpty(value)) {
			throw new IllegalArgumentException("value null");
		}
		if (value.startsWith("@")) {
			throw new IllegalArgumentException(
					"Can't create IRIs with public resourceIds: " + value);
		}
		if(value.contains(" ")) {
			throw new IllegalArgumentException(
					"value can't contain spaces " + value);
		}
		IRI iriValue = cache.get(value);
		if(iriValue != null) {
			return iriValue;
		} else {
			IRI iri = new IRI(value);
			cache.add(iri);
			return iri;
		}
	}

	public IRI() {
	}

	public IRI(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IRI other = (IRI) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IRI [value=" + value + "]";
	}
}
