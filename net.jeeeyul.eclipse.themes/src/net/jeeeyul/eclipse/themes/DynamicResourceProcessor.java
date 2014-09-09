package net.jeeeyul.eclipse.themes;

import javax.inject.Inject;

import net.jeeeyul.eclipse.themes.css.internal.dynamicresource.JTDynamicResourceLocator;
import net.jeeeyul.eclipse.themes.internal.Debug;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.RunAndTrack;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
@SuppressWarnings("restriction")
public class DynamicResourceProcessor {
	private static final String THEME_ENGINE_KEY = "org.eclipse.e4.ui.css.swt.theme";
	@Inject
	IEclipseContext ctx;

	@Execute
	public void trackThemeInstance() {
		Debug.println("DynamicResourceProcessor Started.");
		ctx.runAndTrack(new RunAndTrack() {
			@Override
			public boolean changed(IEclipseContext context) {
				Object stylingEngine = context.get(IStylingEngine.SERVICE_NAME);

				if (stylingEngine != null) {
					IThemeEngine themeEngine = (IThemeEngine) Display.getDefault().getData(THEME_ENGINE_KEY);
					CSSEngine cssEngine = WidgetElement.getEngine(Display.getDefault());
					if (themeEngine != null) {
						themeEngine.registerResourceLocator(new JTDynamicResourceLocator());
						Debug.println("Dynamic Resource Locator is installed on Theme Engine");
						return false;
					}

					else if (cssEngine == null) {
						return true;
					}
					cssEngine.getResourcesLocatorManager().registerResourceLocator(new JTDynamicResourceLocator());
					Debug.println("Dynamic Resource Locator is installed on CSS Engine");
					return false;
				}
				return true;
			}
		});

	}
}