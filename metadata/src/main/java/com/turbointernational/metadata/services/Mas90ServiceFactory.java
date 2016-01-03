package com.turbointernational.metadata.services;

import com.turbointernational.metadata.services.mas90.Mas90;
import com.turbointernational.metadata.services.mas90.MsAccessImpl;
import com.turbointernational.metadata.services.mas90.MsSqlImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
@Service
public class Mas90ServiceFactory {

    public enum Implementation {MS_ACCESS, MS_SQL}

    @Value("${mas90.db.path}")
    private String mas90DbPath;

    @Value("${mas90.db.mssql.url}")
    private String mas90mssqlUrl;

    @Value("${mas90.db.mssql.username}")
    private String mas90mssqlUsername;

    @Value("${mas90.db.mssql.password}")
    private String mas90mssqlPassword;

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
                retVal = new MsSqlImpl(mas90mssqlUrl, mas90mssqlUsername, mas90mssqlPassword);
                break;
            default:
                throw new IllegalArgumentException("Unsupported service implementation: " + implementation);
        }
        return retVal;
    }

}
