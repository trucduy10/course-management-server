package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.VIDEO;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.aptech.coursemanagementserver.services.VideoService;
import com.aptech.coursemanagementserver.services.authServices.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
@Tag(name = "Video Stream Endpoints")
public class VideoStreamController {

    private final VideoService videoStreamService;
    private final JwtService jwtService;

    // @GetMapping("/stream/{fileType}/{fileName}")
    // @Operation(summary = "[ANYROLE] - Partial serving video content")
    // public Mono<ResponseEntity<byte[]>> streamVideo(
    // @RequestHeader(value = "Range", required = false) String httpRangeList,
    // @PathVariable("fileType") String fileType,
    // @PathVariable("fileName") String fileName) {
    // return Mono.just(videoStreamService.prepareVideoContent(fileName, fileType,
    // httpRangeList));
    // }
    @GetMapping("/stream/{fileType}/{fileName}")
    @Operation(summary = "[ANYROLE] - Partial serving video content")

    public ResponseEntity<StreamingResponseBody> streamVideo(
            @RequestHeader(value = "Range", required = false) String httpRangeList,
            @PathVariable("fileType") String fileType,
            @PathVariable("fileName") String fileName,
            @RequestParam String token) throws IOException {

        String userName = null;

        userName = jwtService.extractUsername(token);

        if (userName == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String range = httpRangeList == null ? "1048576-" : httpRangeList;
        String filePathString = Paths.get(VIDEO, fileName + "." + fileType).toString();
        return videoStreamService.loadPartialMediaFile(filePathString,
                range);
    }

    @GetMapping("/caption/{fileName}")
    @Operation(summary = "[ANYROLE] - Get Caption of Video")
    public ResponseEntity<byte[]> getCaption(

            @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareCaptionContent(fileName);
    }
}
