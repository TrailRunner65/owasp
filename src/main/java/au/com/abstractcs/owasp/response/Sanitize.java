package au.com.abstractcs.owasp.response;

import java.util.List;

public class Sanitize {
    private String response;
    private List removedItems;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(List removedItems) {
        this.removedItems = removedItems;
    }
}
