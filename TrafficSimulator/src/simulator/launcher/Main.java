package simulator.launcher;

import java.io.*;
import java.io.IOException;
import java.util.*;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;


public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static int ticks;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseModeOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("gui or console").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc("An integer representing the number of simulation time. Default value: 10.").build());
	
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && !_mode.equals("gui")) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException {
		if (!line.hasOption("t"))
			ticks = _timeLimitDefaultValue;
		else
			ticks =Integer.parseInt(line.getOptionValue("t"));
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.getOptionValue("m");
		if (_mode == null) {
			throw new ParseException("Mode is missing");
		}
	}

	private static void initFactories() {
		
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		_eventsFactory= new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws IOException {
		TrafficSimulator trafficSimulator = new TrafficSimulator();
		Controller controller = new Controller(trafficSimulator, _eventsFactory);
		
		InputStream inputStream = new FileInputStream(_inFile);
		controller.loadEvents(inputStream);
		
		if (_mode != "gui") {
			OutputStream outputStream = _outFile == null ? System.out: new FileOutputStream(_outFile);
			controller.run(ticks, outputStream);
			
			outputStream.close(); //que eso este dentro puede dar error
		}

		inputStream.close();
		
	}
	
	
	private static void startGUIMode() throws IOException{
		
		TrafficSimulator trafficSimulator = new TrafficSimulator();
		Controller controller = new Controller(trafficSimulator, _eventsFactory);
		
		
		if(_inFile!=null) {
			
			InputStream in= new FileInputStream (new File(_inFile));
			controller.loadEvents(in);
		}
		

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			new MainWindow(controller);
			}
		});
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		
		if(_mode==null||_mode.equals("gui"))
			startGUIMode();
		else if(_mode.equals("console"))
			startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
