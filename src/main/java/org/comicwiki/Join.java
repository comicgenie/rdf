package org.comicwiki;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.comicwiki.joinrules.JoinRule;
import org.comicwiki.joinrules.NoOpJoinRule;
import org.comicwiki.model.schema.Thing;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Joins.class)
public @interface Join {

	Class<? extends BaseTable<? extends TableRow<? extends Thing>>> value();

	Class<? extends JoinRule> withRule() default NoOpJoinRule.class;

	String leftKey() default "";

	String rightKey() default "id";

	String leftField() default "";

	String rightField() default "instance";

}
