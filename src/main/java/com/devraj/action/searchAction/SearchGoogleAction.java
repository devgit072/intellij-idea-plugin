package com.devraj.action.searchAction;

import com.intellij.ide.BrowserUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class SearchGoogleAction extends AnAction {

    /*
    This function is called when we click on Action button.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        // We are getting proggraming language, because we have to search like this: JAVA: string, so that google
        // will have better context on keyword.
        String programmingLanguage =   psiFile.getLanguage().getDisplayName();
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        String selectedText = caretModel.getCurrentCaret().getSelectedText();
        String searchQueryUrl = String.format("https://google.com/search?q=%s: %s", programmingLanguage, selectedText);
        BrowserUtil.browse(searchQueryUrl);
    }

    /*
    This function will be called at start only. When intellij-idea starts and plugin kicks in.
    We can enable or disable action based on some condition. For example: When there is no selection, this action
    will be disabled. When user has selected some words, then only this button/action will be active.
    Ideally, you will make action active based on some condition, but sometime your action may be action without any
    condition.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        // ToDO
        // CaretModel is interface which provides information about cursor postion or you can move the cursor.
        CaretModel caretModel = editor.getCaretModel();
        // If there is no word selected, then action will be disabled.
        boolean isAnyWordSelected = caretModel.getCurrentCaret().hasSelection();
        e.getPresentation().setEnabledAndVisible(isAnyWordSelected);
    }
}
