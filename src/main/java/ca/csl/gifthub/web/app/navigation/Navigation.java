package ca.csl.gifthub.web.app.navigation;

import org.apache.commons.lang3.StringUtils;

public class Navigation {

    public enum Type {
        FORWARD, REDIRECT;
    }

    private final String url;
    private final Type type;

    public Navigation(String url, Type type) {
        this.url = url;
        this.type = type;
    }

    public String nav(String... parameters) throws NavigationException {
        if (parameters.length != StringUtils.countMatches(this.url, "{")) {
            throw new NavigationException(this.url);
        }
        String effectiveUrl = this.url;
        for (String parameter : parameters) {
            effectiveUrl = this.url.replaceFirst("\\{[a-z]+\\}", parameter);
        }
        if (this.type.equals(Type.FORWARD)) {
            return "forward:/" + effectiveUrl;
        } else {
            return "redirect:/" + effectiveUrl;
        }
    }
}
