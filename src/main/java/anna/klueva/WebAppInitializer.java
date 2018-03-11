package anna.klueva;

import anna.klueva.config.RootConfig;
import anna.klueva.config.WebConfig;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    /**
     * Used to set the @Profile beans via root context
     * */
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        if(System.getProperty("spring.profiles.active") == null){
            ((ConfigurableEnvironment)context.getEnvironment()).setDefaultProfiles("dev");
        }
        else{
            ((ConfigurableEnvironment)context.getEnvironment()).setDefaultProfiles(
                    System.getProperty("spring.profiles.active"));
        }
        return context;
    }
}
