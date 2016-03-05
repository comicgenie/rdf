package org.comicwiki;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface JoinRule<L extends TableRow<?>, R extends TableRow<?>> {

	void join(L left, R right );
}
