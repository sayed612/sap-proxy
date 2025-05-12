package com.example.sapproxy;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/data")
public class SAPProxyController {

    @GetMapping
    public ResponseEntity<String> getOData(
            @RequestParam String service,
            @RequestParam String entity,
            @RequestParam(required = false) String filter
    ) {
        try {
            // الخدمات والـ EntitySets المسموح بيها
            Map<String, List<String>> allowed = Map.of(
                "ZGW_CUSTOMERCOLLECTIONMD_SRV", List.of("CompanyCodeDetailsSet", "CustomerDetailsSet"),
                "Z_VENDOR_SRV", List.of("VendorSet")
            );

            if (!allowed.containsKey(service) || !allowed.get(service).contains(entity)) {
                return ResponseEntity.badRequest().body("Service or Entity not allowed.");
            }

            // تكوين رابط الـ OData
            String baseUrl = "http://192.168.0.10:8000/sap/opu/odata/sap/";
            String url = baseUrl + service + "/" + entity + "?$format=json";

            if (filter != null && !filter.isEmpty()) {
                url += "&$filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8);
            }

            // إعدادات الاتصال
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("EX.NESMA", "Sayed@123456789"); // <-- غيّرهم
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }
}
