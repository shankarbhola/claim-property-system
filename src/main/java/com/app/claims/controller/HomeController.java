package com.app.claims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HomeController {

    @GetMapping("/ping")
    public String ping(){
        return "Claim Property service is up and running";
    }

    private static final String LOG_FILE_PATH = "./app.log";

    @GetMapping("/logs")
    public ResponseEntity<String> getLogContents(@RequestParam(defaultValue = "100") int lines) {
        try {

            Path path = Paths.get(LOG_FILE_PATH).toAbsolutePath().normalize();
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            List<String> allLines = Files.readAllLines(path);
            int totalLines = allLines.size();
            int fromIndex = Math.max(0, totalLines - lines);

            List<String> lastLines = allLines.subList(fromIndex, totalLines);
            String content = lastLines.stream().collect(Collectors.joining("\n"));
            // Dark theme HTML with inline CSS
            String htmlFormatted = """
                <html>
                    <head>
                        <style>
                            body { 
                                background-color: #121212; 
                                color: #FFFFFF; 
                                font-family: monospace;
                                padding: 20px;
                                margin: 0;
                            }
                        </style>
                    </head>
                    <body>
                        <pre>%s</pre>
                    </body>
                </html>
                """.formatted(content);

            return ResponseEntity.ok(htmlFormatted);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading log file.");
        }
    }

}
