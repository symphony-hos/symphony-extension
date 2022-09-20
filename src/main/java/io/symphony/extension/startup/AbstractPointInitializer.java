package io.symphony.extension.startup;

import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.config.data.PointConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.StreamSupport;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractPointInitializer<T extends PointConfig> implements PointInitializer<T> {

	private final PointConfigRepo<T> configRepo;

	@Override
	public void initialize() {
		StreamSupport.stream(configRepo.findAll().spliterator(), false)
			.forEach(p -> initialize(p));
	}

}
