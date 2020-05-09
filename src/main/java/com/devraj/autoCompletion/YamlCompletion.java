package com.devraj.autoCompletion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import org.jetbrains.yaml.YAMLLanguage;

/*
We will implement auto-completion for yaml file.
We will be implementing auto-completion to write BDD tests in yaml file.
Here is format of yaml file:

- TestcaseName: Blabla
  OpCollection: programming_ops
  ProgrammingOpArgs:
    OpName: RunGolangCode
    RunGolangCodeArgs:
      CodeFilePath: ${enter_path_of_golang_code_file}
      ExpectedOutputFilePath: {enter_path_of_expected_output}
      IgnoreFailure: {bool_value}

When you choose: programming_ops, all required fields below the lines should be auto-matically populated.
It also has auto-popup.
 */
/*
Any class which is going to implement auto-completion, need to extends CompletionContributor.
This class is going to be registered in plugin.xml as completion contributor.

 */
public class YamlCompletion extends CompletionContributor {
    public YamlCompletion() {
        GlobalKeyProvider globalKeyProvider = GlobalKeyProvider.getInstance();
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(YAMLLanguage.INSTANCE), globalKeyProvider);
    }
}
