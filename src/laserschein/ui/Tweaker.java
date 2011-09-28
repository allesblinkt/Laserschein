package laserschein.ui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tweaker {
	String name();
	String description();
	int min();
	int max();
}
