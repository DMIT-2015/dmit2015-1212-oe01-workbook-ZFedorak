package common.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.enterprise.context.ApplicationScoped;

@DataSourceDefinitions({

	@DataSourceDefinition(
		name="java:app/datasources/mssqlDS",
		className="com.microsoft.sqlserver.jdbc.SQLServerDataSource",
		url="jdbc:sqlserver://DMIT-Capstone1.ad.sast.ca;databaseName=DMIT2015_1212_E01_zfedorak1;TrustServerCertificate=true",   // change A01 to E01 if you are in section E01, change yourNaitUsername
		user="zfedorak1",  //  change yourNaitUsername
		password="RemotePassword200410635"),    // Replace 200012345 with your NAIT StudentID

})

@ApplicationScoped
public class ApplicationConfig {

}