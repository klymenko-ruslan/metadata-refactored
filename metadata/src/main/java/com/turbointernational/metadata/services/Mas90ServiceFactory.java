package com.turbointernational.metadata.services;

import com.turbointernational.metadata.services.mas90.Mas90;
import com.turbointernational.metadata.services.mas90.MsAccessImpl;
import com.turbointernational.metadata.services.mas90.MsSqlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
@Service
public class Mas90ServiceFactory {

    public enum Implementation {MS_ACCESS, MS_SQL}

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90;

    @Value("${mas90.db.path}")
    private String mas90DbPath;

    /**
     * Get implementation of Mas90 service.
     *
     * For historical reasons there are couple of implementations of the 'MAS90 service'.
     * <ul>
     * <li>Obsolete: based on MS Access</li>
     * <li>Actual: based on MS SQL</li>
     * <ul/>
     *
     * @param implementation implementation of the service
     * @return
     * @throws IOException
     * @see https://github.com/pthiry/TurboInternational/issues/551
     */
    public Mas90 getService(Implementation implementation) throws IOException {
        Mas90 retVal;
        switch (implementation) {
            case MS_ACCESS:
                File f = new File(mas90DbPath);
                retVal = new MsAccessImpl(f);
                break;
            case MS_SQL:
                retVal = new MsSqlImpl(dataSourceMas90);
                break;
            default:
                throw new IllegalArgumentException("Unsupported service implementation: " + implementation);
        }
        return retVal;
    }

}
