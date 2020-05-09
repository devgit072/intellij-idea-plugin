package com.devraj.codeGeneration;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

// Basically this class will place the template.xml bundled in the plugin.
// When the plugin will be installed, then template.xml will be placed inside /home/devraj/.IdeaIC2019.3/config/templates folder.
// Any user created template will be added in config/templates/user.xml file.
public class TemplateProvider implements DefaultLiveTemplatesProvider {
    @Override
    public String[] getDefaultLiveTemplateFiles() {
        // Give the list template.xml files whose content will be appended in user.xml and all will be placed
        // inside config/templates folder.
        return new String[] {"liveTemplates/template.xml", "liveTemplates/yamlTemplate.xml"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}