package com.blinnproject.myworkdayback.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.blinnproject.myworkdayback", importOptions = ImportOption.DoNotIncludeTests.class)
public class NamingConventionTest {

  @ArchTest
  static ArchRule controllerShouldBeSuffixed = classes()
    .that()
    .resideInAPackage("..controller..")
    .should()
    .haveSimpleNameEndingWith("Controller");

  @ArchTest
  static ArchRule serviceShouldBeSuffixed = classes()
    .that()
    .resideInAPackage("..service..")
    .should()
    .haveSimpleNameEndingWith("Service")
    .orShould()
    .haveSimpleNameEndingWith("ServiceImpl");

  @ArchTest
  static ArchRule repositoryShouldBeSuffixed = classes()
    .that()
    .resideInAPackage("..repository..")
    .should()
    .haveSimpleNameEndingWith("Repository");

  @ArchTest
  static ArchRule exceptionShouldBeSuffixed = classes()
    .that()
    .resideInAPackage("..exception..")
    .should()
    .haveSimpleNameEndingWith("Exception")
    .orShould()
    .haveSimpleNameEndingWith("Handler");
}
