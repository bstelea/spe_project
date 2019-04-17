package web.globalbeershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import web.globalbeershop.service.BraintreeGatewayService;
import com.braintreegateway.BraintreeGateway;

import java.io.File;

@PropertySources({
		@PropertySource(value = "classpath:secret.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:/home/ubuntu/.secret.properties", ignoreResourceNotFound = true)
})
@SpringBootApplication
public class GlobalbeershopApplication {

	public static String DEFAULT_CONFIG_FILENAME = "../config.properties";
	public static String DEFAULT_CONFIG_FILENAME_FOR_DEV = "config.properties";
	public static BraintreeGateway gateway;

	public static void main(String[] args) {
		File configFile = new File(DEFAULT_CONFIG_FILENAME);
		File devFile = new File(DEFAULT_CONFIG_FILENAME_FOR_DEV);
		try {
			if(configFile.exists() && !configFile.isDirectory()) {
				gateway = BraintreeGatewayService.fromConfigFile(configFile);
			} else if (devFile.exists() && !devFile.isDirectory()) {
				gateway = BraintreeGatewayService.fromConfigFile(devFile);
			} else {
				gateway = BraintreeGatewayService.fromConfigMapping(System.getenv());
			}
		} catch (NullPointerException e) {
			System.err.println("Could not load Braintree configuration from config file or system environment.");
			System.exit(1);
		}
		SpringApplication.run(GlobalbeershopApplication.class, args);
	}

}

