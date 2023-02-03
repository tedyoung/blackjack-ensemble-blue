package com.jitterted.ebp.blackjack;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Tag;

@Tag("architecture")
@AnalyzeClasses(packages = "com.jitterted.ebp.blackjack")
public class HexArchTest {

    public static final ArchRule hexagonal_architecture =

            Architectures.layeredArchitecture()
                         .consideringOnlyDependenciesInLayers()
                         .layer("Adapter").definedBy("..adapter..")
                         .layer("Application").definedBy("..application..")
                         .layer("Domain").definedBy("..domain..")
                         .layer("Startup").definedBy("com.jitterted.ebp.blackjack")

                         .whereLayer("Adapter").mayOnlyBeAccessedByLayers("Startup")
                         .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter", "Startup")
                         .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter", "Startup");
}