package com.aptech.coursemanagementserver.services.servicesImpl;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.BAD_REQUEST_EXCEPTION;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NoteDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.LessonTrackingId;
import com.aptech.coursemanagementserver.models.Note;
import com.aptech.coursemanagementserver.repositories.NoteRepository;
import com.aptech.coursemanagementserver.services.NoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public List<NoteDto> loadNotes(NoteDto dto) {
        try {
            List<Note> notes = noteRepository.findAllNotesByEnrollmentIdAndCourseId(
                    dto.getEnrollmentId(), dto.getCourseId());
            if (notes == null) {
                return new ArrayList<NoteDto>();
            }

            List<NoteDto> noteDtos = new ArrayList<>();
            for (Note note : notes) {
                NoteDto noteDto = toDto(note);
                noteDtos.add(noteDto);
            }

            return noteDtos;

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    @Override
    public NoteDto saveNote(NoteDto noteDto) {
        try {
            boolean isUpdated = noteRepository.findById(noteDto.getId()).isPresent();
            return addNote(noteDto, isUpdated);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(BAD_REQUEST_EXCEPTION);
        }
    }

    public void deleteNote(long id) {
        Optional<Note> note = noteRepository.findById(id);

        if (note.isPresent()) {
            noteRepository.delete(note.get());
        }
    }

    private NoteDto addNote(NoteDto noteDto, boolean isUpdated) {
        LessonTrackingId trackId = setTrackId(noteDto);
        Note note = isUpdated ? noteRepository.findById(noteDto.getId()).orElseThrow(
                () -> new NoSuchElementException(
                        "The note with noteId:[" + noteDto.getId() + "] is not exist."))
                : new Note();

        note.setTrackId(trackId)
                .setResumePoint(noteDto.getResumePoint())
                .setDescription(noteDto.getDescription());

        Note savedNote = noteRepository.save(note);
        NoteDto returnNoteDto = toDto(savedNote);
        return returnNoteDto;

    }

    private LessonTrackingId setTrackId(NoteDto noteDto) {
        LessonTrackingId trackId = new LessonTrackingId();

        trackId.setEnrollment_id(noteDto.getEnrollmentId())
                .setCourse_id(noteDto.getCourseId())
                .setSection_id(noteDto.getSectionId())
                .setLession_id(noteDto.getLessonId())
                .setVideo_id(noteDto.getVideoId());
        return trackId;
    }

    private NoteDto toDto(Note note) {
        NoteDto returnNoteDto = new NoteDto();
        returnNoteDto.setId(note.getId());
        returnNoteDto.setEnrollmentId(note.getTrackId().getEnrollment_id());
        returnNoteDto.setCourseId(note.getTrackId().getCourse_id());
        returnNoteDto.setSectionId(note.getTrackId().getSection_id());
        returnNoteDto.setLessonId(note.getTrackId().getLession_id());
        returnNoteDto.setVideoId(note.getTrackId().getVideo_id());
        returnNoteDto.setResumePoint(note.getResumePoint());
        returnNoteDto.setDescription(note.getDescription());
        returnNoteDto.setCreated_at(note.getCreated_at());
        return returnNoteDto;
    }
}
