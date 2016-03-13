package org.comicwiki;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.comicwiki.guice.ComicWikiModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	private static Options addOptions() {
		Options options = new Options();
		Option resourceDir = Option.builder("r").hasArg().longOpt("resDir")
				.desc("Resource Directory").build();
		Option username = Option.builder("un").hasArg().longOpt("username")
				.desc("Username for DB").build();
		Option password = Option.builder("pw").hasArg().longOpt("password")
				.desc("Password for DB").build();
		Option db = Option.builder("db").hasArg().longOpt("database")
				.desc("Database name").build();
		Option exportDB = Option.builder("x").longOpt("exportDB")
				.desc("Export GCD tables to parquet").build();
		Option etl = Option
				.builder()
				.longOpt("etl")
				.desc("Extract, Transform and Load from dataframes to JSON and Turtle formats")
				.build();
		Option imports = Option.builder("i").longOpt("import").hasArg()
				.argName("resourceType")
				.desc("import characters, organizations or creators").build();

		Option help = Option.builder().longOpt("help").desc("Display help")
				.build();

		return options.addOption(exportDB).addOption(etl).addOption(imports)
				.addOption(username).addOption(password).addOption(db)
				.addOption(resourceDir).addOption(help);
	}

	private static void exit(String message) {
		System.out.println("\r\n" + message + "\r\n");
		printHelp();
		System.exit(-1);
	}

	public static void main(String[] args) {

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;

		try {
			commandLine = parser.parse(addOptions(), args);
		} catch (ParseException ex) {
			ex.printStackTrace();
			exit("Invalid command arguments: " + ex.getMessage());
		}

		if (commandLine.hasOption("h")) {
			printHelp();
			System.exit(0);
		}

		String resourceDir = commandLine.getOptionValue("resourceDir");
		if(resourceDir == null) {
			resourceDir = ".";
		}
		Injector injector = Guice.createInjector(new ComicWikiModule(new File(resourceDir)));
		File rootDir = new File("build");
		File resources = new File("resourceIds.txt");

		if (commandLine.hasOption("import")) {
			String imp = commandLine.getOptionValue("import");
			if ("creators".equals(imp)) {
				File creators = new File(rootDir, "creators");
				validateFile(creators);
			} else if ("organizations".equals(imp)) {
				File organizations = new File(rootDir, "organizations");
				validateFile(organizations);
			} else if ("characters".equals(imp)) {
				File characters = new File(rootDir, "characters");
				validateFile(characters);
			} else {
				exit("Invalid command arguments for import");
			}
		} else if (commandLine.hasOption("etl")) {
			ETL etl = injector.getInstance(ETL.class);
			etl.setInjector(injector);
			try {
				etl.process(resources, rootDir);
			} catch (Exception e) {
				System.out.println("ETL: Critical");
				e.printStackTrace();
			}
		} else if (commandLine.hasOption("exportDB")) {
			if (!commandLine.hasOption("username")
					|| !commandLine.hasOption("password")
					|| !commandLine.hasOption("database")) {
				exit("Must specify --username --password and --database values");
			}
			String username = commandLine.getOptionValue("username");
			String password = commandLine.getOptionValue("password");
			String database = commandLine.getOptionValue("database");

			String jdbcUrl = SparkUtils.createJDBCUrl(username, password,
					database);

			ETL etl = injector.getInstance(ETL.class);
			etl.setInjector(injector);
			try {
				etl.fromRDB(jdbcUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("cw:", addOptions());
	}

	private static void validateFile(File inputFile) {
		if (!inputFile.exists()) {
			exit("Input file does not exist: " + inputFile.getAbsolutePath());
		}
	}

}
