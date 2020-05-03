package com.devraj.codeGeneration;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

// Create a custom context. We are creating yaml context, because intellij plugin will ask that template.xml is applicable
// in which context, and we have to say that it is applicable in yaml context.
// In our plugin.xml, we can mention yaml or YAML because those two keywords we have defined here.
// We mention yaml context, then any file whose suffix ends with yaml or yml will be applicable.
public class YamlContext extends TemplateContextType {

    // YAML is id and yaml is presentable name.
    // In intellij idea settings, yaml(because it is presentable name) will come in list of context along with others context.
    protected YamlContext() {
        super("YAML", "yaml");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        String fileName = file.getName();
        return fileName.endsWith("yaml") || fileName.endsWith("yml");
    }
}
