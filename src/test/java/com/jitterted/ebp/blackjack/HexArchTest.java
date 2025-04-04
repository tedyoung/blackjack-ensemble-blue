package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.domain.Reconstitute;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaCodeUnitAccess;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;

@Tag("architecture")
public class HexArchTest {
//
//    public static final ArchRule hexagonal_architecture =
//
//            Architectures.layeredArchitecture()
//                         .consideringOnlyDependenciesInLayers()
//                         .layer("Adapter").definedBy("..adapter..")
//                         .layer("Application").definedBy("..application..")
//                         .layer("Domain").definedBy("..domain..")
//                         .layer("Startup").definedBy("com.jitterted.ebp.blackjack")
//
//                         .whereLayer("Adapter").mayOnlyBeAccessedByLayers("Startup")
//                         .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter", "Startup")
//                         .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter", "Startup");
//
//    @Test
//    void checkHexArch() {
//        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jitterted.ebp.blackjack");
//
//        hexagonal_architecture.check(importedClasses);
//    }
//
//    // this is the handwritten version, a simplified version is by ChatGPT below
//    @Test
//    void ensureAggregateReconstituteOnlyCalledByRepository() {
//        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.jitterted.ebp.blackjack");
//
//        DescribedPredicate<? super JavaMethod> haveMethodAnnotatedWithReconstitute =
//                new DescribedPredicate<>("have a method annotated with @Reconstitute") {
//                    @Override
//                    public boolean test(JavaMethod method) {
//                        return method.isAnnotatedWith(Reconstitute.class);
//                    }
//                };
//        ArchCondition<? super JavaMethod> onlyBeAccessedByMethodsInRepositoryClass =
//                new ArchCondition<JavaMethod>("only be accessed by methods in *Repository") {
//                    @Override
//                    public void check(JavaMethod item, ConditionEvents events) {
//                        for (JavaCodeUnitAccess<?> methodCallToReconstitute : item.getAccessesToSelf()) {
//                            if (callerIsNotInTestClass(methodCallToReconstitute)) {
//                                if (!methodCallToReconstitute.getOrigin().getOwner().getFullName().endsWith("Repository")) {
//                                    events.add(SimpleConditionEvent.violated(item, "%s is called by method %s, but it is not in a Repository"
//                                            .formatted(item.getFullName(), methodCallToReconstitute.getOrigin().getFullName())));
//                                }
//                            }
//                        }
//                    }
//
//                    public boolean callerIsNotInTestClass(JavaCodeUnitAccess<?> callersToReconstitute) {
//                        return !callersToReconstitute.getOrigin().getOwner().getFullName().endsWith("Test")
//                                && !callersToReconstitute.getOrigin().getOwner().getEnclosingClass()
//                                                         .map(s -> s.getFullName().endsWith("Test")).orElse(false);
//                    }
//                };
//        ArchRule rule = ArchRuleDefinition.methods()
//                                          .that(haveMethodAnnotatedWithReconstitute)
//                                          .should(onlyBeAccessedByMethodsInRepositoryClass);
//
//        rule.check(importedClasses);
//    }
//
//    @Test
//    void ensureAggregateReconstituteOnlyCalledByRepositoryByChatGPT4() {
//        ArchRule rule = ArchRuleDefinition.methods()
//                                          .that().areAnnotatedWith(Reconstitute.class)
//                                          .should(ArchConditions.onlyBeCalledByClassesThat(nameEndingWith("Repository")))
//                                          .because("Methods annotated with @Reconstitute should only be accessed by Repository classes")
//                                          .allowEmptyShould(true);
//
//        rule.check(new ClassFileImporter()
//                           .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
//                           .importPackages("com.jitterted.ebp.blackjack"));
//    }
//

}