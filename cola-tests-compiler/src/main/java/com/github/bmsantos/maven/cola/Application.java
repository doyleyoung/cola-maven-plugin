package com.github.bmsantos.maven.cola;

import static java.lang.System.out;

import java.net.URLClassLoader;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.github.bmsantos.maven.cola.exceptions.ColaExecutionException;
import com.github.bmsantos.maven.cola.main.ColaMain;
import com.github.bmsantos.maven.cola.provider.CommandLineColaProvider;

public class Application {

    private static final String ERR_MSG_FAILED_COMPILATION = "Failed to compile COLA Tests: ";

    @Parameter(names = { "-h", "--help" }, description = "Print this guide")
    private boolean help;

    @Parameter(names = { "-t", "--target" }, required = true, help = true,
        description = "Base directory containing compiled java packages and classes (required)")
    private String targetDirectory;

    @Parameter(names = { "-b", "--ideBaseClass" }, description = "IDE base test class if required")
    private String ideBaseClass;

    @Parameter(names = { "-m", "--ideBaseClassTest" }, description = "IDE base test class method to be removed")
    private String ideBaseClassTest;

    public static void main(final String[] args) {

        final Application app = new Application();
        final JCommander jc = new JCommander(app);
        jc.setProgramName("java -jar /path/to/cola-tests.jar");

        ColaMain main = null;
        try {
            jc.parse(args);

            final CommandLineColaProvider provider = new CommandLineColaProvider(app.targetDirectory);

            try (final URLClassLoader loader = provider.getTargetClassLoader()) {
                main = new ColaMain(app.ideBaseClass, app.ideBaseClassTest);
                main.execute(provider);
            }

            return;
        } catch (final ColaExecutionException e) {
            if (main != null) {
                for (final String f : main.getFailures()) {
                    out.println(f);
                }
            }
        } catch (final ParameterException e) {
            if (!app.help) {
                out.println(ERR_MSG_FAILED_COMPILATION + e.getMessage());
            }
        } catch (final Throwable t) {
            if (!app.help) {
                out.println(ERR_MSG_FAILED_COMPILATION);
                t.printStackTrace();
            }
        }
        jc.usage();
    }
}
