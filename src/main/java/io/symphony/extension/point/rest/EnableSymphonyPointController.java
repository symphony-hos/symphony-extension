package io.symphony.extension.point.rest;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ EnableSymphonyPointControllerConfig.class })
public @interface EnableSymphonyPointController {

}
