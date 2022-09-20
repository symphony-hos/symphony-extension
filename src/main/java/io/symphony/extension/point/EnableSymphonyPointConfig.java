package io.symphony.extension.point;

import io.symphony.common.conversion.UnitConversionConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan
@Import({UnitConversionConfig.class})
public class EnableSymphonyPointConfig {
}
