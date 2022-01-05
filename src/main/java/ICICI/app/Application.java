package ICICI.app;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ICICI.common.CommonAppClass;
import ICICI.core.CoreAppClass;
import ICICI.core.module.CoreModule;
import ICICI.persistence.dao.IUserDao;
import ICICI.persistence.domain.User;
import ICICI.persistence.module.PersistenceModule;
import ICICI.persistence.mysql.module.PersistenceMySqlModule;
import io.gitbub.devlibx.easy.helper.ApplicationContext;
import io.gitbub.devlibx.easy.helper.metrics.IMetrics;
import io.github.devlibx.easy.database.IDatabaseService;
import io.github.devlibx.easy.database.mysql.config.MySqlConfig;
import io.github.devlibx.easy.database.mysql.config.MySqlConfigs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    /**
     * Make a table in your database to test you code:
     * <p>
     * CREATE TABLE `users` (
     * `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
     * `name` varchar(265) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
     */
    // FIXME - PUT YOUR OWN JDBC USRL HERE TO TEST
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/users";

    /***
     * This is just a dummy app to test something. Use RestApplicaiton.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        CoreAppClass.main(args);
        CommonAppClass.main(args);

        // Setup DB - datasource
        MySqlConfig dbConfig = new MySqlConfig();
        dbConfig.setDriverClassName("com.mysql.jdbc.Driver");
        dbConfig.setJdbcUrl(jdbcUrl);
        dbConfig.setUsername("test");
        dbConfig.setPassword("test");
        dbConfig.setShowSql(false);
        MySqlConfigs mySqlConfigs = new MySqlConfigs();
        mySqlConfigs.addConfig(dbConfig);

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IMetrics.class).to(IMetrics.NoOpMetrics.class);
                bind(MySqlConfigs.class).toInstance(mySqlConfigs);
            }
        }, new CoreModule(), new PersistenceModule(), new PersistenceMySqlModule());
        ApplicationContext.setInjector(injector);

        IDatabaseService databaseService = injector.getInstance(IDatabaseService.class);
        databaseService.startDatabase();

        User user = new User();
        user.setName("Some User");
        IUserDao userDao = injector.getInstance(IUserDao.class);
        userDao.persist(user);
        log.info("Saved User with ID={}", user.getId());

        User userFromDB = userDao.findById(user.getId()).orElse(null);
        log.info("User from DB={}", userFromDB);
    }
}
