package io.symphony.extension.properties;

import java.awt.*;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@Validated
public class ExtensionProperties<T extends PointProperties> {

	@NotNull
	private String name;
	
	private Set<T> points;
	
}
