package org.comicwiki;

import org.comicwiki.model.schema.Person;

public class CreatorRepository extends Repository<Person>{
	
	public Person matchFamilyName(String familyName) {
		for(Person person : cache.values()) {
			if(familyName.toLowerCase().equals(person.familyName)) {
				return person;	
			}
		}
		return null;
	}
	
	public Person matchGivenName(String givenName) {
		for(Person person : cache.values()) {
			if(givenName.toLowerCase().equals(person.givenName)) {
				return person;	
			}
		}
		return null;
	}
}
