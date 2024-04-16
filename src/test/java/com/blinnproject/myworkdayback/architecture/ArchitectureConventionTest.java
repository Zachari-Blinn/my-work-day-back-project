package com.blinnproject.myworkdayback.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.blinnproject.myworkdayback", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureConventionTest {

  @ArchTest
  static ArchRule entities_fields_should_be_private = fields()
      .that().areDeclaredInClassesThat().resideInAPackage("..entity..")
      .should().bePrivate();

  @ArchTest
  static ArchRule services_must_be_interfaces = classes()
    .that().resideInAPackage("..service..")
    .and().haveSimpleNameEndingWith("Service")
    .should().beInterfaces();

  @ArchTest
  static ArchRule layered_architecture_should_be_respected = layeredArchitecture().consideringAllDependencies()
    .layer("Controller").definedBy("com.blinnproject.myworkdayback.controller..")
    .layer("Service").definedBy("com.blinnproject.myworkdayback.service..")
    .layer("Repository").definedBy("com.blinnproject.myworkdayback.repository..")

    .whereLayer("Controller").mayNotBeAccessedByAnyLayer();
}
