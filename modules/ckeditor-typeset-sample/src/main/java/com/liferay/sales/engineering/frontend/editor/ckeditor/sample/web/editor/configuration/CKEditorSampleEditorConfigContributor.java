package com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.editor.configuration;

import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.constants.CKEditorSamplePortletKeys;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

@Component(
        property = {
            "editor.name=ckeditor", "editor.name=ckeditor_se_sample",
            "javax.portlet.name=" + CKEditorSamplePortletKeys.CKEDITOR_SAMPLE
        },
        service = EditorConfigContributor.class
)
public class CKEditorSampleEditorConfigContributor extends BaseEditorConfigContributor {
    @Override
    public void populateConfigJSONObject(JSONObject jsonObject, Map<String, Object> map, ThemeDisplay themeDisplay, RequestBackedPortletURLFactory requestBackedPortletURLFactory) {
        JSONArray toolbarJSONArray = jsonObject.getJSONArray(CKEditorSamplePortletKeys.TOOLBAR.SIMPLE);
        toolbarJSONArray.put(toJSONArray("['Superscript', 'Subscript']"));
    }
}
