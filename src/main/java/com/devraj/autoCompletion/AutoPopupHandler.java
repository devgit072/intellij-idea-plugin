package com.devraj.autoCompletion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class AutoPopupHandler extends TypedHandlerDelegate {

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        final int offset = editor.getCaretModel().getOffset();
        final int lineNumber = editor.getDocument().getLineNumber(offset);
        final int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
        final int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);
        final String lineContent = editor.getDocument().getText().substring(lineStartOffset, lineEndOffset);
        // Implement auto-popup for key OpName:
        if (lineContent.trim().endsWith("OpName:")) {
            AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
            return Result.STOP;
        }
        // We can implement auto-popup when : is typed, but dont need for now.
        /*if (c == ':') {

        }*/
        //AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
        return super.charTyped(c, project, editor, file);
    }

    // There is also another function: checkAutoPopup but do not need to now.
}
