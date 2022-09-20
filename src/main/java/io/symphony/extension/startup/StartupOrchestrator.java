package io.symphony.extension.startup;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartupOrchestrator implements ApplicationListener<ApplicationReadyEvent> {

	private final Logger logger = LoggerFactory.getLogger(StartupOrchestrator.class);

	private final List<StartupAction> actions;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		run();
	}

	private void run() {
		logger.info("Running startup procedure with {} actions", actions.size());
		
		Comparator<StartupAction> byOrder = new Comparator<>() {
			@Override
			public int compare(StartupAction o1, StartupAction o2) {
				return o1.getOrder().compareTo(o2.getOrder());
			}
		};
		
		actions.stream().sorted(byOrder).forEach(a -> {
			logger.info("Running startup action: {}", a.getClass().getCanonicalName());
			a.run();
		});
	}

}
