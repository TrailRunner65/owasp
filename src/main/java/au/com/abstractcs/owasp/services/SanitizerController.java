package au.com.abstractcs.owasp.services;

import org.owasp.html.HtmlChangeListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SanitizerController {

    @RequestMapping(value="/sanitize", method=POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public SanitizedResponse sanitize(@RequestParam(value = "src") String srcText, @RequestParam(value = "sanitizer", required=false) List<String> sanitizers) {

        PolicyFactory policyFactory = new HtmlPolicyBuilder().toFactory();
        if (sanitizers != null) {
            if (sanitizers.contains("LINKS")) {
                policyFactory = policyFactory.and(Sanitizers.LINKS);
            }
            if (sanitizers.contains("STYLES")) {
                policyFactory = policyFactory.and(Sanitizers.STYLES);
            }
            if (sanitizers.contains("FORMATTING")) {
                policyFactory = policyFactory.and(Sanitizers.FORMATTING);
            }
            if (sanitizers.contains("BLOCKS")) {
                policyFactory = policyFactory.and(Sanitizers.BLOCKS);
            }
            if (sanitizers.contains("IMAGES")) {
                policyFactory = policyFactory.and(Sanitizers.IMAGES);
            }
            if (sanitizers.contains("TABLES")) {
                policyFactory = policyFactory.and(Sanitizers.TABLES);
            }
        }
        List<String> changeList = new ArrayList<>();
        SanitizedResponse response = new SanitizedResponse(policyFactory.sanitize(srcText, new ChangeListener(), changeList));
        response.setRemovedItems(changeList);
        return response;
    }

    class ChangeListener implements HtmlChangeListener<List<String>> {
        @Override
        public void discardedTag(List<String> context, String elementName) {
            context.add(elementName);
        }

        @Override
        public void discardedAttributes(List<String> context, String attributeName, String... strings2) {
            context.add(attributeName);
        }
    }

    private class SanitizedResponse {
        private String response;
        private List removedItems;

        public SanitizedResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return this.response;
        }

        public List getRemovedItems() {
            return removedItems;
        }

        public void setRemovedItems(List removedItems) {
            this.removedItems = removedItems;
        }
    }
}