package web.globalbeershop;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import web.globalbeershop.configuration.SecurityConfiguration;

public  abstract class SpringApplicationInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SecurityConfiguration.class};
    }
}