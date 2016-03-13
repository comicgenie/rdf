package org.comicwiki.gcd.fields;

import java.util.Collection;
import java.util.HashSet;

import org.comicwiki.model.CreatorAlias;
import org.comicwiki.model.schema.Person;

public class CreatorField {

	public Collection<Person> creators = new HashSet<>(5);
	
	public Collection<Person> aliases = new HashSet<>(5);
	
	public Collection<CreatorAlias> creatorAliases = new HashSet<>(5);
	
}
