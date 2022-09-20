package io.symphony.extension.point.rest;

import io.symphony.common.exception.SymphonyExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({SymphonyExceptionHandler.class})
public class EnableSymphonyPointControllerConfig {

}
