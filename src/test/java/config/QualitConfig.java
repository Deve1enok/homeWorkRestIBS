package config;

import org.aeonbits.owner.Config;

@Config.Sources({"file:src/test/resources/conf.properties"})

public interface QualitConfig extends Config {

    @Key("baseUri")
    @DefaultValue("http://localhost:8080/api")
    String baseUri();
}
