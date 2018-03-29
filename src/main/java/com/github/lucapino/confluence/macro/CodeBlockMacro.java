package com.github.lucapino.confluence.macro;

import com.github.lucapino.confluence.util.StringUtils;

import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a confluence Code Block Macro.
 *
 * @author Jonathon Hope
 * @see <a href="https://confluence.atlassian.com/display/DOC/Code+Block+Macro">
 * Confluence Page describing the Macro</a>
 */
public class CodeBlockMacro {

    /**
     * Stores the parameters of this code block macro.
     */
    private EnumMap<Parameters, String> parameters;
    /**
     * The code of this CodeBlockMacro.
     */
    private String body;

    /**
     * Parameters are options that you can set to control the content or format of
     * the macro output.
     */
    public enum Parameters {
        /**
         * Can be either {@literal true} or {@literal false}
         * Default: false
         */
        COLLAPSE,
        /**
         * When Show line numbers is selected, this value defines the number of the
         * first line of code.
         * Default: 1
         */
        FIRSTlINE,
        /**
         * Specifies the language (or environment) for syntax highlighting. The default
         * language is Java but you can choose from:
         * {@link com.softwareleaf.confluence.rest.macro.CodeBlockMacro.Languages}
         *
         * @see CodeBlockMacro.Languages
         */
        LANGUAGE,
        /**
         * If selected, line numbers will be shown to the left of the lines of code.
         * Default: false
         */
        LINENUMBERS,
        /**
         * Specifies the colour scheme used for displaying your code block.
         * Many of these themes are based on the default colour schemes of popular
         * integrated development environments (IDEs). The default theme is
         * Confluence (also known as Default), which is typically black and coloured
         * text on a blank background.
         * However, you can also choose from one of the following other
         * popular themes:
         *
         * @see CodeBlockMacro.Themes
         */
        THEME,
        /**
         * Adds a title to the code block. If specified, the title will be displayed in a header row at the top
         * of the code block.
         * Default: none
         */
        TITLE;

        @Override
        public String toString() {
            // replace the underscore with a slash and print as lower case
            return name().replace("_", "/").toLowerCase();
        }

    }

    /**
     * The enumerated list of languages supported by the confluence code macro.
     */
    public enum Languages {
        ACTION_SCRIPT_3("actionscript3"),
        BASH("bash"),
        C_SHARP("c#"),     // (C#)
        COLD_FUSION("coldfusion"),
        CPP("cpp"),        //(C++)
        CSS("css"),
        DELPHI("delphi"),
        DIFF("diff"),
        ERLANG("erlang"),
        GROOVY("groovy"),
        HTML_XML("xml"),   // html/xml
        JAVA("java"),
        JAVA_FX("javafx"),
        JAVA_SCRIPT("js"),       // JavaScript
        NONE("none"),       // (no syntax highlighting)
        PERL("perl"),
        PHP("php"),
        POWER_SHELL("powershell"),
        PYTHON("python"),
        RUBY("ruby"),
        SCALA("scala"),
        SQL("sql"),
        VB("vb");

        public final String value;

        Languages(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * The available values for the Theme parameter.
     *
     * @see CodeBlockMacro.Parameters#THEME
     */
    public enum Themes {
        D_JANGO,
        EMACS,
        FADE_TO_GREY,
        MIDNIGHT,
        R_DARK,
        ECLIPSE,
        CONFLUENCE;

        @Override
        public String toString() {
            return StringUtils.convertToUpperCamel(super.toString());
        }
    }

    /**
     * Constructor.
     *
     * @param builder the builder instance to use as a factory.
     * @see CodeBlockMacro.Builder
     */
    protected CodeBlockMacro(final Builder builder) {
        this.parameters = builder.parameters;
        this.body = builder.code;
    }

    /**
     * Converts this CodeBlockMacro into confluence markup form.
     *
     * @return a {@code String} containing the markup generated by this {@code CodeBlockMacro}.
     * @see <a href="https://confluence.atlassian.com/display/DOC/Code+Block+Macro">
     * Confluence Page describing the Macro</a>
     */
    public String toMarkup() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ac:structured-macro ac:name=\"code\">");
        for (Map.Entry<Parameters, String> entry : parameters.entrySet()) {
            sb.append("<ac:parameter ac:name=\"");
            sb.append(entry.getKey().toString());
            sb.append("\">");
            sb.append(entry.getValue());
            sb.append("</ac:parameter>");
        }
        sb.append("<ac:plain-text-body>");
        sb.append(body);
        sb.append("</ac:plain-text-body>");
        sb.append("</ac:structured-macro>");
        return sb.toString();
    }

    /**
     * Builder factory method.
     *
     * @return a {@code Builder} instance for chain-building a CodeBlockMacro.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A class for implementing the Builder Pattern for {@code CodeBlockMacro}.
     */
    public static class Builder {
        private EnumMap<Parameters, String> parameters;
        private String code;

        /**
         * Constructor.
         */
        private Builder() {
            parameters = new EnumMap<>(Parameters.class);
        }

        /**
         * @return a new instance of {@code CodeBlockMacro}.
         */
        public CodeBlockMacro build() {
            return new CodeBlockMacro(this);
        }

        /**
         * <p>Example
         * <pre>{@literal
         *      <ac:parameter ac:name=\"language\">xml</ac:parameter>
         * }</pre>
         * <p> would be represented as:
         * <pre>{@code
         *      CodeBlockMacro.builder().language(Languages.HTML_XML)
         * }</pre>
         *
         * @param l the Languages enum.
         * @return {@code this}.
         * @see CodeBlockMacro.Languages
         */
        public Builder language(Languages l) {
            parameters.putIfAbsent(Parameters.LANGUAGE, l.value);
            return this;
        }

        /**
         * <p>Makes the macro collapsible:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"collapse\">true</ac:parameter>
         * }</pre>
         *
         * @return {@code this}.
         */
        public Builder collapse() {
            parameters.putIfAbsent(Parameters.COLLAPSE, "true");
            return this;
        }

        /**
         * Enables linenumbers:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"linenumbers\">true</ac:parameter>
         * }</pre>
         *
         * @return {@code this}.
         */
        public Builder showLineNumbers() {
            parameters.putIfAbsent(Parameters.LINENUMBERS, "true");
            return this;
        }

        /**
         * If {@code Parameters.LINENUMBERS} is set, sets the first line number to start at {@code first}:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"linenumbers\">true</ac:parameter><ac:parameter ac:name=\"firstline\">1</ac:parameter>
         * }</pre>
         *
         * @param first the first line number.
         * @return {@code this}.
         */
        public Builder firstline(int first) {

            if (parameters.containsKey(Parameters.LINENUMBERS)) {
                parameters.putIfAbsent(Parameters.FIRSTlINE, Integer.toString(first));
            }

            return this;
        }

        /**
         * <p>Adds a theme, for example; adding eclipse theme:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"theme\">Eclipse</ac:parameter>
         * }</pre>
         *
         * @param theme the {@code Theme} to use.
         * @return {@code this}.
         */
        public Builder theme(Themes theme) {
            parameters.putIfAbsent(Parameters.THEME, theme.toString());
            return this;
        }

        /**
         * <p>Sets the title for the code block, for example, here we call it {@literal "Request"}:
         * <pre>{@literal
         *      <ac:parameter ac:name=\"title\">Request</ac:parameter>
         * }</pre>
         *
         * @param title the title for this {@code CodeBlockMacto}.
         * @return {@code this}
         */
        public Builder title(String title) {
            parameters.putIfAbsent(Parameters.TITLE, title);
            return this;
        }

        /**
         * The code to be displayed by this code body.
         *
         * @param code the code contents, as a String.
         * @return {@code this}.
         */
        public Builder code(String code) {
            // escape code in <![CDATA[ ... ]]> wrapper
            this.code = "<![CDATA[" + code + "]]>";
            return this;
        }
    }

}
