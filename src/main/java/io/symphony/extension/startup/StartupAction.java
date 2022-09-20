package io.symphony.extension.startup;

public interface StartupAction {
	
	public void run();

	public Integer getOrder();
	
}
