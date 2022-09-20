package io.symphony.extension.event;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.symphony.common.messages.event.PointUpdate;
import io.symphony.common.point.data.Point;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointPublisher {
	
	private Logger logger = LoggerFactory.getLogger(PointPublisher.class);

	private LinkedList<PointUpdate> queue = new LinkedList<>();

	public void publish(Point point) {
		if (point != null)
			queue.add(toEvent(point));
	}

	private PointUpdate toEvent(Point point) {
		return PointUpdate.builder()
			.identifier(point.getId())
			.point(point)
			.build();
	}
	
	@Bean
	public Supplier<PointUpdate> publishPoint() {
		return () -> {
			if (queue.size() > 0)
				return queue.removeFirst();
			return null;
		};
	}

}