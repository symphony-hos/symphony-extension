package io.symphony.extension.startup;

import io.symphony.common.point.data.Point;
import io.symphony.extension.config.data.PointConfig;
import io.symphony.extension.startup.StartupAction;

public interface PointInitializer<T extends PointConfig> extends StartupAction {

	public void initialize();

	public void initialize(T config);

	@Override
	default void run() {
		initialize();
	}

	@Override
	public default Integer getOrder() {
		return 200;
	}

}
