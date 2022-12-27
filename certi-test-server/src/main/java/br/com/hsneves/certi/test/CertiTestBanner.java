package br.com.hsneves.certi.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

public class CertiTestBanner implements Banner {

	private static final String BUILD_INFO_PROPERTIES_FILE = "META-INF/build-info.properties";
	
	private static final String BUILD_VERSION_PROPERTY = "build.version";

	private static final String BUILD_TIME_PROPERTY = "build.time";

	//@formatter:off
    private static final String[] BANNER = {
            "   _____   ______   _____    _______   _____     _______                _   ",
            "  / ____| |  ____| |  __ \\  |__   __| |_   _|   |__   __|              | |  ",
            " | |      | |__    | |__) |    | |      | |        | |      ___   ___  | |_ ",
            " | |      |  __|   |  _  /     | |      | |        | |     / _ \\ / __| | __|",
            " | |____  | |____  | | \\ \\     | |     _| |_       | |    |  __/ \\__ \\ | |_ ",
            "  \\_____| |______| |_|  \\_\\    |_|    |_____|      |_|     \\___| |___/  \\__|",
            " "
    };
    
    private static final String BUILD = "  |  Version: {0}  | Build time: {1}  |";
    //@formatter:on

	@Override
	public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {

		for (String line : BANNER) {
			out.println(line);
		}

		String buildTime = null;
		String buildVersion = null;

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(BUILD_INFO_PROPERTIES_FILE);
		Properties props = new Properties();
		try {
			props.load(is);
			buildTime = props.getProperty(BUILD_TIME_PROPERTY);
			buildVersion = props.getProperty(BUILD_VERSION_PROPERTY);
			out.println(MessageFormat.format(BUILD, buildVersion, buildTime));
			out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}