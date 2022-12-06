package com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.portlet;

import com.liferay.sales.engineering.frontend.editor.ckeditor.sample.web.constants.CKEditorSamplePortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author peterrichards
 */
@Component(
	immediate = true,
	property = {
			"com.liferay.portlet.css-class-wrapper=portlet-ckeditor-sample",
			"com.liferay.portlet.display-category=category.sample",
			"com.liferay.portlet.instanceable=true",
			"com.liferay.portlet.layout-cacheable=true",
			"com.liferay.portlet.private-request-attributes=false",
			"com.liferay.portlet.private-session-attributes=false",
			"com.liferay.portlet.render-weight=50",
			"com.liferay.portlet.use-default-template=true",
			"javax.portlet.display-name=CKEditor Sample",
			"javax.portlet.expiration-cache=0",
			"javax.portlet.init-param.template-path=/META-INF/resources/",
			"javax.portlet.init-param.view-template=/view.jsp",
			"javax.portlet.name=" + CKEditorSamplePortletKeys.CKEDITOR_SAMPLE,
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user",
			"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class CKEditorSamplePortlet extends MVCPortlet {
}