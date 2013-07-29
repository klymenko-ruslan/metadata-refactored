package ti.metadata.domain;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 *
 * @author jrodriguez
 */
/**
 * A central place to register application Converters and Formatters.
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {
    
    @Override
    protected void installFormatters(FormatterRegistry registry) {
        super.installFormatters(registry);
    }

}