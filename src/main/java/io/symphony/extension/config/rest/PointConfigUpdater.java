package io.symphony.extension.config.rest;

import io.symphony.extension.config.data.PointConfig;

public interface PointConfigUpdater {

    void updateNonNull(PointConfig from, PointConfig to);

    void updateAll(PointConfig from, PointConfig to);

}
