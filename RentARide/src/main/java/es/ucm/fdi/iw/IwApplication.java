package es.ucm.fdi.iw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.model.Configuration;

@SpringBootApplication
public class IwApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(IwApplication.class, args);

		LoadConfiguration configuration = new LoadConfiguration(app);
		configuration.loadConfiguration();
	}

}

@Service
class LoadConfiguration implements ApplicationContextAware {

	private static ApplicationContext context;
	private ConfigurableApplicationContext app;

	public LoadConfiguration(ConfigurableApplicationContext app) {
		this.app = app;
	}

	public void loadConfiguration() {
		EntityManager em = context.getBean(EntityManager.class);
		List<Configuration> configData = em.createNamedQuery("Configuration.findAll", Configuration.class)
				.getResultList();

		Map<String, Object> propertySource = new HashMap<>();
		for (Configuration data : configData)
			propertySource.put(data.getConfigKey(), data.getConfigValue());

		// ConfigurableEnvironment configEnv = ((ConfigurableEnvironment)
		// applicationContext.getEnvironment());
		// PropertySource<?> appConfigProp =
		// configEnv.getPropertySources().get("applicationConfig:
		// [classpath:/application.properties]");
		app.getEnvironment().getPropertySources().addLast(new MapPropertySource("APP_CONFIG", propertySource));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

}