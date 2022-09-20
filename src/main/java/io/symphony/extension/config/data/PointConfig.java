package io.symphony.extension.config.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.symphony.common.point.data.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Document(collection = "config")
public class PointConfig {

	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String id;

	@DBRef
	@NotNull
	@JsonIgnore
	private Point point;

}
