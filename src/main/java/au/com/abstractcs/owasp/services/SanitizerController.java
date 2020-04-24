package au.com.abstractcs.owasp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

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
        return new SanitizedResponse(policyFactory.sanitize(srcText));
    }

    private class SanitizedResponse {
        private String response;

        public SanitizedResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return this.response;
        }
    }
}