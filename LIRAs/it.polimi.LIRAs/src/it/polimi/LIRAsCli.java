package it.polimi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.generator.GeneratorContext;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

public class LIRAsCli {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println(
                "Usage: java ... it.polimi.LIRAsCli <input.liras> <output-directory>"
            );
            System.exit(1);
        }

        Path inputPath = Paths.get(args[0]).toAbsolutePath().normalize();
        Path outputPath = Paths.get(args[1]).toAbsolutePath().normalize();

        System.out.println("Input:  " + inputPath);
        System.out.println("Output: " + outputPath);

        // Initialize Xtext and the LIrAs language
        Injector injector =
            new LIRAsStandaloneSetup().createInjectorAndDoEMFRegistration();

        // Load the LIrAs file
        ResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

        Resource resource = resourceSet.getResource(
            URI.createFileURI(inputPath.toString()),
            true
        );

        // Validate the input model
        IResourceValidator validator =
            injector.getInstance(IResourceValidator.class);

        List<Issue> issues = validator.validate(
            resource,
            CheckMode.ALL,
            CancelIndicator.NullImpl
        );

        boolean hasErrors = false;

        for (Issue issue : issues) {
            System.out.println(
                issue.getSeverity() + ": " +
                issue.getMessage() +
                " [line " + issue.getLineNumber() + "]"
            );

            if (issue.getSeverity().toString().equals("ERROR")) {
                hasErrors = true;
            }
        }

        if (hasErrors) {
            System.err.println("LIrAs specification contains errors. Generation aborted.");
            System.exit(2);
        }

        // Configure where generated files have to be written
        JavaIoFileSystemAccess fsa =
            injector.getInstance(JavaIoFileSystemAccess.class);

        fsa.setOutputPath(outputPath.toString());

        // Obtain the same generator used by Xtext/Eclipse
        IGenerator2 generator =
            injector.getInstance(IGenerator2.class);

        GeneratorContext context = new GeneratorContext();

        generator.doGenerate(resource, fsa, context);

        System.out.println("Done.");
        //System.out.println("PID: " + ProcessHandle.current().pid());
        //Thread.sleep(30000);
    }
}