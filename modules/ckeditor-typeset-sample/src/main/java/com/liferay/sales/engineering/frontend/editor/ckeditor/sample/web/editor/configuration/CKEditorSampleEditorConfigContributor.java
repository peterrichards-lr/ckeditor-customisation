package com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.editor.configuration;

import com.liferay.document.library.kernel.util.AudioProcessorUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ColorScheme;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.*;
import com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.constants.CKEditorSampleEditorConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;
import java.util.Map;

@Component(
        property = {"editor.name=ckeditor", "editor.name=ckeditor_classic"},
        service = EditorConfigContributor.class
)
public class CKEditorSampleEditorConfigContributor extends BaseEditorConfigContributor {
    private static final Log _log = LogFactoryUtil.getLog("");

    public CKEditorSampleEditorConfigContributor() {
        _log.info("Hello from Liferay");
    }

    @Override
    public void populateConfigJSONObject(
            JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
            ThemeDisplay themeDisplay,
            RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

        _log.info("Inside populateConfigJSONObject");

        jsonObject.put("allowedContent", Boolean.TRUE);

        StringBundler sb = new StringBundler(5);

        sb.append("cke_editable html-editor");

        ColorScheme colorScheme = themeDisplay.getColorScheme();

        if (Validator.isNotNull(colorScheme.getCssClass())) {
            sb.append(StringPool.SPACE);
            sb.append(HtmlUtil.escape(colorScheme.getCssClass()));
        }

        String cssClasses = GetterUtil.getString(
                inputEditorTaglibAttributes.get(
                        CKEditorSampleEditorConstants.ATTRIBUTE_NAMESPACE + ":cssClasses"));

        if (Validator.isNotNull(cssClasses)) {
            sb.append(StringPool.SPACE);
            sb.append(HtmlUtil.escape(cssClasses));
        }

        jsonObject.put(
                "bodyClass", sb.toString()
        ).put(
                "contentsCss",
                JSONUtil.putAll(
                        HtmlUtil.escape(themeDisplay.getClayCSSURL()),
                        HtmlUtil.escape(themeDisplay.getMainCSSURL()),
                        HtmlUtil.escape(
                                PortalUtil.getStaticResourceURL(
                                        themeDisplay.getRequest(),
                                        PortalUtil.getPathContext() +
                                                "/o/frontend-editor-ckeditor-web/ckeditor/skins" +
                                                "/moono-lexicon/editor.css")),
                        HtmlUtil.escape(
                                PortalUtil.getStaticResourceURL(
                                        themeDisplay.getRequest(),
                                        PortalUtil.getPathContext() +
                                                "/o/frontend-editor-ckeditor-web/ckeditor/skins" +
                                                "/moono-lexicon/dialog.css")))
        ).put(
                "contentsLangDirection",
                HtmlUtil.escapeJS(
                        getContentsLanguageDir(inputEditorTaglibAttributes))
        );

        String contentsLanguageId = getContentsLanguageId(
                inputEditorTaglibAttributes);

        contentsLanguageId = StringUtil.replace(contentsLanguageId, "iw", "he");
        contentsLanguageId = StringUtil.replace(contentsLanguageId, '_', '-');

        jsonObject.put(
                "contentsLanguage", contentsLanguageId
        ).put(
                "height", 265
        );

        String languageId = getLanguageId(themeDisplay);

        languageId = StringUtil.replace(languageId, "iw", "he");
        languageId = StringUtil.replace(languageId, '_', '-');

        jsonObject.put("language", languageId);

        boolean resizable = GetterUtil.getBoolean(
                (String)inputEditorTaglibAttributes.get(
                        CKEditorSampleEditorConstants.ATTRIBUTE_NAMESPACE + ":resizable"));

        if (resizable) {
            String resizeDirection = GetterUtil.getString(
                    inputEditorTaglibAttributes.get(
                            CKEditorSampleEditorConstants.ATTRIBUTE_NAMESPACE +
                                    ":resizeDirection"));

            jsonObject.put("resize_dir", resizeDirection);
        }

        jsonObject.put("resize_enabled", resizable);

        jsonObject.put(
                "autoSaveTimeout", 3000
        ).put(
                "closeNoticeTimeout", 8000
        ).put(
                "entities", Boolean.FALSE
        );

        String extraPlugins =
                "addimages,autogrow,autolink,colordialog,filebrowser," +
                        "itemselector,lfrpopup,media,stylescombo,videoembed";

        boolean inlineEdit = GetterUtil.getBoolean(
                (String)inputEditorTaglibAttributes.get(
                        CKEditorSampleEditorConstants.ATTRIBUTE_NAMESPACE + ":inlineEdit"));

        if (inlineEdit) {
            extraPlugins += ",ajaxsave,restore";
        }

        jsonObject.put(
                "extraPlugins", extraPlugins
        ).put(
                "filebrowserWindowFeatures",
                "title=" + _language.get(themeDisplay.getLocale(), "browse")
        ).put(
                "pasteFromWordRemoveFontStyles", Boolean.FALSE
        ).put(
                "pasteFromWordRemoveStyles", Boolean.FALSE
        ).put(
                "removePlugins", "elementspath"
        ).put(
                "stylesSet", _getStyleFormatsJSONArray(themeDisplay.getLocale())
        ).put(
                "title", false
        );

        JSONArray toolbarSimpleJSONArray = _getToolbarSimpleJSONArray(
                inputEditorTaglibAttributes);

        jsonObject.put(
                "toolbar_editInPlace", toolbarSimpleJSONArray
        ).put(
                "toolbar_email", toolbarSimpleJSONArray
        ).put(
                "toolbar_liferay", toolbarSimpleJSONArray
        ).put(
                "toolbar_liferayArticle", toolbarSimpleJSONArray
        ).put(
                "toolbar_phone", toolbarSimpleJSONArray
        ).put(
                "toolbar_simple", toolbarSimpleJSONArray
        ).put(
                "toolbar_tablet", toolbarSimpleJSONArray
        ).put(
                "toolbar_text_advanced",
                _getToolbarTextAdvancedJSONArray(inputEditorTaglibAttributes)
        ).put(
                "toolbar_text_simple",
                _getToolbarTextSimpleJSONArray(inputEditorTaglibAttributes)
        );
    }

    protected boolean isShowSource(
            Map<String, Object> inputEditorTaglibAttributes) {

        return GetterUtil.getBoolean(
                inputEditorTaglibAttributes.get(
                        CKEditorSampleEditorConstants.ATTRIBUTE_NAMESPACE + ":showSource"),
                true);
    }


    private JSONObject _getStyleFormatJSONObject(
            String styleFormatName, String element, String cssClass) {

        return JSONUtil.put(
                "attributes",
                () -> {
                    if (Validator.isNotNull(cssClass)) {
                        return JSONUtil.put("class", cssClass);
                    }

                    return null;
                }
        ).put(
                "element", element
        ).put(
                "name", styleFormatName
        );
    }

    private JSONArray _getStyleFormatsJSONArray(Locale locale) {
        return JSONUtil.putAll(
                _getStyleFormatJSONObject(
                        _language.get(locale, "normal"), "p", null),
                _getStyleFormatJSONObject(
                        _language.format(locale, "heading-x", "1"), "h1", null),
                _getStyleFormatJSONObject(
                        _language.format(locale, "heading-x", "2"), "h2", null),
                _getStyleFormatJSONObject(
                        _language.format(locale, "heading-x", "3"), "h3", null),
                _getStyleFormatJSONObject(
                        _language.format(locale, "heading-x", "4"), "h4", null),
                _getStyleFormatJSONObject(
                        _language.get(locale, "preformatted-text"), "pre", null),
                _getStyleFormatJSONObject(
                        _language.get(locale, "cited-work"), "cite", null),
                _getStyleFormatJSONObject(
                        _language.get(locale, "computer-code"), "code", null),
                _getStyleFormatJSONObject(
                        _language.get(locale, "info-message"), "div",
                        "overflow-auto portlet-msg-info"),
                _getStyleFormatJSONObject(
                        _language.get(locale, "alert-message"), "div",
                        "overflow-auto portlet-msg-alert"),
                _getStyleFormatJSONObject(
                        _language.get(locale, "error-message"), "div",
                        "overflow-auto portlet-msg-error"));
    }

    private JSONArray _getToolbarSimpleJSONArray(
            Map<String, Object> inputEditorTaglibAttributes) {

        return JSONUtil.putAll(
                toJSONArray("['Undo', 'Redo']"),
                toJSONArray("['Styles', 'Bold', 'Italic', 'Underline']"),
                toJSONArray("['Superscript', 'Subscript']"),
                toJSONArray("['NumberedList', 'BulletedList']"),
                toJSONArray("['Indent', 'Outdent']"),
                toJSONArray("['Link', Unlink]"),
                toJSONArray("['Table', 'ImageSelector', 'VideoSelector']")
        ).put(
                () -> {
                    if (AudioProcessorUtil.isEnabled()) {
                        return toJSONArray("['AudioSelector']");
                    }

                    return null;
                }
        ).put(
                () -> {
                    if (isShowSource(inputEditorTaglibAttributes)) {
                        return toJSONArray("['Source', 'Expand']");
                    }

                    return null;
                }
        );
    }

    private JSONArray _getToolbarTextAdvancedJSONArray(
            Map<String, Object> inputEditorTaglibAttributes) {

        return JSONUtil.putAll(
                toJSONArray("['Undo', 'Redo']"), toJSONArray("['Styles']"),
                toJSONArray("['FontColor', 'BGColor']"),
                toJSONArray("['Bold', 'Italic', 'Underline', 'Strikethrough']"),
                toJSONArray("['Superscript', 'Subscript']"),
                toJSONArray("['RemoveFormat']"),
                toJSONArray("['NumberedList', 'BulletedList']"),
                toJSONArray("['Indent', 'Outdent']"),
                toJSONArray("['Link', Unlink]")
        ).put(
                () -> {
                    if (isShowSource(inputEditorTaglibAttributes)) {
                        return toJSONArray("['Source', 'Expand']");
                    }

                    return null;
                }
        );
    }

    private JSONArray _getToolbarTextSimpleJSONArray(
            Map<String, Object> inputEditorTaglibAttributes) {

        return JSONUtil.putAll(
                toJSONArray("['Undo', 'Redo']"),
                toJSONArray("['Styles', 'Bold', 'Italic', 'Underline']"),
                toJSONArray("['Superscript', 'Subscript']"),
                toJSONArray("['NumberedList', 'BulletedList']"),
                toJSONArray("['Indent', 'Outdent']"),
                toJSONArray("['Link', Unlink]")
        ).put(
                () -> {
                    if (isShowSource(inputEditorTaglibAttributes)) {
                        return toJSONArray("['Source', 'Expand']");
                    }

                    return null;
                }
        );
    }

    @Reference
    private Language _language;

}
