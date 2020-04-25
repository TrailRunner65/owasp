package au.com.abstractcs.owasp.services;

import org.owasp.html.HtmlChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SanitizerController {

//    @Autowired
//    private Sanitizers sanitizers;

    @RequestMapping(value="/sanitize", produces=MediaType.APPLICATION_JSON_VALUE)
    public SanitizedResponse sanitize(@RequestParam(value = "src") String srcText, @RequestParam(value = "sanitizer", required=false) String sanitizer) {
        PolicyFactory policyFactory;
        if (sanitizer != null) {
            if (sanitizer.equalsIgnoreCase("LINKS")) {
                policyFactory = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
                System.out.println(1);
            } else if (sanitizer.equalsIgnoreCase("STYLES")) {
                policyFactory = Sanitizers.FORMATTING.and(Sanitizers.STYLES);
                System.out.println(2);
            } else if (sanitizer.equalsIgnoreCase("FORMATTING")) {
                policyFactory = Sanitizers.FORMATTING.and(Sanitizers.FORMATTING);
                System.out.println(3);
            } else if (sanitizer.equalsIgnoreCase("BLOCKS")) {
                policyFactory = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
                System.out.println(4);
            } else {
                policyFactory = new HtmlPolicyBuilder().toFactory();
                System.out.println(5);
            }
        } else {
            policyFactory = new HtmlPolicyBuilder().toFactory();
            System.out.println(5);
        }
        List<String> changeList = new ArrayList<>();
        SanitizedResponse response = new SanitizedResponse(policyFactory.sanitize(srcText, new ChangeListener(), changeList));
        response.setRemovedItems(changeList);
        return response;
    }

    class ChangeListener implements HtmlChangeListener<List<String>> {
        @Override
        public void discardedTag(List<String> context, String elementName) {
            System.out.println(elementName + " tag found");
            context.add(elementName);
        }

        @Override
        public void discardedAttributes(List<String> context, String attributeName, String... strings2) {
            System.out.println(attributeName + " tag found");
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