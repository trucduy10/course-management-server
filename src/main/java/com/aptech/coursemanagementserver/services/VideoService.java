package com.aptech.coursemanagementserver.services;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.aptech.coursemanagementserver.dtos.VideoDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Video;

public interface VideoService {
    public Video findVideoByName(String videoName);

    public List<Video> findAll();

    public VideoDto findByLessonId(long lessonId);

    public BaseDto save(VideoDto video, long lessonId);

    public BaseDto update(VideoDto video);

    public List<Video> saveAll(List<VideoDto> videos);

    public BaseDto delete(long sectionId);

    ResponseEntity<byte[]> prepareVideoContent(final String fileName, final String fileType, final String range);

    ResponseEntity<byte[]> prepareCaptionContent(final String fileName);

    public ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, String rangeValues)
            throws IOException;;

    public ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath,
            long fileStartPos, long fileEndPos) throws IOException;

    public ResponseEntity<StreamingResponseBody> loadEntireMediaFile(String localMediaFilePath) throws IOException;

}
