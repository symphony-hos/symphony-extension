package io.symphony.extension.startup;

import io.symphony.extension.point.data.PointRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.symphony.extension.event.PointPublisher;
import lombok.RequiredArgsConstructor;

import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AllPointsPublisher implements StartupAction {

	private final Logger logger = LoggerFactory.getLogger(AllPointsPublisher.class);

	private final PointRepo repo;

	private final PointPublisher publisher;

	public void run() {
		long count = StreamSupport.stream(repo.findAll().spliterator(), false)
			.peek(p -> publisher.publish(p))
			.count();
		logger.debug("Queued {} points for publishing", count);
	}

	@Override
	public Integer getOrder() {
		return 10000;
	}

}
