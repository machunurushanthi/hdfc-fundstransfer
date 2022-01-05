package ICICI.app.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.Configuration;
import io.github.devlibx.easy.database.mysql.config.MySqlConfigs;
import io.github.devlibx.easy.http.config.Config;
import io.github.devlibx.easy.lock.config.LockConfigs;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationConfig extends Configuration {
    public MySqlConfigs mySqlConfigs;
    public Config easyHttpConfig;
    public LockConfigs locks;
}
