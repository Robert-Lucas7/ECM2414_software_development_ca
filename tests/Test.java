package test;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class Test { //From: https://www.baeldung.com/junit-tests-run-programmatically-from-java
    public static void main(String[] args){
        RunTest runner = new RunTest();
        runner.runAll();

        TestExecutionSummary summary = runner.listener.getSummary();
        summary.printTo(new PrintWriter(System.out));

        List<TestExecutionSummary.Failure> failures = summary.getFailures();
        if(!failures.isEmpty()) {
            System.out.println("Failed tests:");
            for (TestExecutionSummary.Failure f : failures) {
                System.out.println(f.getTestIdentifier().getDisplayName());
            }
        }

    }
    //nested inner class or standalone file??
     static class RunTest {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        public void runAll(){
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectPackage("test"))
                    .filters(includeClassNamePatterns(".*Test"))
                    .build();
            Launcher launcher = LauncherFactory.create();
            TestPlan testplan = launcher.discover(request);
            launcher.registerTestExecutionListeners(listener);
            launcher.execute(request);
        }
    }
}
