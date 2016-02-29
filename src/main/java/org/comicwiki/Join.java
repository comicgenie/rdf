package org.comicwiki;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Joins.class)
public @interface Join {

	Class<? extends BaseTable> value();
	
	Class<? extends JoinRule> withRule();
	
}
