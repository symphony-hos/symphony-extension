package io.symphony.extension.startup;

import io.symphony.extension.startup.StartupAction;

public interface PointLoader extends StartupAction {

	public void load();
	
	@Override
	default void run() {
		load();
	}

	@Override
	public default Integer getOrder() {
		return 100;
	}
	
}
