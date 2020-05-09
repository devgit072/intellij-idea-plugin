package com.devraj.autoCompletion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
This class has responsibility to manage all keys and value which will come in suggestion.
This class extends CompletionProvider and will have to implement addCompletions methods which will add the list of
all keys and also to do what will happen when any particular key is selected out of suggestion list.
 */

public class GlobalKeyProvider extends CompletionProvider<CompletionParameters> {

    public static Set<String> keySet = new HashSet<>();
    List<String> existingKeys = Arrays.asList("TestcaseName",
            "TestcaseDescription", "PickCluster",
            "SleepAfterTestcaseInSecs", "SleepBeforeTestcaseInSecs", "SetTestCaseCommunicationId",
            "GetTestCaseCommunicationId");
    private static GlobalKeyProvider globalKeyProvider;
    private GlobalKeyProvider() {
        keySet.addAll(existingKeys);
    }
    public static GlobalKeyProvider getInstance() {
        if (globalKeyProvider == null) {
            globalKeyProvider = new GlobalKeyProvider();
        }
        return globalKeyProvider;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        PsiElement element = parameters.getPosition();
        // If current element is comment, do nothing and return.
        // Editor will automatically determine if it is comment or not. Like in yaml # is comment,
        // in java // or /**/ is comment.
        if (element instanceof PsiComment) {
            return;
        }

        Editor editor = parameters.getEditor();
        Project project = editor.getProject();
        assert project != null;
        // Get the current line content. For example, it the word or element is OpCollection: Show list of words in suggestion.
        final int offset = editor.getCaretModel().getOffset();
        final int lineNumber = editor.getDocument().getLineNumber(offset);
        final int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
        final int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
        final String lineContent = editor.getDocument().getText().substring(lineStartOffset, lineEndOffset);
        final int offsetOfContent = lineContent.length() - StringUtil.trimLeading(lineContent).length();
        final String indentToLine = lineContent.substring(0, offsetOfContent);

        // The logic here is if lineContent is OpCollection, then show the list of keywords
        if (lineContent.contains("OpCollection:")) {
            result.addElement(LookupElementBuilder.create("math_ops").withInsertHandler((insertionContext, lookupElement) -> {
                final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "MathOpArgs:\n"+ additionalIndent + indentToLine + "OpName:");
            }));
            result.addElement(LookupElementBuilder.create("programming_ops").withInsertHandler((insertionContext, lookupElement) -> {
                final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "ProgrammingOpArgs:\n"+ additionalIndent + indentToLine + "OpName:");
            }));

            result.stopHere();
            return;
        }

        // // The logic here is if lineContent is OpName, then show the list of keywords. Also the list of keywords should depend what was
        // the OpCollection. If op-collection is math_ops, then show keywords like add, subtract, multiply etc.
        // If the OpCollection is programming_ops, the show the keyword like run_java_code, run_golang_code etc.
        // Also if user select certain, opName, then insert something below.
        if (lineContent.contains("OpName:")) {
            String value = "";
            int retry = 10;
            for (int i = 0;i< retry ; i++) {
                final int currentLine = lineNumber - i;
                if (currentLine < 0) {
                    break;
                }
                final int currLineEndOffset = editor.getDocument().getLineEndOffset(currentLine);
                final int currLineStartOffset = editor.getDocument().getLineStartOffset(currentLine);
                final String currLineContent = parameters.getEditor().getDocument().getText().substring(currLineStartOffset, currLineEndOffset);
                if (currLineContent.contains("OpCollection:")) {
                    value = currLineContent.substring(currLineContent.indexOf("OpCollection:") + "OpCollection:".length()).trim();
                    break;
                }
                if (currLineContent.trim().equals("")) {
                    break;
                }
            }
            if (value.equalsIgnoreCase("math_ops")){
                result.addElement(LookupElementBuilder.create("AddTwoNumber").withInsertHandler((insertionContext, lookupElement) -> {
                    final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                    final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                    EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "AddTwoNumberArgs:\n"+ additionalIndent + indentToLine + "FirstNumber: ${enter_num}\n" + additionalIndent + indentToLine + "SecondNumber: {enter_num}" +"\n"+ indentToLine + additionalIndent + "ExpectedSum: {enter_num}\n" + indentToLine + additionalIndent + "IgnoreFailure: {bool_value}");
                }));

                result.addElement(LookupElementBuilder.create("SubtractTwoNumber").withInsertHandler((insertionContext, lookupElement) -> {
                    final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                    final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                    EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "SubtractTwoNumberArgs:\n"+ additionalIndent + indentToLine + "FirstNumber: ${enter_num}\n" + additionalIndent + indentToLine + "SecondNumber: {enter_num}" +"\n"+ indentToLine + additionalIndent + "ExpectedSubtractResult: {enter_num}\n" + indentToLine + additionalIndent + "IgnoreFailure: {bool_value}");
                }));

                result.addElement(LookupElementBuilder.create("MultiplyTwoNumber").withInsertHandler((insertionContext, lookupElement) -> {
                    final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                    final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                    EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "MultiplyTwoNumberArgs:\n"+ additionalIndent + indentToLine + "FirstNumber: ${enter_num}\n" + additionalIndent + indentToLine + "SecondNumber: {enter_num}" +"\n"+ indentToLine + additionalIndent + "ExpectedMultiplyResult: {enter_num}\n" + indentToLine + additionalIndent + "IgnoreFailure: {bool_value}");
                }));
            }
            /*
                LookupElementBuilder.create("Demo").withLookupString("Memo");
                withLookupString is just a alias.
                If you type Demo or Memo, Demo word will appear in suggestions.
             */

            if (value.equalsIgnoreCase("programming_ops")){
                result.addElement(LookupElementBuilder.create("RunJavaCode").withInsertHandler((insertionContext, lookupElement) -> {
                    final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                    final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                    EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "RunJavaCodeArgs:\n"+ additionalIndent + indentToLine + "CodeFilePath: ${enter_path_of_java_code_file}\n" + additionalIndent + indentToLine + "ExpectedOutputFilePath: {enter_path_of_expected_output}" +"\n" + indentToLine + additionalIndent + "IgnoreFailure: {bool_value}");
                }));

                result.addElement(LookupElementBuilder.create("RunGolangCode").withInsertHandler((insertionContext, lookupElement) -> {
                    final CodeStyleSettings currentSettings = CodeStyleSettingsManager.getSettings(insertionContext.getProject());
                    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions(insertionContext.getFile().getFileType());
                    final String additionalIndent = indentOptions.USE_TAB_CHARACTER ? "\t" : StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
                    EditorModificationUtil.insertStringAtCaret(editor, "\n"+ indentToLine + "RunGolangCodeArgs:\n"+ additionalIndent + indentToLine + "CodeFilePath: ${enter_path_of_golang_code_file}\n" + additionalIndent + indentToLine + "ExpectedOutputFilePath: {enter_path_of_expected_output}" +"\n" + indentToLine + additionalIndent + "IgnoreFailure: {bool_value}");
                }));
            }

            result.stopHere();
            return;
        }

        result.addElement(LookupElementBuilder.create("OpCollection").withInsertHandler((insertionContext, lookupElement) -> {
            EditorModificationUtil.insertStringAtCaret(editor, ": ");
            AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, null);
            //AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
            result.stopHere();
        }));
        for (String s : keySet) {
            result.addElement(LookupElementBuilder.create(s).withInsertHandler((insertionContext, lookupElement) -> {
                EditorModificationUtil.insertStringAtCaret(editor, ":");
            }));
        }
        result.stopHere();
    }

    public void updateKeys(Set<String> keysToBeUpdated) {
        keySet.addAll(keysToBeUpdated);
    }
}