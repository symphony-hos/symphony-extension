package io.symphony.extension.startup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertiesPrinter implements StartupAction {
	
	private Logger logger = LoggerFactory.getLogger(PropertiesPrinter.class);

	private final ConfigurableEnvironment env;

	public void run() {
		logger.debug("************************* ACTIVE PROPERTIES **********************************");

		List<MapPropertySource> propertySources = new ArrayList<>();

		env.getPropertySources().forEach(it -> {
			if (it instanceof MapPropertySource) {
				propertySources.add((MapPropertySource) it);
			}
		});

		propertySources.stream().map(propertySource -> propertySource.getSource().keySet()).flatMap(Collection::stream)
				.distinct().sorted().forEach(key -> {
					try {
						logger.debug(key + "=" + env.getProperty(key));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		logger.debug("******************************************************************************");
	}

	@Override
	public Integer getOrder() {
		return 0;
	}

}
