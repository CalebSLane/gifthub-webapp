package ca.csl.gifthub.web.app.navigation;

public class NavigationException extends Exception {

    private static final long serialVersionUID = -2855585939528009904L;

    NavigationException(String url) {
        super("Could not create navigation string for url: " + url);
    }

}
