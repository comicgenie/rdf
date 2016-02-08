package org.comicwiki;

public final class ComicKey {

	public final String id;

	public final String internalId;

	protected ComicKey(String id, String internalId) {
		this.id = id;
		this.internalId = internalId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((internalId == null) ? 0 : internalId.hashCode());
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
		ComicKey other = (ComicKey) obj;
		if (internalId == null) {
			if (other.internalId != null)
				return false;
		} else if (!internalId.equals(other.internalId))
			return false;
		return true;
	}

}
