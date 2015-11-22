package net.ilexiconn.showcase.api.model;

import com.google.common.annotations.Beta;

@Beta
public interface IModel {
    String getName();

    String getAuthor();

    int getCubeCount();
}
