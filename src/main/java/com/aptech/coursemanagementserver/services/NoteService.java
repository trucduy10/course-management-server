package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.NoteDto;

public interface NoteService {
    List<NoteDto> loadNotes(NoteDto noteDto);

    NoteDto saveNote(NoteDto noteDto);

    void deleteNote(long id);
}
